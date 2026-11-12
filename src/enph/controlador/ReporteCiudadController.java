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

    public List<ReporteCiudadBarrio> generarReporteCiudad(String idCiudad) throws AppException {

        if (idCiudad == null || idCiudad.trim().isEmpty()) {
            throw new AppException("Debe seleccionar una ciudad.");
        }

        try {
            return reporteDAO.generarReporte(idCiudad.trim());
        } catch (SQLException e) {
            throw new AppException("No fue posible generar el reporte por ciudad.", e);
        }
    }

    public List<ReporteBarrioDetalle> generarDetalleBarrio(String idBarrio) throws AppException {

        if (idBarrio == null || idBarrio.trim().isEmpty()) {
            throw new AppException("No se recibió el identificador del barrio.");
        }

        try {
            return reporteDAO.generarDetalleBarrio(idBarrio.trim());
        } catch (SQLException e) {
            throw new AppException("No fue posible consultar el detalle del barrio.", e);
        }
    }
}
