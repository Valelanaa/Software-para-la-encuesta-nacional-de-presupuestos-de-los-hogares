/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.modelo.Ciudad;
import enph.modelo.CiudadDAO;
import enph.modelo.ReporteBarrioDetalle;
import enph.modelo.ReporteCiudadBarrio;
import enph.modelo.ReporteCiudadDAO;

import java.sql.SQLException;
import java.util.List;

public class ReporteCiudadController {

    private final CiudadDAO ciudadDAO;
    private final ReporteCiudadDAO reporteDAO;

    public ReporteCiudadController() {
        this.ciudadDAO = new CiudadDAO();
        this.reporteDAO = new ReporteCiudadDAO();
    }

    // Consultar ciudades disponibles
    public List<Ciudad> listarCiudades() {
        return ciudadDAO.listarCiudades();
    }

    // Generar reporte de encuestas por ciudad
    public List<ReporteCiudadBarrio> generarReporteCiudad(
            String idCiudad) throws SQLException {

        if (idCiudad == null || idCiudad.trim().isEmpty()) {
            throw new SQLException("Debe seleccionar una ciudad.");
        }

        return reporteDAO.generarReporte(idCiudad.trim());
    }

    // Consultar familias y encuestadores de un barrio
    public List<ReporteBarrioDetalle> generarDetalleBarrio(
            String idBarrio) throws SQLException {

        if (idBarrio == null || idBarrio.trim().isEmpty()) {
            throw new SQLException("No se recibió el identificador del barrio.");
        }

        return reporteDAO.generarDetalleBarrio(idBarrio.trim());
    }
}
