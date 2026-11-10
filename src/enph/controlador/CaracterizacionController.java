/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.modelo.CaracterizacionCompra;
import enph.modelo.CaracterizacionDAO;
import enph.modelo.Encuesta;
import enph.modelo.EncuestaDAO;
import enph.modelo.FrecuenciaCompra;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Respuestas (caracterización de compra) de la encuesta en curso.
 * Lo usan FrecuenciaGasto, PrimeraCondicion y RespuestasEncuesta.
 */
public class CaracterizacionController {

    private final CaracterizacionDAO caracterizacionDAO;
    private final EncuestaDAO encuestaDAO;

    public CaracterizacionController() {
        this.caracterizacionDAO = new CaracterizacionDAO();
        this.encuestaDAO = new EncuestaDAO();
    }

    // ==================== CONSULTAS ====================

    /** Respuesta ya guardada del grupo actual de la sesión; null si todavía no se respondió. */
    public CaracterizacionCompra obtenerPrevia(SesionEncuesta sesion) {
        Encuesta encuesta = encuestaDAO.buscarPorId(sesion.getIdEncuesta());

        return caracterizacionDAO.obtener(encuesta, sesion.getGrupo(), sesion.getFrecuencias());
    }

    public List<String[]> listarRespuestasPorHogar(String idHogar) throws SQLException {
        return caracterizacionDAO.listarRespuestasPorHogar(idHogar);
    }

    public Set<Integer> listarGruposRespondidos(int idEncuesta) {
        return caracterizacionDAO.listarGruposRespondidos(idEncuesta);
    }

    // ==================== GUARDAR ====================

    /**
     * Guarda la respuesta del grupo actual de la sesión.
     * valor e intencion pueden ser null cuando la frecuencia no los pide.
     */
    public boolean guardar(SesionEncuesta sesion, FrecuenciaCompra frecuencia,
                           BigDecimal valor, Boolean intencion) {

        Encuesta encuesta = encuestaDAO.buscarPorId(sesion.getIdEncuesta());

        return caracterizacionDAO.guardar(
                new CaracterizacionCompra(encuesta, sesion.getGrupo(), frecuencia, valor, intencion));
    }
}
