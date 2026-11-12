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

public class CaracterizacionController {

    private final CaracterizacionDAO caracterizacionDAO;
    private final EncuestaDAO encuestaDAO;

    public CaracterizacionController() {
        this.caracterizacionDAO = new CaracterizacionDAO();
        this.encuestaDAO = new EncuestaDAO();
    }

    public CaracterizacionCompra obtenerPrevia(SesionEncuesta sesion)
            throws AppException {

        validarSesion(sesion);

        try {
            Encuesta encuesta =
                    encuestaDAO.buscarPorId(sesion.getIdEncuesta());

            if (encuesta == null) {
                throw new AppException(
                        "No se encontró la encuesta asociada a la sesión."
                );
            }

            return caracterizacionDAO.obtener(
                    encuesta,
                    sesion.getGrupo(),
                    sesion.getFrecuencias()
            );

        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible consultar la respuesta guardada.", e
            );
        }
    }

    public List<String[]> listarRespuestasPorHogar(String idHogar)
            throws AppException {

        if (idHogar == null || idHogar.trim().isEmpty()) {
            throw new AppException("Debe seleccionar un hogar.");
        }

        try {
            return caracterizacionDAO.listarRespuestasPorHogar(idHogar);
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible consultar las respuestas del hogar.", e
            );
        }
    }

    public Set<Integer> listarGruposRespondidos(int idEncuesta)
            throws AppException {

        if (idEncuesta <= 0) {
            throw new AppException("El identificador de la encuesta no es válido.");
        }

        try {
            return caracterizacionDAO.listarGruposRespondidos(idEncuesta);
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible consultar los grupos respondidos.", e
            );
        }
    }

    public boolean guardar(SesionEncuesta sesion,
                           FrecuenciaCompra frecuencia,
                           BigDecimal valor,
                           Boolean intencion) throws AppException {

        validarSesion(sesion);

        if (frecuencia == null || frecuencia.getIdFrecuencia() <= 0) {
            throw new AppException("Debe seleccionar una frecuencia válida.");
        }

        try {
            Encuesta encuesta =
                    encuestaDAO.buscarPorId(sesion.getIdEncuesta());

            if (encuesta == null) {
                throw new AppException(
                        "No se encontró la encuesta que se desea actualizar."
                );
            }

            CaracterizacionCompra caracterizacion =
                    new CaracterizacionCompra(
                            encuesta,
                            sesion.getGrupo(),
                            frecuencia,
                            valor,
                            intencion
                    );

            return caracterizacionDAO.guardar(caracterizacion);

        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible guardar la respuesta.", e
            );
        }
    }

    private void validarSesion(SesionEncuesta sesion)
            throws AppException {

        if (sesion == null
                || sesion.getIdEncuesta() <= 0
                || sesion.getGrupo() == null) {

            throw new AppException("La sesión de encuesta no es válida.");
        }
    }
}
