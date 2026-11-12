package enph.controlador;

import enph.modelo.Hogar;
import enph.modelo.HogarDAO;
import java.sql.SQLException;
import java.util.List;

public class AsignacionController {

    private final HogarDAO hogarDAO;
    private final String idEncuestador;

    public AsignacionController(String idEncuestador) {
        this.idEncuestador = idEncuestador;
        this.hogarDAO = new HogarDAO();
    }

    public List<Hogar> cargarHogares() throws AppException {

        if (idEncuestador == null || idEncuestador.trim().isEmpty()) {
            throw new AppException("No se ha seleccionado un encuestador.");
        }

        try {
            return hogarDAO.listarHogaresDisponiblesParaEncuestador(
                    idEncuestador.trim()
            );
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible cargar los hogares disponibles.", e
            );
        }
    }

    public void asignarHogares(
            String idEncuestador,
            List<String> idsHogares) throws AppException {

        if (idEncuestador == null || idEncuestador.trim().isEmpty()) {
            throw new AppException("Debe seleccionar un encuestador.");
        }

        if (idsHogares == null || idsHogares.isEmpty()) {
            throw new AppException("Debe seleccionar al menos una familia.");
        }

        try {
            hogarDAO.asignarHogaresAEncuestador(
                    idEncuestador.trim(),
                    idsHogares
            );
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible asignar las familias.", e
            );
        }
    }
}