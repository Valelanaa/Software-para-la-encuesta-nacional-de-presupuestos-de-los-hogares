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
import enph.modelo.SesionUsuario;
import enph.modelo.Usuario;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReporteEncuestadorController {

    public List<ReporteBarrio> generarReporte(LocalDate desde, LocalDate hasta)
            throws SQLException {

        Usuario usuario = SesionUsuario.getActual();

        if (usuario == null || usuario.getIdEncuestador() == null
                || usuario.getIdEncuestador().trim().isEmpty()) {
            throw new SQLException("No se encontró el encuestador de la sesión.");
        }

        if (desde == null || hasta == null || desde.isAfter(hasta)) {
            throw new SQLException("El rango de fechas no es válido.");
        }

        return enph.Main.generarReporteEncuestador(usuario.getIdEncuestador(), desde, hasta);
    }
}