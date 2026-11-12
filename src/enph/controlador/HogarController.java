/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.modelo.Hogar;
import enph.modelo.HogarDAO;
import enph.modelo.Ciudad;
import enph.modelo.CiudadDAO;
import enph.modelo.Barrio;
import enph.modelo.BarrioDAO;
import enph.modelo.CabezaHogar;
import enph.modelo.CabezaHogarDAO;
import enph.modelo.EncuestaDAO;
import java.sql.SQLException;
import java.util.List;

public class HogarController {

    private final HogarDAO hogarDAO;
    private final CiudadDAO ciudadDAO;
    private final BarrioDAO barrioDAO;
    private final CabezaHogarDAO cabezaDAO;
    private final EncuestaDAO encuestaDAO;

    public HogarController() {
        this.hogarDAO = new HogarDAO();
        this.ciudadDAO = new CiudadDAO();
        this.barrioDAO = new BarrioDAO();
        this.cabezaDAO = new CabezaHogarDAO();
        this.encuestaDAO = new EncuestaDAO();
    }

    // ==================== CONSULTAS ====================

    public List<Hogar> listarHogares() {
        return hogarDAO.listarHogaresConDetalle();
    }

    public Hogar buscarHogar(String idHogar) {
        if (idHogar == null || idHogar.trim().isEmpty()) {
            return null;
        }

        return hogarDAO.buscarPorId(idHogar.trim());
    }

    public List<Ciudad> listarCiudades() {
        return ciudadDAO.listarCiudades();
    }

    public List<Barrio> listarBarriosPorCiudad(String idCiudad) throws AppException {

        if (idCiudad == null || idCiudad.trim().isEmpty()) {
            throw new AppException("Debe seleccionar una ciudad.");
        }

        try {
            return barrioDAO.listarPorCiudad(idCiudad.trim());
        } catch (SQLException e) {
            throw new AppException("No fue posible cargar los barrios de la ciudad.", e);
        }
    }

    // ==================== CREACIÓN ====================

    public boolean crearHogarConCabeza(
            Hogar hogar,
            CabezaHogar cabeza) {

        if (hogar == null || cabeza == null
                || hogar.getBarrio() == null) {
            return false;
        }

        return hogarDAO.insertarHogarConCabeza(hogar, cabeza);
    }

    // ==================== ACTUALIZACIÓN ====================

    public boolean actualizarHogar(Hogar hogar) throws AppException {

        if (hogar == null) {
            throw new AppException("No se recibió la información del hogar.");
        }

        if (hogar.getIdHogar() <= 0) {
            throw new AppException("El identificador del hogar no es válido.");
        }

        if (hogar.getBarrio() == null) {
            throw new AppException("Debe seleccionar un barrio.");
        }

        try {
            return hogarDAO.actualizarHogar(hogar);
        } catch (SQLException e) {
            throw new AppException("No fue posible actualizar el hogar.", e);
        }
    }
    // ==================== ELIMINACIÓN ====================

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

    public boolean eliminarHogar(String idHogar) throws AppException {

        if (idHogar == null || idHogar.trim().isEmpty()) {
            return false;
        }

        int id;

        try {
            id = Integer.parseInt(idHogar.trim());
        } catch (NumberFormatException e) {
            return false;
        }

        // No permitir eliminar hogares que tienen encuestas.
        if (hogarTieneEncuesta(id)) {
            return false;
        }

        return hogarDAO.eliminarHogar(idHogar.trim());
    }

    // ==================== CABEZA DE HOGAR ====================

    public boolean existeDocumentoCabeza(
            String documento,
            String idExcluido) {

        if (documento == null || documento.trim().isEmpty()) {
            return false;
        }

        return cabezaDAO.existeDocumento(
                documento.trim(),
                idExcluido
        );
    }
}
