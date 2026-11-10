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

    // Listar todos los barrios
    public List<Barrio> listarBarrios() throws SQLException {
        return barrioDAO.listarBarrios();
    }

    // Listar ciudades para el combo
    public List<Ciudad> listarCiudades() {
        return ciudadDAO.listarCiudades();
    }

    // Crear un barrio
    public boolean crearBarrio(String nombre, String idCiudad)
            throws SQLException {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new SQLException("Debe ingresar el nombre del barrio.");
        }

        if (idCiudad == null || idCiudad.trim().isEmpty()) {
            throw new SQLException("Debe seleccionar una ciudad.");
        }

        return barrioDAO.crearBarrio(nombre.trim(), idCiudad.trim());
    }

    // Editar un barrio
    public boolean editarBarrio(String idBarrio, String nombre,
                                String idCiudad) throws SQLException {

        if (idBarrio == null || idBarrio.trim().isEmpty()) {
            throw new SQLException("No se recibió el identificador del barrio.");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new SQLException("Debe ingresar el nombre del barrio.");
        }

        if (idCiudad == null || idCiudad.trim().isEmpty()) {
            throw new SQLException("Debe seleccionar una ciudad.");
        }

        return barrioDAO.editarBarrio(
                idBarrio.trim(),
                nombre.trim(),
                idCiudad.trim()
        );
    }

    // Comprobar si el barrio tiene hogares asociados
    public boolean barrioEnUso(String idBarrio) throws SQLException {
        return barrioDAO.barrioEnUso(idBarrio);
    }

    // Eliminar un barrio
    public boolean eliminarBarrio(String idBarrio) throws SQLException {
        return barrioDAO.eliminarBarrio(idBarrio);
    }
}
