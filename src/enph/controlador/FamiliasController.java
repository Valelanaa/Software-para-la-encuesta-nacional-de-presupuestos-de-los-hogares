/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package enph.controlador;

import enph.modelo.Hogar;
import enph.modelo.HogarDAO;
import enph.modelo.EncuestaDAO;
import enph.vista.FamiliasRegistradas;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class FamiliasController {

    private final FamiliasRegistradas vista;
    private final HogarDAO hogarDAO;
    private final EncuestaDAO encuestaDAO;

    public FamiliasController(FamiliasRegistradas vista) {
        this.vista = vista;
        this.hogarDAO = new HogarDAO();
        this.encuestaDAO = new EncuestaDAO();
    }

    /**
     * Consulta los hogares registrados y los muestra en la tabla.
     */
    public void cargarHogares() {

        try {

            List<Hogar> hogares = hogarDAO.listarHogares();

            vista.mostrarHogares(hogares);

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Error al cargar las familias:\n" + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    /**
     * Comprueba si un hogar tiene una encuesta registrada.
     */
    public boolean hogarTieneEncuesta(int idHogar) {
        return encuestaDAO.hogarTieneEncuesta(idHogar);
    }

    /**
     * Elimina las encuestas y respuestas pertenecientes a un hogar.
     */
    public boolean eliminarEncuestasDelHogar(int idHogar) {
        return encuestaDAO.eliminarEncuestasDelHogar(idHogar);
    }
}