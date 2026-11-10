/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

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

    // Generar reporte de asignaciones del encuestador autenticado
    public List<ReporteBarrio> generarReporte(
            LocalDate desde, LocalDate hasta) throws SQLException {

        Usuario usuario = SesionUsuario.getActual();

        if (usuario == null
                || usuario.getIdEncuestador() == null
                || usuario.getIdEncuestador().trim().isEmpty()) {

            throw new SQLException(
                    "No se encontró el encuestador de la sesión."
            );
        }

        if (desde == null || hasta == null) {
            throw new SQLException(
                    "Debe ingresar las dos fechas del reporte."
            );
        }

        if (desde.isAfter(hasta)) {
            throw new SQLException(
                    "La fecha inicial no puede ser posterior a la fecha final."
            );
        }

        return reporteDAO.generarReporte(
                usuario.getIdEncuestador(),
                desde,
                hasta
        );
    }
}