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
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReporteEncuestadorDAO {

    public List<ReporteBarrio> generarReporte(
            String idEncuestador, LocalDate desde, LocalDate hasta)
            throws SQLException {

        List<ReporteBarrio> reporte = new ArrayList<>();
        String llamada = "{CALL PR_REPORTE_ASIGNACIONES_ENC(?,?,?,?)}";

        try (Connection con = Conexion.init()) {
            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {
                cs.setString(1, idEncuestador);
                cs.setDate(2, Date.valueOf(desde));
                cs.setDate(3, Date.valueOf(hasta));
                cs.registerOutParameter(4, Types.REF_CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(4)) {
                    while (rs.next()) {
                        reporte.add(new ReporteBarrio(
                            rs.getString("BARRIO"),
                            rs.getInt("ASIGNADAS"),
                            rs.getInt("FINALIZADAS"),
                            rs.getInt("PENDIENTES"),
                            rs.getInt("INCOMPLETAS")
                        ));
                    }
                }
            }
        }

        return reporte;
    }
}