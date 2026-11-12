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

import java.sql.SQLException;
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
    public SesionEncuesta iniciar(int idHogar) throws AppException {

        validarIdHogar(idHogar);

        try {
            List<GrupoAlimento> grupos = catalogoDAO.listarGrupos();
            List<FrecuenciaCompra> frecuencias =
                    catalogoDAO.listarFrecuencias();

            validarCatalogos(grupos, frecuencias);

            int idEncuesta = encuestaDAO.crearEncuesta(idHogar);

            if (idEncuesta <= 0) {
                throw new AppException(
                        "No fue posible crear la encuesta para este hogar."
                );
            }

            return new SesionEncuesta(
                    idHogar,
                    idEncuesta,
                    grupos,
                    frecuencias,
                    false
            );

        } catch (SQLException e) {
            throw new AppException(
                    "Ocurrió un error al iniciar la encuesta.", e
            );
        }
    }

    /**
     * Abre una encuesta existente desde el primer grupo.
     */
    public SesionEncuesta editar(int idHogar) throws AppException {

        validarIdHogar(idHogar);

        try {
            int idEncuesta = encuestaDAO.obtenerIdEncuesta(idHogar);

            if (idEncuesta <= 0) {
                throw new AppException(
                        "No se encontró una encuesta para este hogar."
                );
            }

            List<GrupoAlimento> grupos = catalogoDAO.listarGrupos();
            List<FrecuenciaCompra> frecuencias =
                    catalogoDAO.listarFrecuencias();

            validarCatalogos(grupos, frecuencias);

            return new SesionEncuesta(
                    idHogar,
                    idEncuesta,
                    grupos,
                    frecuencias,
                    true
            );

        } catch (SQLException e) {
            throw new AppException(
                    "Ocurrió un error al abrir la encuesta para editarla.", e
            );
        }
    }

    /**
     * Abre únicamente los grupos que todavía no tienen respuesta.
     */
    public SesionEncuesta completar(int idHogar) throws AppException {

        validarIdHogar(idHogar);

        try {
            int idEncuesta = encuestaDAO.obtenerIdEncuesta(idHogar);

            if (idEncuesta <= 0) {
                throw new AppException(
                        "No se encontró una encuesta para este hogar."
                );
            }

            List<GrupoAlimento> todos = catalogoDAO.listarGrupos();
            List<FrecuenciaCompra> frecuencias =
                    catalogoDAO.listarFrecuencias();

            validarCatalogos(todos, frecuencias);

            Set<Integer> respondidos =
                    caracterizacionDAO.listarGruposRespondidos(idEncuesta);

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

        } catch (SQLException e) {
            throw new AppException(
                    "Ocurrió un error al recuperar los grupos pendientes.", e
            );
        }
    }

    private void validarIdHogar(int idHogar) throws AppException {

        if (idHogar <= 0) {
            throw new AppException(
                    "Debe seleccionar un hogar válido."
            );
        }
    }

    private void validarCatalogos(
            List<GrupoAlimento> grupos,
            List<FrecuenciaCompra> frecuencias) throws AppException {

        if (grupos == null || grupos.isEmpty()) {
            throw new AppException(
                    "No hay grupos de alimentos registrados."
            );
        }

        if (frecuencias == null || frecuencias.isEmpty()) {
            throw new AppException(
                    "No hay frecuencias de compra registradas."
            );
        }
    }
}
