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
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;

public class ReporteTiempoDetalleDAO {

    public List<ReporteTiempoDetalleDTO> obtenerDetalle(
            String idEncuestador) throws SQLException {

        List<ReporteTiempoDetalleDTO> lista = new ArrayList<>();

        String sql = "{call PR_REPORTE_TIEMPO_DETALLE(?, ?)}";

        Connection con = null;

        try {

            con = Conexion.init();

            if (con == null) {
                throw new SQLException(
                        "No fue posible establecer la conexión con Oracle."
                );
            }

            try (CallableStatement cs = con.prepareCall(sql)) {

                cs.setString(1, idEncuestador);

                cs.registerOutParameter(2, OracleTypes.CURSOR);

                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {

                    while (rs.next()) {

                        String idHogar = rs.getString("ID_HOGAR");

                        String nombreHogar = rs.getString("NOMBRE_HOGAR");

                        long tiempo = rs.getLong("TIEMPO_SEGUNDOS");

                        Long tiempoSegundos = rs.wasNull() ? null : tiempo;

                        ReporteTiempoDetalleDTO detalle =
                                new ReporteTiempoDetalleDTO(
                                        idHogar,
                                        nombreHogar,
                                        tiempoSegundos
                                );

                        lista.add(detalle);
                    }
                }
            }

        } finally {
            Conexion.close(con);
        }

        return lista;
    }
}
