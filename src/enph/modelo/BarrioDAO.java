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
import java.util.List;

public class BarrioDAO {

    private static String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    // ==================== LISTAR BARRIOS ====================

    public List<Barrio> listarBarrios() throws SQLException {
        return listar("{call PR_LISTAR_BARRIOS(?)}", null);
    }

    // ==================== LISTAR POR CIUDAD ====================

    public List<Barrio> listarPorCiudad(String idCiudad) throws SQLException {
        if (idCiudad == null || idCiudad.trim().isEmpty()) {
            throw new SQLException("Debe seleccionar una ciudad.");
        }

        return listar("{call PR_LISTAR_BARRIOS_POR_CIUDAD(?,?)}", idCiudad.trim());
    }

    // ==================== BUSCAR POR ID ====================

    public Barrio buscarPorId(String idBarrio) throws SQLException {
        if (idBarrio == null || idBarrio.trim().isEmpty()) {
            throw new SQLException("Debe proporcionar el identificador del barrio.");
        }

        String llamada = "{call PR_BUSCAR_BARRIO(?,?)}";

        try (Connection con = Conexion.init()) {
            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {
                cs.setString(1, idBarrio.trim());
                cs.registerOutParameter(2, Types.REF_CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                    if (rs != null && rs.next()) {
                        return obtenerBarrio(rs);
                    }
                }
            }
        }

        return null;
    }

    // ==================== MÉTODO AUXILIAR ====================

    private List<Barrio> listar(String llamada, String idCiudad) throws SQLException {
        List<Barrio> barrios = new ArrayList<>();

        try (Connection con = Conexion.init()) {
            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {
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
                        barrios.add(obtenerBarrio(rs));
                    }
                }
            }
        }

        return barrios;
    }

    private Barrio obtenerBarrio(ResultSet rs) throws SQLException {
        return new Barrio(
                limpiar(rs.getString("ID_BARRIO")),
                limpiar(rs.getString("NOMBRE_BARRIO")),
                limpiar(rs.getString("ID_CIUDAD"))
        );
    }
}