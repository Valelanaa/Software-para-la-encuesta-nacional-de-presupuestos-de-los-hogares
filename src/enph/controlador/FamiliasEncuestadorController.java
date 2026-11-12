/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

import enph.modelo.Hogar;
import enph.modelo.HogarDAO;
import java.sql.SQLException;
import java.util.List;

public class FamiliasEncuestadorController {

    private final HogarDAO hogarDAO;

    public FamiliasEncuestadorController() {
        this.hogarDAO = new HogarDAO();
    }

    public List<Hogar> cargarHogares(String idEncuestador)
            throws AppException {

        if (idEncuestador == null || idEncuestador.trim().isEmpty()) {
            throw new AppException("No se ha seleccionado un encuestador.");
        }

        try {
            return hogarDAO.listarHogaresPorEncuestador(
                    idEncuestador.trim()
            );
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible consultar las familias del encuestador.", e
            );
        }
    }

    public boolean desasignarHogar(String idEncuestador, String idHogar)
            throws AppException {

        if (idEncuestador == null || idEncuestador.trim().isEmpty()) {
            throw new AppException("No se ha seleccionado un encuestador.");
        }

        if (idHogar == null || idHogar.trim().isEmpty()) {
            throw new AppException("No se ha seleccionado un hogar.");
        }

        try {
            return hogarDAO.desasignarHogar(
                    idEncuestador.trim(),
                    idHogar.trim()
            );
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible desasignar la familia.", e
            );
        }
    }
}