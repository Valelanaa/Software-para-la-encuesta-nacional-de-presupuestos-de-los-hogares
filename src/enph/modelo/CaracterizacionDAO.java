package enph.modelo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class CaracterizacionDAO {

    // Texto que se guarda en DETALLE_COMPRA.INTENCION_COMPRA (VARCHAR2).
    // Cámbialo aquí si en tu base usas otros valores.
    public static final String INTENCION_SI = "SI";
    public static final String INTENCION_NO = "NO";

    /**
     * Guarda una respuesta nueva o actualiza la existente.
     * La frecuencia va en CARACTERIZACION_COMPRA; el valor de la última compra y la
     * intención van en DETALLE_COMPRA, y solo existen si se respondieron las dos.
     * Todo se hace en una transacción.
     */
    public boolean guardar(int idEncuesta, int idGrupo,
                           int idFrecuencia, BigDecimal valor,
                           Integer intencion) {

        String sqlBuscar =
                "SELECT ID_CARACTERIZACION FROM CARACTERIZACION_COMPRA "
                + "WHERE ID_ENCUESTA = ? AND ID_GRUPO_ALIMENTO = ?";

        String sqlUpdate =
                "UPDATE CARACTERIZACION_COMPRA "
                + "SET ID_FRECUENCIA_COMPRA = ? "
                + "WHERE ID_CARACTERIZACION = ?";

        String sqlInsert =
                "INSERT INTO CARACTERIZACION_COMPRA "
                + "(ID_CARACTERIZACION, ID_ENCUESTA, "
                + "ID_GRUPO_ALIMENTO, ID_FRECUENCIA_COMPRA) "
                + "VALUES (?, ?, ?, ?)";

        Connection con = Conexion.init();

        if (con == null) {
            System.err.println("No se pudo conectar a Oracle.");
            return false;
        }

        try {
            con.setAutoCommit(false);

            // 1. ¿Ya existe la respuesta de este grupo en esta encuesta?
            String idCaracterizacion = null;
            try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
                ps.setInt(1, idEncuesta);
                ps.setInt(2, idGrupo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idCaracterizacion = rs.getString(1).trim();
                    }
                }
            }

            // 2. Actualiza la frecuencia o inserta la respuesta nueva
            if (idCaracterizacion != null) {
                try (PreparedStatement up = con.prepareStatement(sqlUpdate)) {
                    up.setInt(1, idFrecuencia);
                    up.setString(2, idCaracterizacion);
                    up.executeUpdate();
                }
            } else {
                idCaracterizacion = String.valueOf(
                        EncuestaDAO.siguienteId(con, "CARACTERIZACION_COMPRA", "ID_CARACTERIZACION"));
                try (PreparedStatement ins = con.prepareStatement(sqlInsert)) {
                    ins.setString(1, idCaracterizacion);
                    ins.setInt(2, idEncuesta);
                    ins.setInt(3, idGrupo);
                    ins.setInt(4, idFrecuencia);
                    ins.executeUpdate();
                }
            }

            // 3. Valor e intención de compra (tabla DETALLE_COMPRA)
            guardarDetalle(con, idCaracterizacion, valor, intencion);

            con.commit();
            return true;

        } catch (SQLException e) {
            System.err.println(
                    "Error al guardar la caracterización: "
                    + e.getMessage()
            );
            e.printStackTrace();
            try { con.rollback(); } catch (SQLException ignorada) { }
            return false;

        } finally {
            try { con.setAutoCommit(true); } catch (SQLException ignorada) { }
            Conexion.close(con);
        }
    }

    /**
     * Crea, actualiza o borra el DETALLE_COMPRA de una respuesta.
     * Las dos columnas son obligatorias en la tabla: si falta el valor o la
     * intención (la frecuencia no los pidió), la respuesta no tiene detalle.
     */
    private void guardarDetalle(Connection con, String idCaracterizacion,
                                BigDecimal valor, Integer intencion) throws SQLException {

        if (valor == null || intencion == null) {
            try (PreparedStatement del = con.prepareStatement(
                    "DELETE FROM DETALLE_COMPRA WHERE ID_CARACTERIZACION = ?")) {
                del.setString(1, idCaracterizacion);
                del.executeUpdate();
            }
            return;
        }

        String textoIntencion = (intencion == 1) ? INTENCION_SI : INTENCION_NO;

        try (PreparedStatement up = con.prepareStatement(
                "UPDATE DETALLE_COMPRA SET VALOR_ULTIMA_COMPRA = ?, INTENCION_COMPRA = ? "
                + "WHERE ID_CARACTERIZACION = ?")) {
            up.setBigDecimal(1, valor);
            up.setString(2, textoIntencion);
            up.setString(3, idCaracterizacion);
            if (up.executeUpdate() > 0) {
                return;
            }
        }

        try (PreparedStatement ins = con.prepareStatement(
                "INSERT INTO DETALLE_COMPRA "
                + "(ID_DETALLE_COMPRA, ID_CARACTERIZACION, VALOR_ULTIMA_COMPRA, INTENCION_COMPRA) "
                + "VALUES (?, ?, ?, ?)")) {
            ins.setString(1, String.valueOf(
                    EncuestaDAO.siguienteId(con, "DETALLE_COMPRA", "ID_DETALLE_COMPRA")));
            ins.setString(2, idCaracterizacion);
            ins.setBigDecimal(3, valor);
            ins.setString(4, textoIntencion);
            ins.executeUpdate();
        }
    }

    /**
     * Consulta la respuesta guardada para un grupo de alimentos.
     * El valor y la intención salen de DETALLE_COMPRA (null si no tiene detalle).
     */
    public RespuestaGrupo obtener(int idEncuesta, int idGrupo) {

        String sql =
                "SELECT C.ID_FRECUENCIA_COMPRA, "
                + "D.VALOR_ULTIMA_COMPRA, D.INTENCION_COMPRA "
                + "FROM CARACTERIZACION_COMPRA C "
                + "LEFT JOIN DETALLE_COMPRA D ON D.ID_CARACTERIZACION = C.ID_CARACTERIZACION "
                + "WHERE C.ID_ENCUESTA = ? "
                + "AND C.ID_GRUPO_ALIMENTO = ?";

        Connection con = Conexion.init();

        if (con == null) {
            return null;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEncuesta);
            ps.setInt(2, idGrupo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    int idFrecuencia = rs.getInt(1);
                    BigDecimal valor = rs.getBigDecimal(2);

                    String texto = rs.getString(3);
                    Integer intencion = (texto == null)
                            ? null
                            : (INTENCION_SI.equalsIgnoreCase(texto.trim()) ? 1 : 0);

                    return new RespuestaGrupo(
                            idFrecuencia, valor, intencion
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al consultar la respuesta: "
                    + e.getMessage()
            );
            e.printStackTrace();

        } finally {
            Conexion.close(con);
        }

        return null;
    }
    /**
     * Respuestas de la encuesta más reciente de un hogar.
     * Cada fila es {alimento, frecuencia, valor de la última compra,
     * volvería a comprar}, ordenada por grupo de alimento.
     * Devuelve una lista vacía si el hogar no tiene encuesta o respuestas.
     */
    public List<String[]> listarRespuestasPorHogar(String idHogar) throws SQLException {

        List<String[]> filas = new ArrayList<>();

        String sql = "SELECT G.NOMBRE_GRUPO_ALIMENTO, F.NOMBRE_FRECUENCIA_COMPRA, "
                   + "D.VALOR_ULTIMA_COMPRA, D.INTENCION_COMPRA "
                   + "FROM CARACTERIZACION_COMPRA C "
                   + "LEFT JOIN DETALLE_COMPRA D ON D.ID_CARACTERIZACION = C.ID_CARACTERIZACION "
                   + "JOIN GRUPO_ALIMENTO G ON G.ID_GRUPO_ALIMENTO = C.ID_GRUPO_ALIMENTO "
                   + "JOIN FRECUENCIA_COMPRA F ON F.ID_FRECUENCIA_COMPRA = C.ID_FRECUENCIA_COMPRA "
                   + "WHERE TO_NUMBER(C.ID_ENCUESTA) = "
                   + "  (SELECT MAX(TO_NUMBER(E.ID_ENCUESTA)) FROM ENCUESTA E "
                   + "   WHERE E.ID_HOGAR = ?) "
                   + "ORDER BY G.ID_GRUPO_ALIMENTO";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, idHogar.trim());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {

                        // Valor: sin ceros sobrantes; "-" si no aplica.
                        BigDecimal valor = rs.getBigDecimal(3);
                        String textoValor = (valor == null)
                                ? "-" : valor.stripTrailingZeros().toPlainString();

                        // Intención: texto de DETALLE_COMPRA; null = no aplica.
                        String intencion = rs.getString(4);
                        String textoIntencion = (intencion == null)
                                ? "-" : (INTENCION_SI.equalsIgnoreCase(intencion.trim()) ? "Sí" : "No");

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
        /**
     * IDs de los grupos de alimentos que ya tienen respuesta en la encuesta.
     * Devuelve null si falla la consulta.
     */
    public Set<Integer> listarGruposRespondidos(int idEncuesta) {
        String sql = "SELECT ID_GRUPO_ALIMENTO FROM CARACTERIZACION_COMPRA "
                   + "WHERE ID_ENCUESTA = ?";

        Connection con = Conexion.init();
        if (con == null) return null;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEncuesta);

            Set<Integer> ids = new HashSet<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt(1));
                }
            }
            return ids;

        } catch (SQLException e) {
            System.err.println("Error al consultar los grupos respondidos: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            Conexion.close(con);
        }
    }
}