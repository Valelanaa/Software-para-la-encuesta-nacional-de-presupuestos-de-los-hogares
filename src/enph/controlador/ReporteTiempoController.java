/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.modelo.ReporteTiempoDAO;
import enph.modelo.ReporteTiempoDTO;
import enph.vista.ReporteTiempo;

import java.sql.SQLException;
import java.util.List;

public class ReporteTiempoController {

    private final ReporteTiempo vista;
    private final ReporteTiempoDAO dao;

    public ReporteTiempoController(ReporteTiempo vista) {
        this.vista = vista;
        this.dao = new ReporteTiempoDAO();
    }

    public void cargarReporte() {

        try {

            List<ReporteTiempoDTO> lista = dao.obtenerReporte();

            vista.mostrarReporte(lista);

        } catch (SQLException e) {

            javax.swing.JOptionPane.showMessageDialog(
                    vista,
                    "Error al cargar el reporte de tiempos:\n" + e.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
