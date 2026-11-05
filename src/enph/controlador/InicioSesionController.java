package enph.controlador;

import enph.modelo.Conexion;
import enph.modelo.SesionUsuario;
import enph.modelo.Usuario;
import enph.modelo.UsuarioDAO;
import enph.vista.InicioSesion;
import enph.vista.MenuAdmin;
import enph.vista.MenuEncuestador;
import javax.swing.JOptionPane;

public class InicioSesionController {

    private final InicioSesion vista;
    private final UsuarioDAO usuarioDAO;

    public InicioSesionController(InicioSesion vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDAO();
    }

    public void iniciarSesion() {

        // Obtener los datos ingresados
        String documento = vista.getTxtUsuario().getText().trim();

        String password = new String(
                vista.getTxtPassword().getPassword()
        );

        // Validar campos vacíos
        if (documento.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Ingrese su documento y contraseña.",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Consultar usuario en Oracle
        Usuario usuario = usuarioDAO.autenticar(documento, password);

        // Validar autenticación
        if (usuario == null) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Documento o contraseña incorrectos, "
                    + "o no fue posible validar el acceso.",
                    "Error de inicio de sesión",
                    JOptionPane.ERROR_MESSAGE
            );

            vista.getTxtPassword().setText("");
            return;
        }

        // Obtener el rol registrado en Oracle
        String rol = usuario.getRol() == null
                ? ""
                : usuario.getRol().trim();

        // Verificar que tenga un rol reconocido
        if (!"ADMINISTRADOR".equalsIgnoreCase(rol)
                && !"ENCUESTADOR".equalsIgnoreCase(rol)) {

            JOptionPane.showMessageDialog(
                    vista,
                    "El usuario no tiene un rol válido asignado.",
                    "Rol no reconocido",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Cambiar la conexión de Oracle según el rol
        if (!Conexion.usarRol(rol)) {
            JOptionPane.showMessageDialog(
                    vista,
                    "No fue posible configurar el acceso según el rol.",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
            Conexion.usarGeneral();
            return;
        }

        // Guardar el usuario autenticado
        SesionUsuario.iniciar(usuario);

        try {

            if ("ADMINISTRADOR".equalsIgnoreCase(rol)) {

                // Abrir menú del administrador
                MenuAdmin menu = new MenuAdmin();
                menu.setLocationRelativeTo(null);
                menu.setVisible(true);

            } else {

                // Abrir menú del encuestador
                MenuEncuestador menu = new MenuEncuestador();
                menu.setLocationRelativeTo(null);
                menu.setVisible(true);
            }

            // Cerrar ventana de inicio de sesión
            vista.dispose();

        } catch (Exception e) {

            SesionUsuario.cerrar();

            JOptionPane.showMessageDialog(
                    vista,
                    "No fue posible abrir el menú correspondiente:\n"
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }
}