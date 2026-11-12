/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

import enph.modelo.Hogar;
import enph.modelo.HogarDAO;
import enph.modelo.EncuestaDAO;
import java.sql.SQLException;
import java.util.List;

public class FamiliasController {

    private final HogarDAO hogarDAO;
    private final EncuestaDAO encuestaDAO;

    public FamiliasController() {
        this.hogarDAO = new HogarDAO();
        this.encuestaDAO = new EncuestaDAO();
    }

    public List<Hogar> cargarHogares() throws AppException {
        try {
            return hogarDAO.listarHogares();
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible cargar las familias registradas.", e
            );
        }
    }

    public boolean hogarTieneEncuesta(int idHogar) throws AppException {

        try {
            return encuestaDAO.hogarTieneEncuesta(idHogar);
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible comprobar si el hogar tiene una encuesta.",
                    e
            );
        }
    }

    public boolean eliminarEncuestasDelHogar(int idHogar) throws AppException {

        try {
            return encuestaDAO.eliminarEncuestasDelHogar(idHogar);
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible eliminar las encuestas del hogar.",
                    e
            );
        }
    }
}