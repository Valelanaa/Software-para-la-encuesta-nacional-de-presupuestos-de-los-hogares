/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package enph.controlador;

import enph.conexion.HogarDAO;
import enph.conexion.Hogar;
import enph.vista.FamiliasRegistradas;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Controlador de la pantalla FamiliasRegistradas (patrón MVC).
 * Hace de intermediario: pide los datos a HogarDAO (modelo) y se los
 * entrega a FamiliasRegistradas (vista), para que la ventana no tenga
 * que hablar directamente con la base de datos.
 */

public class FamiliasController {

    private final FamiliasRegistradas vista; // ventana que mostrará las familias
    private final HogarDAO hogarDAO; // acceso a la tabla HOGAR
    //la ventana FamiliasRegistradas que este controlador atiende
    public FamiliasController(FamiliasRegistradas vista) {
        this.vista = vista;
        this.hogarDAO = new HogarDAO();
    }
    /**
     * Lee los hogares de la base de datos y los muestra en la tabla de la vista.
     * Si la base de datos falla (sin conexión, columna mal escrita, etc.),
     * muestra el error en un cuadro de diálogo en lugar de cerrar el programa.
     */
    public void cargarHogares() {

        try {
            // Consulta a la base de datos
            List<Hogar> hogares = hogarDAO.listarHogares();
        // La vista se encarga de llenar la tabla con esa lista
            vista.mostrarHogares(hogares);

        } catch (SQLException e) {
            // Aviso para el usuario, con el mensaje de Oracle (ej. ORA-00904)
            JOptionPane.showMessageDialog(
                vista,
                "Error al cargar las familias desde Oracle:\n"
                    + e.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE
            );
            // El detalle completo queda en la consola para depurar
            e.printStackTrace();
        }
    }
}