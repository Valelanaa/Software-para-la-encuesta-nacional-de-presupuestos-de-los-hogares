package enph.controlador;

import enph.modelo.SesionUsuario;
import enph.modelo.Usuario;
import enph.modelo.UsuarioDAO;
import enph.vista.FamiliasRegistradas;
import enph.vista.InicioSesion;
import enph.vista.MenuAdmin;
import javax.swing.JOptionPane;

/**
 * Controlador de la pantalla InicioSesion (patrón MVC).
 * Lee lo que el usuario escribió en la ventana, le pide a UsuarioDAO
 * que verifique las credenciales y, si son correctas, guarda la sesión
 * y abre la pantalla que corresponde a su rol: MenuAdmin para el
 * administrador y FamiliasRegistradas para el encuestador.
 */
public class InicioSesionController {

    private final InicioSesion vista; // ventana de inicio de sesión
    private final UsuarioDAO usuarioDAO; // acceso a la tabla ENCUESTADOR

    // la ventana InicioSesion que este controlador atiende
    public InicioSesionController(InicioSesion vista) {
        this.vista = vista;
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Se ejecuta al pulsar el botón de ingresar: valida los campos, comprueba
     * las credenciales en la base de datos y abre la siguiente pantalla.
     */
    public void iniciarSesion() {
        // trim() quita espacios accidentales al inicio y al final del usuario
        String documento = vista.getTxtUsuario().getText().trim();
        // El campo de contraseña entrega un arreglo de caracteres (char[]),
        // por seguridad; aquí se convierte a String para compararla
        String password = new String(vista.getTxtPassword().getPassword());

        // Validación previa: no se consulta la base de datos con campos vacíos
        if (documento.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Por favor, ingrese el usuario y la contraseña.",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Consulta a la base de datos: devuelve el usuario (con nombre y rol)
        // si el documento y la contraseña coinciden, o null si no
        Usuario usuario = usuarioDAO.autenticar(documento, password);

        if (usuario != null) {
            // Se guarda la sesión para que cualquier ventana pueda leer el nombre y el rol
            SesionUsuario.iniciar(usuario);

            try {
                if ("ADMINISTRADOR".equalsIgnoreCase(usuario.getRol())) {
                    // Administrador: abre su menú, centrado
                    MenuAdmin menu = new MenuAdmin();
                    menu.setLocationRelativeTo(null);
                    menu.setVisible(true);
                } else {
                    // Encuestador: abre la pantalla de familias, centrada
                    FamiliasRegistradas familias = new FamiliasRegistradas();
                    familias.setLocationRelativeTo(null);
                    familias.setVisible(true);
                }
                // Cierra la ventana de inicio de sesión
                vista.dispose();

            } catch (Exception e) {
                // Si la pantalla no se pudo abrir, se avisa
                // y el detalle queda en la consola para depurar
                SesionUsuario.cerrar(); // no dejar una sesión abierta sin ventana
                JOptionPane.showMessageDialog(
                        vista,
                        "No se pudo abrir la pantalla: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        } else {
            // autenticar devuelve null tanto si las credenciales no
            // coinciden como si falló la conexión; por eso el mensaje cubre ambos casos
            JOptionPane.showMessageDialog(
                    vista,
                    "Usuario o contraseña incorrectos, o no fue posible validar el acceso.",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}