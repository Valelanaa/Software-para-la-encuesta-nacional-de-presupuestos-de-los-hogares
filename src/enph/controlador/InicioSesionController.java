package enph.controlador;

import enph.modelo.Conexion;
import enph.modelo.SesionUsuario;
import enph.modelo.Usuario;
import enph.modelo.UsuarioDAO;
import java.sql.SQLException;

public class InicioSesionController {

    private final UsuarioDAO usuarioDAO;

    public InicioSesionController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario iniciarSesion(String documento, String password)
            throws AppException {

        if (documento == null || documento.trim().isEmpty()
                || password == null || password.isEmpty()) {
            throw new AppException(
                    "Ingrese su documento y contraseña."
            );
        }

        Usuario usuario;

        try {
            usuario = usuarioDAO.autenticar(
                    documento.trim(),
                    password
            );
        } catch (SQLException e) {
            throw new AppException("No fue posible validar las credenciales con la base de datos.", e);
        }

        if (usuario == null) {
            throw new AppException(
                    "Documento o contraseña incorrectos, "
                    + "o no fue posible validar el acceso."
            );
        }

        String rol = usuario.getRol() == null
                ? ""
                : usuario.getRol().trim();

        if (!"ADMINISTRADOR".equalsIgnoreCase(rol)
                && !"ENCUESTADOR".equalsIgnoreCase(rol)) {

            throw new AppException(
                    "El usuario no tiene un rol válido asignado."
            );
        }

        if (!Conexion.usarRol(rol)) {
            Conexion.usarGeneral();

            throw new AppException(
                    "No fue posible configurar el acceso según el rol."
            );
        }

        SesionUsuario.iniciar(usuario);

        return usuario;
    }
}