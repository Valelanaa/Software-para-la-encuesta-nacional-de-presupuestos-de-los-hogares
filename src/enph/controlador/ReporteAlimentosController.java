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

    // Reporte de alimentos esenciales
    public List<ReporteEsencial> generarReporteEsenciales(
            String frecuencia) throws SQLException {

        validarFrecuencia(frecuencia);

        return reporteDAO.generarReporte(frecuencia);
    }

    // Reporte de alimentos no esenciales
    public List<ReporteEsencial> generarReporteNoEsenciales(
            String frecuencia) throws SQLException {

        validarFrecuencia(frecuencia);

        return reporteDAO.generarReporteNoEsenciales(frecuencia);
    }

    // Reporte de alimentos por todas las frecuencias
    public List<ReporteEsencial> generarReporteTodasFrecuencias(
            String frecuencia) throws SQLException {

        validarFrecuencia(frecuencia);

        return reporteDAO.generarReporteTodasFrecuencias(frecuencia);
    }

    private void validarFrecuencia(String frecuencia)
            throws SQLException {

        if (frecuencia == null || frecuencia.trim().isEmpty()) {
            throw new SQLException(
                    "Debe seleccionar una frecuencia de compra."
            );
        }
    }
}
