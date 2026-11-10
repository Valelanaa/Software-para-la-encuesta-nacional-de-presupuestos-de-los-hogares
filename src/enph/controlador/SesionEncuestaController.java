/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.modelo.CaracterizacionDAO;
import enph.modelo.CatalogoDAO;
import enph.modelo.EncuestaDAO;
import enph.modelo.FrecuenciaCompra;
import enph.modelo.GrupoAlimento;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Coordina la creación y recuperación de sesiones de encuesta.
 */
public class SesionEncuestaController {

    private final EncuestaDAO encuestaDAO;
    private final CatalogoDAO catalogoDAO;
    private final CaracterizacionDAO caracterizacionDAO;

    public SesionEncuestaController() {
        this.encuestaDAO = new EncuestaDAO();
        this.catalogoDAO = new CatalogoDAO();
        this.caracterizacionDAO = new CaracterizacionDAO();
    }

    /**
     * Inicia una encuesta nueva para un hogar.
     */
    public SesionEncuesta iniciar(int idHogar) {

        if (idHogar <= 0) {
            return null;
        }

        List<GrupoAlimento> grupos = catalogoDAO.listarGrupos();
        List<FrecuenciaCompra> frecuencias =
                catalogoDAO.listarFrecuencias();

        if (grupos.isEmpty() || frecuencias.isEmpty()) {
            return null;
        }

        int idEncuesta = encuestaDAO.crearEncuesta(idHogar);

        if (idEncuesta <= 0) {
            return null;
        }

        return new SesionEncuesta(
                idHogar,
                idEncuesta,
                grupos,
                frecuencias,
                false
        );
    }

    /**
     * Abre una encuesta existente desde el primer grupo.
     */
    public SesionEncuesta editar(int idHogar) {

        if (idHogar <= 0) {
            return null;
        }

        int idEncuesta = encuestaDAO.obtenerIdEncuesta(idHogar);

        if (idEncuesta <= 0) {
            return null;
        }

        List<GrupoAlimento> grupos = catalogoDAO.listarGrupos();
        List<FrecuenciaCompra> frecuencias =
                catalogoDAO.listarFrecuencias();

        if (grupos.isEmpty() || frecuencias.isEmpty()) {
            return null;
        }

        return new SesionEncuesta(
                idHogar,
                idEncuesta,
                grupos,
                frecuencias,
                true
        );
    }

    /**
     * Abre únicamente los grupos que todavía no tienen respuesta.
     */
    public SesionEncuesta completar(int idHogar) {

        if (idHogar <= 0) {
            return null;
        }

        int idEncuesta = encuestaDAO.obtenerIdEncuesta(idHogar);

        if (idEncuesta <= 0) {
            return null;
        }

        List<GrupoAlimento> todos = catalogoDAO.listarGrupos();
        List<FrecuenciaCompra> frecuencias =
                catalogoDAO.listarFrecuencias();

        if (todos.isEmpty() || frecuencias.isEmpty()) {
            return null;
        }

        Set<Integer> respondidos =
                caracterizacionDAO.listarGruposRespondidos(idEncuesta);

        if (respondidos == null) {
            return null;
        }

        List<GrupoAlimento> faltantes = new ArrayList<>();

        for (GrupoAlimento grupo : todos) {
            if (!respondidos.contains(grupo.getId())) {
                faltantes.add(grupo);
            }
        }

        // Si todos tienen respuesta, se permite revisar la encuesta completa.
        if (faltantes.isEmpty()) {
            return new SesionEncuesta(
                    idHogar,
                    idEncuesta,
                    todos,
                    frecuencias,
                    true
            );
        }

        return new SesionEncuesta(
                idHogar,
                idEncuesta,
                faltantes,
                frecuencias,
                false
        );
    }
}
