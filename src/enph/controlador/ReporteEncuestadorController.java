/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.modelo.Encuestador;
import enph.modelo.ReporteBarrio;
import enph.modelo.ReporteEncuestadorDAO;
import enph.modelo.SesionUsuario;
import enph.modelo.Usuario;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReporteEncuestadorController {

    private final ReporteEncuestadorDAO reporteDAO;

    public ReporteEncuestadorController() {
        this.reporteDAO = new ReporteEncuestadorDAO();
    }

    public List<ReporteBarrio> generarReporte(
            LocalDate desde, LocalDate hasta) throws AppException {

        Usuario usuario = SesionUsuario.getActual();

        if (!(usuario instanceof Encuestador)) {
            throw new AppException("No se encontró el encuestador de la sesión.");
        }

        String idEncuestador = ((Encuestador) usuario).getIdEncuestador();

        if (idEncuestador == null || idEncuestador.trim().isEmpty()) {
            throw new AppException("No se encontró el encuestador de la sesión.");
        }

        if (desde == null || hasta == null) {
            throw new AppException(
                    "Debe ingresar las dos fechas del reporte."
            );
        }

        if (desde.isAfter(hasta)) {
            throw new AppException(
                    "La fecha inicial no puede ser posterior a la fecha final."
            );
        }

        try {
            return reporteDAO.generarReporte(idEncuestador, desde, hasta);

        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible generar el reporte de encuestas.",
                    e
            );
        }
    }
}
