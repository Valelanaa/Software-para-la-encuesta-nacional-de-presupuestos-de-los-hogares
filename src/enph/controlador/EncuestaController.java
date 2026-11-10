/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.modelo.EncuestaDAO;
import java.sql.SQLException;
import java.util.List;

public class EncuestaController {

    private final EncuestaDAO encuestaDAO;

    public EncuestaController() {
        this.encuestaDAO = new EncuestaDAO();
    }

    /** Registra la fecha de fin y quita la marca de incompleta. */
    public void finalizar(int idEncuesta) {
        encuestaDAO.finalizarEncuesta(idEncuesta);
        encuestaDAO.quitarMarcaIncompleta(idEncuesta);
    }

    /** Cierra la encuesta sin quitar la marca de incompleta. */
    public void cerrar(int idEncuesta) {
        encuestaDAO.eliminarEncuestaVacia(idEncuesta);
        encuestaDAO.finalizarEncuesta(idEncuesta);
    }

    /** Elimina la encuesta únicamente si no tiene respuestas. */
    public void eliminarSiVacia(int idEncuesta) {
        encuestaDAO.eliminarEncuestaVacia(idEncuesta);
    }

    /** Obtiene el identificador de la encuesta asociada a un hogar. */
    public int obtenerIdEncuesta(String idHogar) {
        if (idHogar == null || idHogar.trim().isEmpty()) {
            return -1;
        }

        try {
            int id = Integer.parseInt(idHogar.trim());
            return encuestaDAO.obtenerIdEncuesta(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** Marca una encuesta como incompleta según los permisos del usuario. */
    public boolean marcarIncompleta(int idEncuesta) {
        if (idEncuesta <= 0) {
            return false;
        }

        return encuestaDAO.marcarIncompleta(idEncuesta);
    }
}