/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.modelo.HogarDAO;
import java.sql.SQLException;

public class DetalleEncuestaController {

    private final HogarDAO hogarDAO;

    public DetalleEncuestaController() {
        this.hogarDAO = new HogarDAO();
    }

    public String[] buscarDetalleEncuesta(String idHogar) throws AppException {

        if (idHogar == null || idHogar.trim().isEmpty()) {
            return null;
        }

        try {
            return hogarDAO.buscarDetalleEncuesta(idHogar.trim());
        } catch (SQLException e) {
            throw new AppException("No fue posible consultar el detalle de la encuesta.", e);
        }
    }
}
