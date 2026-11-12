/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.modelo.CatalogoDAO;
import enph.modelo.GrupoAlimento;
import java.sql.SQLException;
import java.util.List;

public class GrupoAlimentoController {

    private final CatalogoDAO catalogoDAO;

    public GrupoAlimentoController() {
        this.catalogoDAO = new CatalogoDAO();
    }

    // Listar grupos de alimentos
    public List<GrupoAlimento> listarGrupos() throws AppException {
        try {
            return catalogoDAO.listarGrupos();
        } catch (SQLException e) {
            throw new AppException(
                    "No fue posible consultar los grupos de alimentos.", e
            );
        }
    }

    // Crear un grupo
    public boolean crearGrupo(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        return catalogoDAO.crearGrupo(nombre.trim());
    }

    // Editar un grupo
    public boolean editarGrupo(int idGrupo, String nombre) {

        if (idGrupo <= 0 || nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        return catalogoDAO.editarGrupo(idGrupo, nombre.trim());
    }

    // Comprobar si el grupo está siendo utilizado
    public boolean grupoEnUso(int idGrupo) {
        return catalogoDAO.grupoEnUso(idGrupo);
    }

    // Eliminar un grupo
    public boolean eliminarGrupo(int idGrupo) {

        if (idGrupo <= 0) {
            return false;
        }

        return catalogoDAO.eliminarGrupo(idGrupo);
    }
}
