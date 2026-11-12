/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */
import enph.modelo.ReporteRendimientoEncuestador;
import enph.modelo.ReporteRendimientoEncuestadorDAO;

import java.sql.SQLException;
import java.util.List;

public class ReporteRendimientoController {

    private final ReporteRendimientoEncuestadorDAO reporteDAO;

    public ReporteRendimientoController() {
        this.reporteDAO = new ReporteRendimientoEncuestadorDAO();
    }

    // Generar el reporte mensual de rendimiento
    public List<ReporteRendimientoEncuestador> generarReporte(int mes, int anio) throws AppException {
        if (mes < 1 || mes > 12) {
            throw new AppException("El mes debe estar entre 1 y 12.");
        }
        if (anio < 1900 || anio > 9999) {
            throw new AppException("El año debe estar entre 1900 y 9999.");
        }
        try {
            return reporteDAO.generarReporte(mes, anio);
        } catch (SQLException e) {
            throw new AppException("No fue posible generar el reporte de rendimiento.", e);
        }
    }
}
