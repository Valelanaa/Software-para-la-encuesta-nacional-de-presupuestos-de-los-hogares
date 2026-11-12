
package enph.modelo;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CaracterizacionDAO {

    public static final String INTENCION_SI = "SI";
    public static final String INTENCION_NO = "NO";

/**
 * Guarda o actualiza una caracterización y su detalle.
 * Devuelve false si el procedimiento no pudo realizar la operación.
 */
public boolean guardar(CaracterizacionCompra c) throws SQLException {

    if (c == null
            || c.getEncuesta() == null
            || c.getGrupo() == null
            || c.getFrecuencia() == null
            || c.getEncuesta().getIdEncuesta() <= 0
            || c.getGrupo().getId() <= 0
            || c.getFrecuencia().getIdFrecuencia() <= 0) {

        throw new SQLException("Los datos de la caracterización no son válidos.");
    }

    Integer intencion = c.getIntencionCompra() == null
            ? null
            : (c.getIntencionCompra() ? 1 : 0);

    try (Connection con = Conexion.init()) {

        if (con == null) {
            throw new SQLException("No fue posible conectar con Oracle.");
        }

        con.setAutoCommit(false);

        try (CallableStatement cs =
                con.prepareCall("{CALL PR_GUARDAR_CARACTERIZACION(?,?,?,?,?,?)}")) {

            cs.setInt(1, c.getEncuesta().getIdEncuesta());
            cs.setInt(2, c.getGrupo().getId());
            cs.setInt(3, c.getFrecuencia().getIdFrecuencia());

            if (c.getValorUltimaCompra() == null) {
                cs.setNull(4, Types.DECIMAL);
            } else {
                cs.setBigDecimal(4, c.getValorUltimaCompra());
            }

            if (intencion == null) {
                cs.setNull(5, Types.NUMERIC);
            } else {
                cs.setInt(5, intencion);
            }

            cs.registerOutParameter(6, Types.NUMERIC);
            cs.execute();

            if (cs.getInt(6) != 1) {
                con.rollback();
                return false;
            }
        }

        con.commit();
        return true;

    } catch (SQLException e) {
        throw e;
    }
}

/**
 * Consulta la respuesta guardada de un grupo.
 * Devuelve null únicamente cuando no existe una respuesta.
 */
public CaracterizacionCompra obtener(
        Encuesta encuesta,
        GrupoAlimento grupo,
        List<FrecuenciaCompra> frecuencias) throws SQLException {

    if (encuesta == null || grupo == null
            || encuesta.getIdEncuesta() <= 0 || grupo.getId() <= 0) {
        throw new SQLException("Los datos para consultar la caracterización no son válidos.");
    }

    try (Connection con = Conexion.init()) {

        if (con == null) {
            throw new SQLException("No fue posible conectar con Oracle.");
        }

        try (CallableStatement cs =
                con.prepareCall("{CALL PR_OBTENER_CARACTERIZACION(?,?,?)}")) {

            cs.setInt(1, encuesta.getIdEncuesta());
            cs.setInt(2, grupo.getId());
            cs.registerOutParameter(3, Types.REF_CURSOR);
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(3)) {

                if (rs == null || !rs.next()) {
                    return null;
                }

                int idFrecuencia = rs.getInt(1);
                BigDecimal valor = rs.getBigDecimal(2);
                String texto = rs.getString(3);

                Boolean intencion = texto == null
                        ? null
                        : INTENCION_SI.equalsIgnoreCase(texto.trim());

                FrecuenciaCompra frecuencia =
                        new FrecuenciaCompra(idFrecuencia, null);

                if (frecuencias != null) {
                    for (FrecuenciaCompra f : frecuencias) {
                        if (f.getIdFrecuencia() == idFrecuencia) {
                            frecuencia = f;
                            break;
                        }
                    }
                }

                CaracterizacionCompra c = new CaracterizacionCompra(
                        encuesta,
                        grupo,
                        frecuencia,
                        valor,
                        intencion
                );

                c.setIdCaracterizacion(rs.getInt(4));

                return c;
            }
        }
    }
}

/**
 * Obtiene los grupos que ya tienen respuesta.
 * Devuelve un conjunto vacío si todavía no hay respuestas.
 */
public Set<Integer> listarGruposRespondidos(int idEncuesta)
        throws SQLException {

    if (idEncuesta <= 0) {
        throw new SQLException("El identificador de la encuesta no es válido.");
    }

    Set<Integer> ids = new HashSet<>();

    try (Connection con = Conexion.init()) {

        if (con == null) {
            throw new SQLException("No fue posible conectar con Oracle.");
        }

        try (CallableStatement cs =
                con.prepareCall("{CALL PR_LISTAR_GRUPOS_RESPONDIDOS(?,?)}")) {

            cs.setInt(1, idEncuesta);
            cs.registerOutParameter(2, Types.REF_CURSOR);
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                while (rs != null && rs.next()) {
                    ids.add(rs.getInt(1));
                }
            }
        }
    }

    return ids;
}

/**
 * Lista las respuestas de la encuesta más reciente de un hogar.
 */
public List<String[]> listarRespuestasPorHogar(String idHogar)
        throws SQLException {

    if (idHogar == null || idHogar.trim().isEmpty()) {
        throw new SQLException("El identificador del hogar es obligatorio.");
    }

    List<String[]> filas = new ArrayList<>();

    try (Connection con = Conexion.init()) {

        if (con == null) {
            throw new SQLException("No fue posible conectar con Oracle.");
        }

        try (CallableStatement cs =
                con.prepareCall("{CALL PR_LISTAR_RESPUESTAS_HOGAR(?,?)}")) {

            cs.setString(1, idHogar.trim());
            cs.registerOutParameter(2, Types.REF_CURSOR);
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {

                while (rs != null && rs.next()) {

                    BigDecimal valor = rs.getBigDecimal(3);
                    String textoValor = valor == null
                            ? "-"
                            : valor.stripTrailingZeros().toPlainString();

                    String intencion = rs.getString(4);
                    String textoIntencion = intencion == null
                            ? "-"
                            : (INTENCION_SI.equalsIgnoreCase(intencion.trim())
                                ? "Sí" : "No");

                    filas.add(new String[]{
                        rs.getString(1) == null ? "" : rs.getString(1).trim(),
                        rs.getString(2) == null ? "" : rs.getString(2).trim(),
                        textoValor,
                        textoIntencion
                    });
                }
            }
        }
    }

    return filas;
}
}
