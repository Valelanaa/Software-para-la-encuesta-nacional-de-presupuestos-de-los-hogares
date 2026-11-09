/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Consulta las asignaciones (Hogar - Encuestador) mediante
 * PR_BUSCAR_ASIGNACION_HOGAR y PR_LISTAR_ASIGNACIONES_ENC.
 *
 * Asignar y desasignar siguen en HogarDAO
 * (asignarHogaresAEncuestador / desasignarHogar).
 */
public class AsignacionDAO {

    private static String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private static int aEntero(String valor) {
        try {
            return Integer.parseInt(limpiar(valor));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Asignacion obtenerAsignacion(ResultSet rs) throws SQLException {

        Hogar hogar = new Hogar(
                aEntero(rs.getString("ID_HOGAR")),
                limpiar(rs.getString("NOMBRE_HOGAR")),
                limpiar(rs.getString("NUM_DIRECCION")),
                null);

        Encuestador encuestador = new Encuestador();
        encuestador.setIdEncuestador(limpiar(rs.getString("ID_ENCUESTADOR")));
        encuestador.setNombreCompleto(limpiar(rs.getString("NOMBRE_COMPLETO")));
        encuestador.setDocumentoIdentidad(limpiar(rs.getString("DOCUMENTO_IDENTIDAD")));
        encuestador.setTelefono(limpiar(rs.getString("TELEFONO")));
        encuestador.setCiudad(new Ciudad(
                aEntero(rs.getString("ID_CIUDAD")),
                limpiar(rs.getString("NOMBRE_CIUDAD")),
                new Departamento(
                        aEntero(rs.getString("ID_DEPARTAMENTO")),
                        limpiar(rs.getString("NOMBRE_DEPARTAMENTO")))));

        return new Asignacion(
                hogar,
                encuestador,
                rs.getTimestamp("FECHA_INICIO_ASIGNACION"),
                rs.getTimestamp("FECHA_FIN_ASIGNACION"));
    }

    // ==================== BUSCAR POR HOGAR ====================

    /** Asignación vigente del hogar, o null si no tiene encuestador. */
    public Asignacion buscarPorHogar(int idHogar) {

        String llamada = "{call PR_BUSCAR_ASIGNACION_HOGAR(?,?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                System.err.println("No se pudo conectar a la base de datos.");
                return null;
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, String.valueOf(idHogar));
                cs.registerOutParameter(2, Types.REF_CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                    if (rs != null && rs.next()) {
                        return obtenerAsignacion(rs);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar la asignación: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // ==================== LISTAR POR ENCUESTADOR ====================

    public List<Asignacion> listarPorEncuestador(String idEncuestador) {

        List<Asignacion> asignaciones = new ArrayList<>();

        if (idEncuestador == null || idEncuestador.trim().isEmpty()) {
            return asignaciones;
        }

        String llamada = "{call PR_LISTAR_ASIGNACIONES_ENC(?,?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                System.err.println("No se pudo conectar a la base de datos.");
                return asignaciones;
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, idEncuestador.trim());
                cs.registerOutParameter(2, Types.REF_CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                    while (rs != null && rs.next()) {
                        asignaciones.add(obtenerAsignacion(rs));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar asignaciones: " + e.getMessage());
            e.printStackTrace();
        }

        return asignaciones;
    }
}