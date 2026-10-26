package enph.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Acceso a datos de la tabla USUARIO: comprueba si el documento y la
 * contraseña escritos en InicioSesion corresponden a un usuario registrado
 * y, si es así, devuelve su nombre y su rol.
 */
public class UsuarioDAO {

    /**
     * Valida el acceso recibiendo un objeto Usuario.
     * Solo extrae el documento y la contraseña y llama a la otra versión del método.
     */
    public boolean validarUsuario(Usuario usuario) {
        if (usuario == null) return false;
        return validarUsuario(usuario.getDocumentoIdentidad(), usuario.getContrasenna());
    }

    // Valida el acceso recibiendo directamente el documento y la contraseña.
    // Cuenta cuántos usuarios coinciden con esas dos credenciales;
    // si hay al menos uno, el acceso es válido.
    public boolean validarUsuario(String documento, String contrasenna) {
        // TRIM quita espacios sobrantes en ambos lados (la base y lo digitado),
        // porque las columnas CHAR rellenan con espacios y comparar fallaría.
        // Los "?" son parámetros: el texto del usuario nunca se pega a la consulta,
        // lo que evita la inyección SQL.
        String sql = "SELECT U.DOCUMENTO_IDENTIDAD, U.NOMBRE_COMPLETO, U.ROL, "
           + "C.NOMBRE_CIUDAD, D.NOMBRE_DEPARTAMENTO "
           + "FROM USUARIO U "
           + "JOIN CIUDAD C ON C.ID_CIUDAD = U.ID_CIUDAD "
           + "JOIN DEPARTAMENTO D ON D.ID_DEPARTAMENTO = C.ID_DEPARTAMENTO "
           + "WHERE TRIM(U.DOCUMENTO_IDENTIDAD) = TRIM(?) AND TRIM(U.CONTRASENNA) = TRIM(?)";

        Connection con = Conexion.init();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean esValido = false; // por defecto se niega el acceso

        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, documento);
            ps.setString(2, contrasenna);

            rs = ps.executeQuery();
            // COUNT(*) siempre devuelve una fila: mayor que 0 significa que
            // existe un usuario con ese documento y esa contraseña
            if (rs.next()) {
                esValido = rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            // Ante cualquier error el acceso se niega (esValido sigue en false)
            System.err.println("Error al validar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Se cierran los recursos en orden: resultado, consulta y conexión
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
            Conexion.close(con);
        }

        return esValido;
    }

    /**
     * Autentica y devuelve el usuario completo (con nombre y rol).
     * Si las credenciales no coinciden o hay un error, devuelve null.
     * Este es el método que debe usar InicioSesion para llenar la sesión.
     */
    public Usuario autenticar(String documento, String contrasenna) {
    // 1) La consulta ahora une USUARIO con CIUDAD y DEPARTAMENTO
    String sql = "SELECT U.DOCUMENTO_IDENTIDAD, U.NOMBRE_COMPLETO, U.ROL, "
               + "C.NOMBRE_CIUDAD, D.NOMBRE_DEPARTAMENTO "
               + "FROM USUARIO U "
               + "JOIN CIUDAD C ON C.ID_CIUDAD = U.ID_CIUDAD "
               + "JOIN DEPARTAMENTO D ON D.ID_DEPARTAMENTO = C.ID_DEPARTAMENTO "
               + "WHERE TRIM(U.DOCUMENTO_IDENTIDAD) = TRIM(?) AND TRIM(U.CONTRASENNA) = TRIM(?)";

    Connection con = Conexion.init();
    PreparedStatement ps = null;
    ResultSet rs = null;
    Usuario usuario = null; // por defecto no hay usuario (acceso negado)

    if (con == null) {
        System.err.println("No se pudo conectar a la base de datos.");
        return null;
    }

    try {
        ps = con.prepareStatement(sql);
        ps.setString(1, documento);
        ps.setString(2, contrasenna);

        rs = ps.executeQuery();

        // 2) Aquí va el bloque: se llena el usuario con lo que devolvió la consulta
        if (rs.next()) {
            usuario = new Usuario();
            usuario.setDocumentoIdentidad(rs.getString("DOCUMENTO_IDENTIDAD").trim());
            usuario.setNombreCompleto(rs.getString("NOMBRE_COMPLETO").trim());
            usuario.setRol(rs.getString("ROL").trim());
            usuario.setCiudad(rs.getString("NOMBRE_CIUDAD").trim());              // nuevo
            usuario.setDepartamento(rs.getString("NOMBRE_DEPARTAMENTO").trim());  // nuevo
        }

    } catch (SQLException e) {
        System.err.println("Error al autenticar usuario: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
        Conexion.close(con);
    }

    return usuario;
}
    
}