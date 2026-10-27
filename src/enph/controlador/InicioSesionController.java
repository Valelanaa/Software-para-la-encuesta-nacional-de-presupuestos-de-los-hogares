package enph.controlador;

import enph.modelo.SesionUsuario;
import enph.modelo.Usuario;
import enph.modelo.UsuarioDAO;
import enph.vista.InicioSesion;
import enph.vista.MenuAdmin;
import enph.vista.MenuEncuestador;
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
            SesionUsuario.iniciar(usuario);

            String rol = usuario.getRol() == null ? "" : usuario.getRol().trim();
            System.out.println("Rol leído de la base: [" + rol + "]"); // para depurar; bórrar después

            try {
                if ("ADMINISTRADOR".equalsIgnoreCase(rol)) {
                    MenuAdmin menu = new MenuAdmin();
                    menu.setLocationRelativeTo(null);
                    menu.setVisible(true);
                    vista.dispose();

                } else if ("ENCUESTADOR".equalsIgnoreCase(rol)) {
                    MenuEncuestador menu2 = new MenuEncuestador();
                    menu2.setLocationRelativeTo(null);
                    menu2.setVisible(true);
                    vista.dispose();

                } else {
                    // Rol desconocido: no se abre ninguna pantalla
                    SesionUsuario.cerrar();
                    JOptionPane.showMessageDialog(
                            vista,
                            "El rol del usuario (" + rol + ") no tiene una pantalla asignada.",
                            "Rol no reconocido",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } catch (Exception e) {
                SesionUsuario.cerrar();
                JOptionPane.showMessageDialog(
                        vista,
                        "No se pudo abrir la pantalla: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        }
    }
}