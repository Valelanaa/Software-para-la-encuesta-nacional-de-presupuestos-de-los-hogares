package enph.modelo;

/**
 * Modelo de un usuario del sistema (el encuestador o administrador que inicia sesión).
 * Guarda las credenciales escritas en la pantalla InicioSesion y, una vez que el
 * acceso es válido, también el nombre y el rol leídos de la base de datos.
 * @author SALA-2
 */
public class Usuario {
    private String documentoIdentidad; // número de documento: es el "usuario" para entrar
    private String contrasenna;        // contraseña escrita (con "nn" porque la ñ da problemas en nombres de código)
    private String nombreCompleto;     // nombre que se muestra en pantalla (se llena al autenticar)
    private String rol;                // ADMINISTRADOR o ENCUESTADOR (se llena al autenticar)
    private String ciudad;
    private String departamento;
    private String idUsuario;
    private String idCiudad;
    private String telefono;

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public String getIdCiudad() { return idCiudad; }
    public void setIdCiudad(String idCiudad) { this.idCiudad = idCiudad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
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