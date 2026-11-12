/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.modelo.CatalogoDAO;
import enph.modelo.FrecuenciaCompra;
import enph.modelo.ReporteEsencial;
import enph.modelo.ReporteEsencialesDAO;
import java.sql.SQLException;
import java.util.List;

public class ReporteAlimentosController {

    private final ReporteEsencialesDAO reporteDAO;
    private final CatalogoDAO catalogoDAO;

    public ReporteAlimentosController() {
        this.reporteDAO = new ReporteEsencialesDAO();
        this.catalogoDAO = new CatalogoDAO();
    }

    // Consultar las frecuencias registradas
    public List<FrecuenciaCompra> listarFrecuencias() throws AppException {
        try {
            return catalogoDAO.listarFrecuencias();
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible cargar las frecuencias del reporte.", e
            );
        }
    }

    public List<ReporteEsencial> generarReporteEsenciales(String frecuencia) throws AppException {
        validarFrecuencia(frecuencia);
        try {
            return reporteDAO.generarReporte(frecuencia);
        } catch (SQLException e) {
            throw new AppException("No fue posible generar el reporte de esenciales.", e);
        }
    }

    public List<ReporteEsencial> generarReporteNoEsenciales(String frecuencia) throws AppException {
        validarFrecuencia(frecuencia);
        try {
            return reporteDAO.generarReporteNoEsenciales(frecuencia);
        } catch (SQLException e) {
            throw new AppException("No fue posible generar el reporte de no esenciales.", e);
        }
    }

    public List<ReporteEsencial> generarReporteTodasFrecuencias(String frecuencia) throws AppException {
        validarFrecuencia(frecuencia);
        try {
            return reporteDAO.generarReporteTodasFrecuencias(frecuencia);
        } catch (SQLException e) {
            throw new AppException("No fue posible generar el reporte por frecuencias.", e);
        }
    }

    private void validarFrecuencia(String frecuencia) throws AppException {
        if (frecuencia == null || frecuencia.trim().isEmpty()) {
            throw new AppException("Debe seleccionar una frecuencia de compra.");
        }
    }
}
