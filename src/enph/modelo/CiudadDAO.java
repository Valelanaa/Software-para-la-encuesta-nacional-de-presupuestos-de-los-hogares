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
 * Acceso a datos de CIUDAD y DEPARTAMENTO mediante procedimientos
 * almacenados (PR_LISTAR_DEPARTAMENTOS, PR_LISTAR_CIUDADES_DETALLE y
 * PR_BUSCAR_CIUDAD).
 */
public class CiudadDAO {

    private static String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    /** Los ID de la base son VARCHAR2 con números; 0 si no se puede convertir. */
    private static int aEntero(String valor) {
        try {
            return Integer.parseInt(limpiar(valor));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Ciudad obtenerCiudad(ResultSet rs) throws SQLException {
        Departamento departamento = new Departamento(
                aEntero(rs.getString("ID_DEPARTAMENTO")),
                limpiar(rs.getString("NOMBRE_DEPARTAMENTO")));

        return new Ciudad(
                aEntero(rs.getString("ID_CIUDAD")),
                limpiar(rs.getString("NOMBRE_CIUDAD")),
                departamento);
    }

    // ==================== LISTAR DEPARTAMENTOS ====================

    public List<Departamento> listarDepartamentos() {

        String llamada = "{call PR_LISTAR_DEPARTAMENTOS(?)}";
        List<Departamento> departamentos = new ArrayList<>();

        try (Connection con = Conexion.init()) {

            if (con == null) {
                System.err.println("No se pudo conectar a la base de datos.");
                return departamentos;
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.registerOutParameter(1, Types.REF_CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                    while (rs != null && rs.next()) {
                        departamentos.add(new Departamento(
                                aEntero(rs.getString("ID_DEPARTAMENTO")),
                                limpiar(rs.getString("NOMBRE_DEPARTAMENTO"))));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar departamentos: " + e.getMessage());
            e.printStackTrace();
        }

        return departamentos;
    }

    // ==================== LISTAR CIUDADES ====================

    public List<Ciudad> listarCiudades() {

        String llamada = "{call PR_LISTAR_CIUDADES_DETALLE(?)}";
        List<Ciudad> ciudades = new ArrayList<>();

        try (Connection con = Conexion.init()) {

            if (con == null) {
                System.err.println("No se pudo conectar a la base de datos.");
                return ciudades;
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.registerOutParameter(1, Types.REF_CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                    while (rs != null && rs.next()) {
                        ciudades.add(obtenerCiudad(rs));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar ciudades: " + e.getMessage());
            e.printStackTrace();
        }

        return ciudades;
    }

    // ==================== BUSCAR CIUDAD POR ID ====================

    /** Devuelve la ciudad con su departamento, o null si no existe. */
    public Ciudad buscarPorId(String idCiudad) {

        if (idCiudad == null || idCiudad.trim().isEmpty()) {
            return null;
        }

        String llamada = "{call PR_BUSCAR_CIUDAD(?,?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                System.err.println("No se pudo conectar a la base de datos.");
                return null;
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, idCiudad.trim());
                cs.registerOutParameter(2, Types.REF_CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                    if (rs != null && rs.next()) {
                        return obtenerCiudad(rs);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar la ciudad: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}