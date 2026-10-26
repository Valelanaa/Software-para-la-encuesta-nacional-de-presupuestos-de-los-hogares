/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 * Guarda el usuario que inició sesión mientras la aplicación está abierta.
 * Todos sus miembros son estáticos, así que cualquier ventana puede leer
 * quién entró sin crear un objeto SesionUsuario.
 */
public class SesionUsuario {

    private static Usuario actual; // usuario que inició sesión (null si no hay ninguno)

    // Se llama desde InicioSesionController cuando el acceso es correcto
    public static void iniciar(Usuario usuario) {
        actual = usuario;
    }

    // Se llama al cerrar sesión, para que no quede nadie guardado
    public static void cerrar() {
        actual = null;
    }

    // Devuelve el usuario que inició sesión, o null si no hay sesión
    public static Usuario getActual() {
        return actual;
    }
    // Texto listo para mostrar en una etiqueta; si no hay sesión, devuelve un valor por defecto
    public static String nombreParaMostrar() {
        return (actual != null && actual.getNombreCompleto() != null) ? actual.getNombreCompleto() : "Sin sesión";
    }

    public static String rolParaMostrar() {
        return (actual != null && actual.getRol() != null) ? actual.getRol() : "";
    }
    // Texto "Ciudad - Departamento" listo para una etiqueta
    public static String ubicacionParaMostrar() {
        if (actual == null || actual.getCiudad() == null || actual.getDepartamento() == null) {
            return "";
        }
        return actual.getCiudad() + " - " + actual.getDepartamento();
    }
}