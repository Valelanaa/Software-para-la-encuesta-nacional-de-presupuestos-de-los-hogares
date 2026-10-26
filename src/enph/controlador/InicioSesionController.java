package enph.controlador;

import enph.modelo.UsuarioDAO; 
import enph.vista.FamiliasRegistradas;
import enph.vista.InicioSesion;
import javax.swing.JOptionPane;

/**
 * Controlador de la pantalla InicioSesion (patrón MVC).
 * Lee lo que el encuestador escribió en la ventana, le pide a UsuarioDAO
 * que verifique las credenciales y, si son correctas, abre FamiliasRegistradas.
 */

public class InicioSesionController {

    private final InicioSesion vista; // ventana de inicio de sesión
    private final UsuarioDAO usuarioDAO; // acceso a la tabla ENCUESTADOR
    //la ventana InicioSesion que este controlador atiende
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
        String usuario = vista.getTxtUsuario().getText().trim();
        // El campo de contraseña entrega un arreglo de caracteres (char[]),
        // por seguridad; aquí se convierte a String para compararla
        String password = new String(vista.getTxtPassword().getPassword());
        // Validación previa: no se consulta la base de datos con campos vacíos
        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Por favor, ingrese el usuario y la contraseña.",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        // Consulta a la base de datos: true solo si existe un encuestador
        // con ese documento y esa contraseña
        boolean esValido = usuarioDAO.validarUsuario(usuario, password);

        if (esValido) {
            try {
                // Acceso correcto: abre la pantalla de familias, centrada
                FamiliasRegistradas familias = new FamiliasRegistradas();
                familias.setLocationRelativeTo(null);
                familias.setVisible(true);
                // Cierra la ventana de inicio de sesión
                vista.dispose();

            } catch (Exception e) {
                // Si la pantalla de familias no se pudo abrir, se avisa
                // y el detalle queda en la consola para depurar
                JOptionPane.showMessageDialog(
                        vista,
                        "No se pudo abrir FamiliasRegistradas: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        } else {
            // validarUsuario devuelve false tanto si las credenciales no
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