/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.conexion;

/**
 *Acceso a datos de la tabla CARACTERIZACION_COMPRA: guarda y consulta
 * la respuesta de cada grupo de alimentos de una encuesta.
 * @author SALA-2
 */
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.ResultSet;

public class CaracterizacionDAO {

    /**
     * Guarda la respuesta de un grupo. valor e intencion pueden ser null
     * (son opcionales en el modelo). El id lo genera el sistema.
     */
    public boolean guardar(int idEncuesta, int idGrupo, int idFrecuencia,
                           BigDecimal valor, Integer intencion) {
        // Actualiza la respuesta existente de ese grupo en esa encuesta
        String sqlUpdate = "UPDATE CARACTERIZACION_COMPRA SET ID_FRECUENCIA_COMPRA = ?, "
                + "VALOR_ULTIMA_COMPRA = ?, INTENCION_DE_COMPRA = ? "
                + "WHERE ID_ENCUESTA = ? AND ID_GRUPO_ALIMENTO = ?";
        // Crea una respuesta nueva (se usa solo si el UPDATE no encontró fila)
        String sqlInsert = "INSERT INTO CARACTERIZACION_COMPRA "
                + "(ID_CARACTERIZACION, ID_ENCUESTA, ID_GRUPO_ALIMENTO, "
                + "ID_FRECUENCIA_COMPRA, VALOR_ULTIMA_COMPRA, INTENCION_DE_COMPRA) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            // Si el grupo ya fue respondido (el usuario volvió atrás), se actualiza
            try (PreparedStatement up = con.prepareStatement(sqlUpdate)) {
                up.setInt(1, idFrecuencia);
                // Valor e intención son opcionales: se guardan como NULL si no vienen
                if (valor == null) up.setNull(2, Types.NUMERIC);
                else up.setBigDecimal(2, valor);
                if (intencion == null) up.setNull(3, Types.NUMERIC);
                else up.setInt(3, intencion);
                up.setInt(4, idEncuesta);
                up.setInt(5, idGrupo);
                // executeUpdate devuelve cuántas filas cambió; si fue alguna, terminó
                if (up.executeUpdate() > 0) {
                    return true;
                }
            }

            // Si no existía, se inserta con id generado por el sistema
            try (PreparedStatement ins = con.prepareStatement(sqlInsert)) {
                // El id no lo escribe el usuario: se calcula como MAX + 1
                int id = EncuestaDAO.siguienteId(con, "CARACTERIZACION_COMPRA", "ID_CARACTERIZACION");

                ins.setInt(1, id);
                ins.setInt(2, idEncuesta);
                ins.setInt(3, idGrupo);
                ins.setInt(4, idFrecuencia);
                if (valor == null) ins.setNull(5, Types.NUMERIC);
                else ins.setBigDecimal(5, valor);
                if (intencion == null) ins.setNull(6, Types.NUMERIC);
                else ins.setInt(6, intencion);

                ins.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
             // La pantalla muestra un aviso genérico; el detalle ORA-... queda en la consola
            System.err.println("Error al guardar la caracterización: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // Siempre se cierra la conexión, haya o no error
            Conexion.close(con);
        }
    }
    // Respuesta ya guardada del grupo, o null si todavía no se ha respondido. */
public RespuestaGrupo obtener(int idEncuesta, int idGrupo) {
    String sql = "SELECT ID_FRECUENCIA_COMPRA, VALOR_ULTIMA_COMPRA, INTENCION_DE_COMPRA "
            + "FROM CARACTERIZACION_COMPRA WHERE ID_ENCUESTA = ? AND ID_GRUPO_ALIMENTO = ?";

    Connection con = Conexion.init();
    if (con == null) return null;

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idEncuesta);
        ps.setInt(2, idGrupo);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int idFrecuencia = rs.getInt(1);
                BigDecimal valor = rs.getBigDecimal(2); // null si estaba vacío
                // getInt devuelve 0 cuando el valor es NULL; wasNull() distingue
                // un NULL real de un 0 guardado (0 = "No")
                int in = rs.getInt(3);
                Integer intencion = rs.wasNull() ? null : in;
                return new RespuestaGrupo(idFrecuencia, valor, intencion);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al consultar la respuesta: " + e.getMessage());
        e.printStackTrace();
    } finally {
        Conexion.close(con);
    }
    return null;
}
}