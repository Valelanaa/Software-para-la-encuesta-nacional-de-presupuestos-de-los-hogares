
package enph.controlador;

import enph.modelo.Hogar;
import enph.modelo.HogarDAO;
import enph.vista.FamiliasAsignadas;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class AsignacionController {

    private final FamiliasAsignadas vista;
    private final String idEncuestador;
    private final HogarDAO hogarDAO;

    public AsignacionController(
            FamiliasAsignadas vista,
            String idEncuestador) {

        this.vista = vista;
        this.idEncuestador = idEncuestador;
        this.hogarDAO = new HogarDAO();
    }

    /**
     * Carga los hogares disponibles para el encuestador seleccionado.
     */
    public void cargarHogares() {

        if (idEncuestador == null
                || idEncuestador.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "No se ha seleccionado un encuestador.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            List<Hogar> hogares =
                    hogarDAO.listarHogaresDisponiblesParaEncuestador(
                            idEncuestador
                    );

            vista.mostrarHogares(hogares);

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    vista,
                    "No fue posible cargar los hogares disponibles:\n"
                    + e.getMessage(),
                    "Error de consulta",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    /**
     * Asigna uno o varios hogares al encuestador seleccionado.
     */
    public void asignarHogares(
            String idEncuestador,
            List<String> idsHogares) {

        if (idEncuestador == null
                || idEncuestador.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Debe seleccionar un encuestador.",
                    "Encuestador no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (idsHogares == null || idsHogares.isEmpty()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Debe seleccionar al menos una familia.",
                    "Familias no seleccionadas",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            hogarDAO.asignarHogaresAEncuestador(
                    idEncuestador,
                    idsHogares
            );

            JOptionPane.showMessageDialog(
                    vista,
                    "Las familias fueron asignadas correctamente.",
                    "Asignación exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Actualiza la tabla para retirar los hogares ya asignados.
            cargarHogares();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    vista,
                    "No fue posible asignar las familias:\n"
                    + e.getMessage(),
                    "Error de asignación",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
}
