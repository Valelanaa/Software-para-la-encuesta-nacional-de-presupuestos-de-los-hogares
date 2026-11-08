/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.modelo.ReporteTiempoDetalleDAO;
import enph.modelo.ReporteTiempoDetalleDTO;
import enph.vista.ReporteTiempoDetalles;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

public class ReporteTiempoDetalleController {

    private final ReporteTiempoDetalles vista;
    private final ReporteTiempoDetalleDAO dao;

    public ReporteTiempoDetalleController(ReporteTiempoDetalles vista) {

        this.vista = vista;
        this.dao = new ReporteTiempoDetalleDAO();
    }

    public void cargarDetalle(String idEncuestador) {

        try {

            List<ReporteTiempoDetalleDTO> lista =
                    dao.obtenerDetalle(idEncuestador);

            vista.mostrarDetalle(lista);

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Error al cargar el detalle de tiempos:\n"
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
