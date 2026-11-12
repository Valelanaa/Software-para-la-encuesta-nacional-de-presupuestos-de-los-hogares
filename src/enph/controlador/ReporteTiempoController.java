/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */
    import enph.modelo.ReporteTiempoDAO;
    import enph.modelo.ReporteTiempoDTO;
    import java.sql.SQLException;
    import java.util.List;

    public class ReporteTiempoController {

        private final ReporteTiempoDAO dao;

        public ReporteTiempoController() {
            this.dao = new ReporteTiempoDAO();
        }

        public List<ReporteTiempoDTO> cargarReporte() throws AppException {
            try {
                return dao.obtenerReporte();
            } catch (SQLException e) {
                throw new AppException(
                        "No fue posible cargar el reporte de tiempos.", e
                );
            }
        }
    }