/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.modelo.CabezaHogar;
import enph.modelo.CabezaHogarDAO;

public class CabezaHogarController {

    private final CabezaHogarDAO cabezaDAO;

    public CabezaHogarController() {
        this.cabezaDAO = new CabezaHogarDAO();
    }

    public CabezaHogar buscarPorHogar(String idHogar) {
        if (idHogar == null || idHogar.trim().isEmpty()) {
            return null;
        }

        return cabezaDAO.buscarPorHogar(idHogar);
    }

    public boolean existeDocumento(String documento, String idExcluido) {
        if (documento == null || documento.trim().isEmpty()) {
            return false;
        }

        return cabezaDAO.existeDocumento(documento, idExcluido);
    }

    public boolean actualizar(CabezaHogar cabeza) {
        if (cabeza == null
                || cabeza.getIdCabezaHogar() == null
                || cabeza.getIdCabezaHogar().trim().isEmpty()) {
            return false;
        }

        return cabezaDAO.actualizar(cabeza);
    }
}