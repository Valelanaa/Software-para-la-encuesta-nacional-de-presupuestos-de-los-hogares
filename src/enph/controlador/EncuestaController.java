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

public class EncuestaController {

    private final EncuestaDAO encuestaDAO;

    public EncuestaController() {
        this.encuestaDAO = new EncuestaDAO();
    }

    public void finalizar(int idEncuesta) throws AppException {
        validarId(idEncuesta);

        try {
            encuestaDAO.finalizarEncuesta(idEncuesta);
            encuestaDAO.quitarMarcaIncompleta(idEncuesta);
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible finalizar la encuesta.", e
            );
        }
    }

    public void cerrar(int idEncuesta) throws AppException {
        validarId(idEncuesta);

        try {
            boolean eliminada = encuestaDAO.eliminarEncuestaVacia(idEncuesta);

            // Si tenía respuestas, se conserva y se finaliza.
            if (!eliminada) {
                encuestaDAO.finalizarEncuesta(idEncuesta);
                encuestaDAO.quitarMarcaIncompleta(idEncuesta);
            }

        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible cerrar la encuesta.", e
            );
        }
    }

    public boolean eliminarSiVacia(int idEncuesta) throws AppException {
        validarId(idEncuesta);

        try {
            return encuestaDAO.eliminarEncuestaVacia(idEncuesta);
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible comprobar o eliminar la encuesta vacía.", e
            );
        }
    }

    public int obtenerIdEncuesta(String idHogar) throws AppException {

        if (idHogar == null || idHogar.trim().isEmpty()) {
            throw new AppException("Debe seleccionar un hogar.");
        }

        try {
            int id = Integer.parseInt(idHogar.trim());

            if (id <= 0) {
                throw new AppException(
                        "El identificador del hogar no es válido."
                );
            }

            return encuestaDAO.obtenerIdEncuesta(id);

        } catch (NumberFormatException e) {
            throw new AppException(
                    "El identificador del hogar no es válido.", e
            );
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible consultar la encuesta del hogar.", e
            );
        }
    }

    public boolean marcarIncompleta(int idEncuesta) throws AppException {
        validarId(idEncuesta);

        try {
            return encuestaDAO.marcarIncompleta(idEncuesta);
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible cambiar el estado de la encuesta.", e
            );
        }
    }

    private void validarId(int idEncuesta) throws AppException {
        if (idEncuesta <= 0) {
            throw new AppException(
                    "El identificador de la encuesta no es válido."
            );
        }
    }
}