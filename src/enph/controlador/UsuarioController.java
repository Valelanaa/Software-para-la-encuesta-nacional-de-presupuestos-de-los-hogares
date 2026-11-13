/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */

import enph.Validador;
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
    
    public void guardarEdicion(Usuario usuario, String nombre, String cedula, String telefono, String contrasenna, String rol, String idCiudad) throws AppException {

        if (usuario == null) {
            throw new AppException("No hay un usuario cargado para guardar.");
        }

        Usuario sesion = SesionUsuario.getActual();
        boolean esAdministrador = sesion != null
                && "ADMINISTRADOR".equalsIgnoreCase(sesion.getRol());
        boolean editaSusDatos = sesion != null
                && usuario.getIdUsuario().equals(sesion.getIdUsuario());

        nombre = nombre == null ? "" : nombre.trim();
        cedula = cedula == null ? "" : cedula.trim();
        telefono = telefono == null ? "" : telefono.trim();

        // Lo que no se muestra conserva el valor que ya tiene en la base de datos
        contrasenna = editaSusDatos
                ? (contrasenna == null ? "" : contrasenna.trim())
                : usuario.getContrasenna();
        if (editaSusDatos) {
            rol = usuario.getRol();
        }
        if (editaSusDatos && !esAdministrador) {
            idCiudad = usuario.getIdCiudad();
        }

        if (nombre.isEmpty() || cedula.isEmpty() || contrasenna.isEmpty()
                || telefono.isEmpty() || idCiudad == null || idCiudad.isEmpty()) {
            throw new AppException("Todos los campos son obligatorios.");
        }
        if (!Validador.nombreValido(nombre)) {
            throw new AppException("El nombre debe contener solamente letras, "
                    + "tildes y ñ, con un espacio entre palabras. Mínimo 3 caracteres.");
        }
        if (!Validador.documentoValido(cedula)) {
            throw new AppException("La cédula debe contener entre 6 y 10 dígitos.");
        }
        if (!Validador.telefonoValido(telefono)) {
            throw new AppException("El teléfono debe contener 7 dígitos (fijo) o 10 dígitos (celular).");
        }
        if (nombre.length() > 150 || cedula.length() > 20
                || telefono.length() > 20 || contrasenna.length() > 50) {
            throw new AppException("Algún campo es demasiado largo "
                    + "(nombre máx. 150, cédula y teléfono 20, contraseña 50).");
        }
        if (existeDocumento(cedula, usuario.getIdUsuario())) {
            throw new AppException("Ya existe otro usuario con esa cédula.");
        }

        usuario.setNombreCompleto(nombre);
        usuario.setDocumentoIdentidad(cedula);
        usuario.setTelefono(telefono);
        usuario.setContrasenna(contrasenna);
        usuario.setRol(rol);
        usuario.setCiudad(ciudadDAO.buscarPorId(idCiudad));

        if (!actualizarUsuario(usuario, esAdministrador)) {
            throw new AppException("No se pudo actualizar. Revise la consola para ver el detalle.");
        }

        // Si se editó el usuario de la sesión, se refresca el banner
        if (editaSusDatos) {
            sesion.setNombreCompleto(nombre);
            sesion.setDocumentoIdentidad(cedula);
            sesion.setRol(rol);
        }
    }
}

