package enph.controlador;

import enph.modelo.Hogar;
import enph.vista.FamiliasAsignadas;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Controla la carga y asignación de hogares a encuestadores.
 */
public class AsignacionController {

    private final FamiliasAsignadas vista;

    public AsignacionController(FamiliasAsignadas vista) {
        this.vista = vista;
    }

    /**
     * Carga únicamente los hogares que no tienen encuestador asignado.
     */
    public void cargarHogares() {
        try {
            List<Hogar> hogares = enph.Main.listarHogaresSinEncuestador();

            vista.mostrarHogares(hogares);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                vista,
                "Error al cargar las familias:\n" + e.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Asigna los hogares seleccionados al encuestador indicado.
     */
    public void asignarHogares(
            String idUsuario,
            List<String> idsHogares) {

        // Validar que se haya seleccionado un encuestador.
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                vista,
                "No se ha seleccionado un encuestador.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Validar que haya hogares seleccionados.
        if (idsHogares == null || idsHogares.isEmpty()) {
            JOptionPane.showMessageDialog(
                vista,
                "Selecciona al menos una familia para asignar.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            // Ejecutar la asignación en la base de datos.
            enph.Main.asignarHogaresAEncuestador(idUsuario, idsHogares);

            // El método DAO es void, por eso usamos el tamaño de la lista.
            int cantidad = idsHogares.size();

            JOptionPane.showMessageDialog(
                vista,
                "Se asignaron correctamente " + cantidad
                    + " familia(s) al encuestador.",
                "Asignación exitosa",
                JOptionPane.INFORMATION_MESSAGE
            );

            // Recargar la tabla: los hogares asignados ya no aparecerán.
            cargarHogares();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                vista,
                "No se pudieron asignar las familias:\n"
                    + e.getMessage(),
                "Error al asignar",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
