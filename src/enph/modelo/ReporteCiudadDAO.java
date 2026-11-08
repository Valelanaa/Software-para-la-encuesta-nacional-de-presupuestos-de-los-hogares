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

public class ReporteCiudadDAO {

    /** Barrios de la ciudad con su cantidad de encuestas realizadas. */
    public List<ReporteCiudadBarrio> generarReporte(String idCiudad)
            throws SQLException {

        List<ReporteCiudadBarrio> resultados = new ArrayList<>();
        String llamada = "{call PR_REPORTE_ENCUESTAS_CIUDAD(?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No se pudo establecer la conexión con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {
                cs.setString(1, idCiudad);
                cs.registerOutParameter(2, OracleTypes.CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                    while (rs != null && rs.next()) {
                        resultados.add(new ReporteCiudadBarrio(
                                rs.getString("ID_BARRIO"),
                                rs.getString("BARRIO"),
                                rs.getInt("CANTIDAD")
                        ));
                    }
                }
            }
        }

        return resultados;
    }

    /** Familias con encuesta realizada en el barrio y el encuestador de cada una. */
    public List<ReporteBarrioDetalle> generarDetalleBarrio(String idBarrio)
            throws SQLException {

        List<ReporteBarrioDetalle> resultados = new ArrayList<>();
        String llamada = "{call PR_REPORTE_ENCUESTAS_BARRIO(?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No se pudo establecer la conexión con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {
                cs.setString(1, idBarrio);
                cs.registerOutParameter(2, OracleTypes.CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                    while (rs != null && rs.next()) {
                        resultados.add(new ReporteBarrioDetalle(
                                rs.getString("FAMILIA"),
                                rs.getString("ENCUESTADOR")
                        ));
                    }
                }
            }
        }

        return resultados;
    }
}