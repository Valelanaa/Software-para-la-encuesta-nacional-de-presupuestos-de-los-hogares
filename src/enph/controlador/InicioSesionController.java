
package enph.controlador;

import enph.modelo.Conexion;
import enph.modelo.SesionUsuario;
import enph.modelo.Usuario;
import enph.modelo.UsuarioDAO;
import enph.vista.InicioSesion;
import enph.vista.MenuAdmin;
import enph.vista.MenuEncuestador;

public class InicioSesionController {

    private final InicioSesion vista;
    private final UsuarioDAO usuarioDAO;

    public InicioSesionController(InicioSesion vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDAO();
    }

    public void iniciarSesion() {

        String documento = vista.getTxtUsuario().getText().trim();

        String password = new String(
                vista.getTxtPassword().getPassword()
        );

        // Validar campos
        if (documento.isEmpty() || password.isEmpty()) {
            vista.mostrarMensaje(
                    "Ingrese su documento y contraseña.",
                    "Campos incompletos",
                    2
            );
            return;
        }

        // Consultar el modelo mediante UsuarioDAO
        Usuario usuario = usuarioDAO.autenticar(documento, password);

        if (usuario == null) {
            vista.mostrarMensaje(
                    "Documento o contraseña incorrectos, "
                    + "o no fue posible validar el acceso.",
                    "Error de inicio de sesión",
                    0
            );

            vista.limpiarContrasena();
            return;
        }

        String rol = usuario.getRol() == null
                ? ""
                : usuario.getRol().trim();

        // Validar rol
        if (!"ADMINISTRADOR".equalsIgnoreCase(rol)
                && !"ENCUESTADOR".equalsIgnoreCase(rol)) {

            vista.mostrarMensaje(
                    "El usuario no tiene un rol válido asignado.",
                    "Rol no reconocido",
                    2
            );
            return;
        }

        // Configurar conexión según el rol
        if (!Conexion.usarRol(rol)) {

            vista.mostrarMensaje(
                    "No fue posible configurar el acceso según el rol.",
                    "Error de conexión",
                    0
            );

            Conexion.usarGeneral();
            return;
        }

        // Registrar sesión
        SesionUsuario.iniciar(usuario);

        try {

            if ("ADMINISTRADOR".equalsIgnoreCase(rol)) {

                MenuAdmin menu = new MenuAdmin();
                menu.setLocationRelativeTo(null);
                menu.setVisible(true);

            } else {

                MenuEncuestador menu = new MenuEncuestador();
                menu.setLocationRelativeTo(null);
                menu.setVisible(true);
            }

            vista.dispose();

        } catch (Exception e) {

            SesionUsuario.cerrar();

            vista.mostrarMensaje(
                    "No fue posible abrir el menú correspondiente:\n"
                    + e.getMessage(),
                    "Error",
                    0
            );

            e.printStackTrace();
        }
    }
}
