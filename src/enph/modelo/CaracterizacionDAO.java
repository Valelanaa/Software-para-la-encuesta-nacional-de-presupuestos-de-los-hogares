
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
     * Guarda o actualiza la caracterización y su detalle.
     */
    public boolean guardar(int idEncuesta, int idGrupo, int idFrecuencia, BigDecimal valor, Integer intencion) {
        Connection con = Conexion.init();
        if (con == null) return false;

        try {
            con.setAutoCommit(false);

            try (CallableStatement cs = con.prepareCall("{CALL PR_GUARDAR_CARACTERIZACION(?,?,?,?,?,?)}")) {
                cs.setInt(1, idEncuesta);
                cs.setInt(2, idGrupo);
                cs.setInt(3, idFrecuencia);

                if (valor == null) cs.setNull(4, Types.DECIMAL);
                else cs.setBigDecimal(4, valor);

                if (intencion == null) cs.setNull(5, Types.NUMERIC);
                else cs.setInt(5, intencion);

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
            System.err.println("Error al guardar la caracterización: " + e.getMessage());
            try { con.rollback(); } catch (SQLException ex) { }
            return false;

        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { }
            Conexion.close(con);
        }
    }

    /**
     * Consulta la respuesta de un grupo de alimentos.
     */
    public RespuestaGrupo obtener(int idEncuesta, int idGrupo) {
        Connection con = Conexion.init();
        if (con == null) return null;

        try (CallableStatement cs = con.prepareCall("{CALL PR_OBTENER_CARACTERIZACION(?,?,?)}")) {
            cs.setInt(1, idEncuesta);
            cs.setInt(2, idGrupo);
            cs.registerOutParameter(3, Types.REF_CURSOR);
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(3)) {
                if (rs != null && rs.next()) {
                    int idFrecuencia = rs.getInt(1);
                    BigDecimal valor = rs.getBigDecimal(2);
                    String texto = rs.getString(3);

                    Integer intencion = texto == null ? null :
                            (INTENCION_SI.equalsIgnoreCase(texto.trim()) ? 1 : 0);

                    return new RespuestaGrupo(idFrecuencia, valor, intencion);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar la respuesta: " + e.getMessage());
        } finally {
            Conexion.close(con);
        }

        return null;
    }

    /**
     * Lista las respuestas de la encuesta más reciente de un hogar.
     */
    public List<String[]> listarRespuestasPorHogar(String idHogar) throws SQLException {
        List<String[]> filas = new ArrayList<>();

        try (Connection con = Conexion.init()) {
            if (con == null) throw new SQLException("No fue posible conectar con Oracle.");

            try (CallableStatement cs = con.prepareCall("{CALL PR_LISTAR_RESPUESTAS_HOGAR(?,?)}")) {
                cs.setString(1, idHogar.trim());
                cs.registerOutParameter(2, Types.REF_CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                    while (rs != null && rs.next()) {
                        BigDecimal valor = rs.getBigDecimal(3);
                        String textoValor = valor == null ? "-" : valor.stripTrailingZeros().toPlainString();

                        String intencion = rs.getString(4);
                        String textoIntencion = intencion == null ? "-" :
                                (INTENCION_SI.equalsIgnoreCase(intencion.trim()) ? "Sí" : "No");

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
     * Obtiene los identificadores de los grupos que ya tienen respuesta.
     */
    public Set<Integer> listarGruposRespondidos(int idEncuesta) {
        Connection con = Conexion.init();
        if (con == null) return null;

        Set<Integer> ids = new HashSet<>();

        try (CallableStatement cs = con.prepareCall("{CALL PR_LISTAR_GRUPOS_RESPONDIDOS(?,?)}")) {
            cs.setInt(1, idEncuesta);
            cs.registerOutParameter(2, Types.REF_CURSOR);
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                while (rs != null && rs.next()) ids.add(rs.getInt(1));
            }

            return ids;

        } catch (SQLException e) {
            System.err.println("Error al consultar los grupos respondidos: " + e.getMessage());
            return null;
        } finally {
            Conexion.close(con);
        }
    }
}
