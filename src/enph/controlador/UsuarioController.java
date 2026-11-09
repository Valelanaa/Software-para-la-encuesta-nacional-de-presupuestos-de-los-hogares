/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.modelo.Usuario;
import enph.modelo.UsuarioDAO;
import enph.modelo.Ciudad;
import enph.modelo.CiudadDAO;
import enph.modelo.SesionUsuario;

import java.util.List;

public class UsuarioController {

    private final UsuarioDAO usuarioDAO;
    private final CiudadDAO ciudadDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
        this.ciudadDAO = new CiudadDAO();
    }

    // ==================== CONSULTAS ====================

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarUsuarios();
    }

    public List<Ciudad> listarCiudades() {
        return ciudadDAO.listarCiudades();
    }

    public Usuario buscarUsuario(String idUsuario) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            return null;
        }

        return usuarioDAO.buscarPorId(idUsuario);
    }

    public boolean existeDocumento(String documento) {
        if (documento == null || documento.trim().isEmpty()) {
            return false;
        }

        return usuarioDAO.existeDocumento(documento.trim());
    }

    public boolean existeDocumento(String documento, String idExcluido) {
        if (documento == null || documento.trim().isEmpty()) {
            return false;
        }

        return usuarioDAO.existeDocumento(
                documento.trim(),
                idExcluido
        );
    }

    // ==================== REGISTRO ====================

    public boolean crearUsuario(Usuario usuario) {

        Usuario sesion = SesionUsuario.getActual();

        if (sesion == null
                || !"ADMINISTRADOR".equalsIgnoreCase(sesion.getRol())) {
            return false;
        }

        if (usuario == null
                || usuario.getCiudad() == null) {
            return false;
        }

        return usuarioDAO.insertarUsuario(usuario);
    }

    // ==================== ACTUALIZACIÓN ====================

    public boolean actualizarUsuario(
            Usuario usuario,
            boolean administrador) {

        if (usuario == null) {
            return false;
        }

        if (administrador) {
            Usuario sesion = SesionUsuario.getActual();

            if (sesion == null
                    || !"ADMINISTRADOR".equalsIgnoreCase(sesion.getRol())) {
                return false;
            }

            return usuarioDAO.actualizarUsuario(usuario);
        }

        Usuario sesion = SesionUsuario.getActual();

        if (sesion == null
                || !sesion.getIdUsuario().equals(usuario.getIdUsuario())) {
            return false;
        }

        return usuarioDAO.actualizarMisDatos(usuario);
    }

    // ==================== ELIMINACIÓN ====================

    public boolean eliminarUsuario(String idUsuario) {

        Usuario sesion = SesionUsuario.getActual();

        if (sesion == null
                || !"ADMINISTRADOR".equalsIgnoreCase(sesion.getRol())) {
            return false;
        }

        if (idUsuario == null
                || idUsuario.trim().isEmpty()
                || idUsuario.equals(sesion.getIdUsuario())) {
            return false;
        }

        return usuarioDAO.eliminarUsuario(idUsuario);
    }
}

