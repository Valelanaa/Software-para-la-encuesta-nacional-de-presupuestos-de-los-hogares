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

    public String[] buscarDetalleEncuesta(String idHogar)
            throws SQLException {

        if (idHogar == null || idHogar.trim().isEmpty()) {
            return null;
        }

        return hogarDAO.buscarDetalleEncuesta(idHogar.trim());
    }
}
