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
import java.util.List;

public class DetallesInformeController {

    private final HogarDAO hogarDAO;

    public DetallesInformeController() {
        this.hogarDAO = new HogarDAO();
    }

    // Consultar familias y estado de sus encuestas por encuestador
    public List<String[]> listarEstadoEncuestas(String idEncuestador) throws AppException {

        if (idEncuestador == null || idEncuestador.trim().isEmpty()) {
            throw new AppException("No se recibió el identificador del encuestador.");
        }

        try {
            return hogarDAO.listarEstadoEncuestasPorEncuestador(idEncuestador.trim());
        } catch (SQLException e) {
            throw new AppException("No fue posible consultar el estado de las encuestas.", e);
        }
    }
}
