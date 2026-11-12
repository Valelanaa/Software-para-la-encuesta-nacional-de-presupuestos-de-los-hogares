/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.modelo.Barrio;
import enph.modelo.BarrioDAO;
import enph.modelo.Ciudad;
import enph.modelo.CiudadDAO;
import java.sql.SQLException;
import java.util.List;

public class BarrioController {

    private final BarrioDAO barrioDAO;
    private final CiudadDAO ciudadDAO;

    public BarrioController() {
        this.barrioDAO = new BarrioDAO();
        this.ciudadDAO = new CiudadDAO();
    }

    public List<Barrio> listarBarrios() throws AppException {
        try {
            return barrioDAO.listarBarrios();
        } catch (SQLException e) {
            throw new AppException("No fue posible cargar los barrios.", e);
        }
    }

    public List<Ciudad> listarCiudades() {
        return ciudadDAO.listarCiudades();
    }

    public boolean crearBarrio(String nombre, String idCiudad)
            throws AppException {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new AppException("Debe ingresar el nombre del barrio.");
        }

        if (idCiudad == null || idCiudad.trim().isEmpty()) {
            throw new AppException("Debe seleccionar una ciudad.");
        }

        try {
            return barrioDAO.crearBarrio(nombre.trim(), idCiudad.trim());
        } catch (SQLException e) {
            throw new AppException("No fue posible crear el barrio.", e);
        }
    }

    public boolean editarBarrio(
            String idBarrio,
            String nombre,
            String idCiudad) throws AppException {

        if (idBarrio == null || idBarrio.trim().isEmpty()) {
            throw new AppException("No se recibió el identificador del barrio.");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new AppException("Debe ingresar el nombre del barrio.");
        }

        if (idCiudad == null || idCiudad.trim().isEmpty()) {
            throw new AppException("Debe seleccionar una ciudad.");
        }

        try {
            return barrioDAO.editarBarrio(
                    idBarrio.trim(),
                    nombre.trim(),
                    idCiudad.trim()
            );
        } catch (SQLException e) {
            throw new AppException("No fue posible actualizar el barrio.", e);
        }
    }

    public boolean barrioEnUso(String idBarrio) throws AppException {
        try {
            return barrioDAO.barrioEnUso(idBarrio);
        } catch (SQLException e) {
            throw new AppException("No fue posible comprobar si el barrio está en uso.", e);
        }
    }

    public boolean eliminarBarrio(String idBarrio) throws AppException {

        if (idBarrio == null || idBarrio.trim().isEmpty()) {
            throw new AppException("Debe seleccionar un barrio.");
        }

        try {
            return barrioDAO.eliminarBarrio(idBarrio.trim());
        } catch (SQLException e) {
            throw new AppException("No fue posible eliminar el barrio.", e);
        }
    }
}