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

    public List<Barrio> listarBarriosPorCiudad(String idCiudad)
            throws SQLException {

        if (idCiudad == null || idCiudad.trim().isEmpty()) {
            throw new SQLException("Debe seleccionar una ciudad.");
        }

        return barrioDAO.listarPorCiudad(idCiudad.trim());
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

    public boolean actualizarHogar(Hogar hogar)
            throws SQLException {

        if (hogar == null || hogar.getBarrio() == null
                || hogar.getIdHogar() <= 0) {
            return false;
        }

        return hogarDAO.actualizarHogar(hogar);
    }

    // ==================== ELIMINACIÓN ====================

    public boolean hogarTieneEncuesta(int idHogar) {
        return encuestaDAO.hogarTieneEncuesta(idHogar);
    }

    public boolean eliminarHogar(String idHogar) {

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
