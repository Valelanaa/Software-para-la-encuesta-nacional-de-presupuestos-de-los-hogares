/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.conexion;

/**
 *Modelo de un usuario del sistema (el encuestador que inicia sesión).
 * Guarda las credenciales que se escriben en la pantalla InicioSesion,
 * para que UsuarioDAO las compare con las de la base de datos.
 * @author SALA-2
 */
public class Usuario {
    private String documentoIdentidad; // número de documento: es el "usuario" para entrar
    private String contrasenna; // contraseña escrita (se escribe con "nn" porque la ñ da problemas en nombres de código)
    // Constructor vacío: permite crear el objeto y llenarlo después con los setters
    public Usuario() {
    }
    // Constructor completo: crea el usuario con sus dos datos de una vez
    public Usuario(String documentoIdentidad, String contrasenna) {
        this.documentoIdentidad = documentoIdentidad;
        this.contrasenna = contrasenna;
    }
    // Métodos de lectura y escritura (getters y setters) de cada dato.
    // A diferencia de Hogar, aquí sí hay setters porque los datos se llenan
    // a partir de lo que el usuario digita en el formulario.
    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public String getContrasenna() {
        return contrasenna;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }
}
