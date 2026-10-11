/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.conexion;
import enph.conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
    /**
     * Acceso a datos de la tabla ENCUESTADOR: comprueba si el documento y la
     * contraseña escritos en InicioSesion corresponden a un encuestador registrado.
     */
public class UsuarioDAO {

    /**
     * Valida el acceso aceptando directamente documento y contraseña (Strings)
     * Solo extrae el documento y la contraseña y llama a la otra versión del método.
     */
    public boolean validarUsuario(Usuario usuario) {
        if (usuario == null) return false;
        return validarUsuario(usuario.getDocumentoIdentidad(), usuario.getContrasenna());
    }
    //Valida el acceso recibiendo directamente el documento y la contraseña.
    //Cuenta cuántos encuestadores coinciden con esas dos credenciales;
    //si hay al menos uno, el acceso es válido.
    public boolean validarUsuario(String documento, String contrasenna) {
         // TRIM quita espacios sobrantes en ambos lados (la base y lo digitado),
        // porque las columnas CHAR rellenan con espacios y comparar fallaría.
        // Los "?" son parámetros: el texto del usuario nunca se pega a la consulta,
        // lo que evita la inyección SQL.
        String sql = "SELECT COUNT(*) FROM ENCUESTADOR WHERE TRIM(DOCUMENTO_IDENTIDAD) = TRIM(?) AND TRIM(CONTRASENNA) = TRIM(?)";
        
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
            // existe un encuestador con ese documento y esa contraseña
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
}