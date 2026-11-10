/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */
import enph.modelo.CatalogoDAO;
import enph.modelo.FrecuenciaCompra;

import java.util.List;

public class FrecuenciaController {

    private final CatalogoDAO catalogoDAO;

    public FrecuenciaController() {
        this.catalogoDAO = new CatalogoDAO();
    }

    // Consultar frecuencias de compra
    public List<FrecuenciaCompra> listarFrecuencias() {
        return catalogoDAO.listarFrecuencias();
    }

    // Crear una frecuencia
    public boolean crearFrecuencia(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        return catalogoDAO.crearFrecuencia(nombre.trim());
    }

    // Editar una frecuencia
    public boolean editarFrecuencia(int idFrecuencia, String nombre) {
        if (idFrecuencia <= 0 || nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        return catalogoDAO.editarFrecuencia(idFrecuencia, nombre.trim());
    }

    // Comprobar si una frecuencia está siendo utilizada
    public boolean frecuenciaEnUso(int idFrecuencia) {
        return catalogoDAO.frecuenciaEnUso(idFrecuencia);
    }

    // Eliminar una frecuencia
    public boolean eliminarFrecuencia(int idFrecuencia) {
        if (idFrecuencia <= 0) {
            return false;
        }

        return catalogoDAO.eliminarFrecuencia(idFrecuencia);
    }
}
