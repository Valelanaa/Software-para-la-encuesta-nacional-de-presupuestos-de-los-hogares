/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package enph.controlador;

import enph.modelo.Hogar;
import enph.modelo.HogarDAO;
import enph.vista.FamiliasEncuestador;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class FamiliasEncuestadorController {

    private final FamiliasEncuestador vista;
    private final HogarDAO hogarDAO;

    public FamiliasEncuestadorController(FamiliasEncuestador vista) {
        this.vista = vista;
        this.hogarDAO = new HogarDAO();
    }

    // Consultar los hogares asignados al encuestador.
    public void cargarHogares(String idEncuestador) {

        try {
            List<Hogar> hogares =
                    hogarDAO.listarHogaresPorEncuestador(idEncuestador);

            vista.mostrarHogares(hogares);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Error al consultar las familias del encuestador:\n"
                    + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Desasignar un hogar del encuestador.
    public void desasignarHogar(
            String idEncuestador,
            String idHogar) {

        try {
            boolean desasignado =
                    hogarDAO.desasignarHogar(idEncuestador, idHogar);

            if (desasignado) {
                JOptionPane.showMessageDialog(
                        vista,
                        "La familia se desasignó correctamente.\n"
                        + "El hogar continúa registrado en el sistema.",
                        "Desasignación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarHogares(idEncuestador);

            } else {
                JOptionPane.showMessageDialog(
                        vista,
                        "No se pudo desasignar la familia. "
                        + "Es posible que ya no esté asignada "
                        + "a este encuestador.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Error al desasignar la familia:\n" + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}