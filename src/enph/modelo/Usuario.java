package enph.modelo;

/**
 * Modelo de un usuario del sistema (el encuestador o administrador que inicia sesión).
 * Guarda las credenciales escritas en la pantalla InicioSesion y, una vez que el
 * acceso es válido, también el nombre y el rol leídos de la base de datos.
 *
 * Dominio: Usuario tiene una Ciudad (Ciudad 1 - 0..* Usuario) y de él heredan
 * Encuestador y Administrador.
 * @author SALA-2
 */
public class Usuario {
    private String documentoIdentidad; // número de documento: es el "usuario" para entrar
    private String contrasenna;        // contraseña escrita (con "nn" porque la ñ da problemas en nombres de código)
    private String nombreCompleto;     // nombre que se muestra en pantalla (se llena al autenticar)
    private String rol;                // ADMINISTRADOR o ENCUESTADOR (se llena al autenticar)
    private Ciudad ciudad;             // ciudad del usuario, con su departamento (puede ser null)
    private String idUsuario;
    private String telefono;
    private String idEncuestador;      // ID_ENCUESTADOR de la tabla ENCUESTADOR (null si es administrador)

    public String getIdEncuestador() { return idEncuestador; }
    public void setIdEncuestador(String idEncuestador) { this.idEncuestador = idEncuestador; }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public Ciudad getCiudad() { return ciudad; }
    public void setCiudad(Ciudad ciudad) { this.ciudad = ciudad; }

    /** ID de la ciudad como texto (lo que se envía a los procedimientos); null si no tiene ciudad. */
    public String getIdCiudad() { return ciudad == null ? null : ciudad.getId(); }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    // Constructor vacío: permite crear el objeto y llenarlo después con los setters
    public Usuario() {
    }

    // Constructor con las credenciales: crea el usuario con sus dos datos de una vez
    public Usuario(String documentoIdentidad, String contrasenna) {
        this.documentoIdentidad = documentoIdentidad;
        this.contrasenna = contrasenna;
    }

    // Métodos de lectura y escritura (getters y setters) de cada dato.
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

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
