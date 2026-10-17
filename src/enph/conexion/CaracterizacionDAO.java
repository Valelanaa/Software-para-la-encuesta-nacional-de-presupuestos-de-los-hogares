
package enph.conexion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class CaracterizacionDAO {

    /**
     * Guarda una respuesta nueva o actualiza la existente.
     */
    public boolean guardar(int idEncuesta, int idGrupo,
                           int idFrecuencia, BigDecimal valor,
                           Integer intencion) {

        String sqlUpdate =
                "UPDATE CARACTERIZACION_COMPRA "
                + "SET ID_FRECUENCIA_COMPRA = ?, "
                + "VALOR_ULTIMA_COMPRA = ?, "
                + "INTENCION_DE_COMPRA = ? "
                + "WHERE ID_ENCUESTA = ? "
                + "AND ID_GRUPO_ALIMENTO = ?";

        String sqlInsert =
                "INSERT INTO CARACTERIZACION_COMPRA "
                + "(ID_CARACTERIZACION, ID_ENCUESTA, "
                + "ID_GRUPO_ALIMENTO, ID_FRECUENCIA_COMPRA, "
                + "VALOR_ULTIMA_COMPRA, INTENCION_DE_COMPRA) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection con = Conexion.init();

        if (con == null) {
            System.err.println("No se pudo conectar a Oracle.");
            return false;
        }

        try {
            // Primero intenta actualizar la respuesta existente.
            try (PreparedStatement up = con.prepareStatement(sqlUpdate)) {
                up.setInt(1, idFrecuencia);

                if (valor == null) {
                    up.setNull(2, Types.NUMERIC);
                } else {
                    up.setBigDecimal(2, valor);
                }

                if (intencion == null) {
                    up.setNull(3, Types.NUMERIC);
                } else {
                    up.setInt(3, intencion);
                }

                up.setInt(4, idEncuesta);
                up.setInt(5, idGrupo);

                if (up.executeUpdate() > 0) {
                    return true;
                }
            }

            // Si no existe, inserta una respuesta nueva.
            try (PreparedStatement ins = con.prepareStatement(sqlInsert)) {

                int id = siguienteId(con);

                System.out.println(
                        "ID_CARACTERIZACION generado: " + id
                );

                ins.setInt(1, id);
                ins.setInt(2, idEncuesta);
                ins.setInt(3, idGrupo);
                ins.setInt(4, idFrecuencia);

                if (valor == null) {
                    ins.setNull(5, Types.NUMERIC);
                } else {
                    ins.setBigDecimal(5, valor);
                }

                if (intencion == null) {
                    ins.setNull(6, Types.NUMERIC);
                } else {
                    ins.setInt(6, intencion);
                }

                ins.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al guardar la caracterización: "
                    + e.getMessage()
            );
            e.printStackTrace();
            return false;

        } finally {
            Conexion.close(con);
        }
    }

    /**
     * Calcula el siguiente ID.
     * Convierte el identificador a número si la columna es texto.
     */
    private int siguienteId(Connection con) throws SQLException {

        String sql =
                "SELECT NVL(MAX(TO_NUMBER(ID_CARACTERIZACION)), 0) + 1 "
                + "FROM CARACTERIZACION_COMPRA";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new SQLException("No se pudo generar el ID.");
    }

    /**
     * Consulta la respuesta guardada para un grupo de alimentos.
     */
    public RespuestaGrupo obtener(int idEncuesta, int idGrupo) {

        String sql =
                "SELECT ID_FRECUENCIA_COMPRA, "
                + "VALOR_ULTIMA_COMPRA, INTENCION_DE_COMPRA "
                + "FROM CARACTERIZACION_COMPRA "
                + "WHERE ID_ENCUESTA = ? "
                + "AND ID_GRUPO_ALIMENTO = ?";

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

                    int in = rs.getInt(3);
                    Integer intencion =
                            rs.wasNull() ? null : in;

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
}
