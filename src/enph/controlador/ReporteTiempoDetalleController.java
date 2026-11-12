/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */
    
import enph.modelo.ReporteTiempoDetalleDAO;
import enph.modelo.ReporteTiempoDetalleDTO;
import java.sql.SQLException;
import java.util.List;

public class ReporteTiempoDetalleController {

    private final ReporteTiempoDetalleDAO dao;

    public ReporteTiempoDetalleController() {
        this.dao = new ReporteTiempoDetalleDAO();
    }

    public List<ReporteTiempoDetalleDTO> cargarDetalle(String idEncuestador)
            throws AppException {

        if (idEncuestador == null || idEncuestador.trim().isEmpty()) {
            throw new AppException("No se recibió un encuestador válido.");
        }

        try {
            return dao.obtenerDetalle(idEncuestador.trim());
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible cargar el detalle de tiempos.", e
            );
        }
    }
}