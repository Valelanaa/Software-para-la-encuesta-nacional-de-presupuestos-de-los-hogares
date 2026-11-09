/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarrioDAO {

    private static String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    // ==================== LISTAR BARRIOS ====================

    public List<Barrio> listarBarrios() throws SQLException {
        return listar("{call PR_LISTAR_BARRIOS(?)}", null);
    }

    // ==================== LISTAR POR CIUDAD ====================

    public List<Barrio> listarPorCiudad(String idCiudad)
            throws SQLException {

        if (idCiudad == null || idCiudad.trim().isEmpty()) {
            throw new SQLException("Debe seleccionar una ciudad.");
        }

        return listar(
                "{call PR_LISTAR_BARRIOS_POR_CIUDAD(?,?)}",
                idCiudad.trim()
        );
    }

    // ==================== BUSCAR POR ID ====================

    public Barrio buscarPorId(String idBarrio) throws SQLException {

        if (idBarrio == null || idBarrio.trim().isEmpty()) {
            throw new SQLException("Debe proporcionar el ID del barrio.");
        }

        String llamada = "{call PR_BUSCAR_BARRIO(?,?)}";

        try (Connection con = Conexion.init();
             CallableStatement cs = con.prepareCall(llamada)) {

            cs.setString(1, idBarrio.trim());
            cs.registerOutParameter(2, Types.REF_CURSOR);
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                if (rs != null && rs.next()) {
                    return obtenerBarrio(rs,
                            new CiudadDAO().buscarPorId(
                                    limpiar(rs.getString("ID_CIUDAD"))));
                }
            }
        }

        return null;
    }

    // ==================== CREAR BARRIO ====================

    public boolean crearBarrio(String nombre, String idCiudad)
            throws SQLException {

        if (nombre == null || nombre.trim().isEmpty()
                || idCiudad == null || idCiudad.trim().isEmpty()) {
            throw new SQLException("El nombre y la ciudad son obligatorios.");
        }

        String llamada = "{call PR_CREAR_BARRIO(?,?)}";

        try (Connection con = Conexion.init();
             CallableStatement cs = con.prepareCall(llamada)) {

            cs.setString(1, nombre.trim());
            cs.setString(2, idCiudad.trim());

            cs.execute();
            return true;
        }
    }

    // ==================== EDITAR BARRIO ====================

    public boolean editarBarrio(String idBarrio, String nombre,
                                String idCiudad) throws SQLException {

        if (idBarrio == null || idBarrio.trim().isEmpty()
                || nombre == null || nombre.trim().isEmpty()
                || idCiudad == null || idCiudad.trim().isEmpty()) {
            throw new SQLException("Todos los datos son obligatorios.");
        }

        String llamada = "{call PR_EDITAR_BARRIO(?,?,?)}";

        try (Connection con = Conexion.init();
             CallableStatement cs = con.prepareCall(llamada)) {

            cs.setString(1, idBarrio.trim());
            cs.setString(2, nombre.trim());
            cs.setString(3, idCiudad.trim());

            cs.execute();
            return true;
        }
    }

    // ==================== COMPROBAR SI ESTÁ EN USO ====================

    public boolean barrioEnUso(String idBarrio) throws SQLException {

        String llamada = "{call PR_BARRIO_EN_USO(?,?)}";

        try (Connection con = Conexion.init();
             CallableStatement cs = con.prepareCall(llamada)) {

            cs.setString(1, idBarrio);
            cs.registerOutParameter(2, Types.INTEGER);

            cs.execute();

            return cs.getInt(2) > 0;
        }
    }

    // ==================== ELIMINAR BARRIO ====================

    public boolean eliminarBarrio(String idBarrio) throws SQLException {

        String llamada = "{call PR_ELIMINAR_BARRIO(?)}";

        try (Connection con = Conexion.init();
             CallableStatement cs = con.prepareCall(llamada)) {

            cs.setString(1, idBarrio);
            cs.execute();

            return true;
        }
    }

    // ==================== MÉTODO AUXILIAR ====================

    private List<Barrio> listar(String llamada, String idCiudad)
            throws SQLException {

        List<Barrio> barrios = new ArrayList<>();

        // Las ciudades (con su departamento) se cargan una sola vez por lista.
        Map<String, Ciudad> ciudades = new HashMap<>();
        for (Ciudad c : new CiudadDAO().listarCiudades()) {
            ciudades.put(c.getId(), c);
        }

        try (Connection con = Conexion.init();
             CallableStatement cs = con.prepareCall(llamada)) {

            if (idCiudad == null) {
                cs.registerOutParameter(1, Types.REF_CURSOR);
            } else {
                cs.setString(1, idCiudad);
                cs.registerOutParameter(2, Types.REF_CURSOR);
            }

            cs.execute();

            int indiceCursor = idCiudad == null ? 1 : 2;

            try (ResultSet rs = (ResultSet) cs.getObject(indiceCursor)) {

                while (rs != null && rs.next()) {
                    barrios.add(obtenerBarrio(rs,
                            ciudades.get(limpiar(rs.getString("ID_CIUDAD")))));
                }
            }
        }

        return barrios;
    }

    private Barrio obtenerBarrio(ResultSet rs, Ciudad ciudad) throws SQLException {

        return new Barrio(
                limpiar(rs.getString("ID_BARRIO")),
                limpiar(rs.getString("NOMBRE_BARRIO")),
                ciudad
        );
    }
}