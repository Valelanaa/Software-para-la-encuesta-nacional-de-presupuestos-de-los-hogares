/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla USUARIO: comprueba las credenciales escritas en
 * InicioSesion, lista los usuarios registrados y permite consultar y actualizar
 * los datos de uno de ellos (pantallas GestionUsuarios y EditarUsuario).
 */
public class UsuarioDAO {

    // Quita espacios sobrantes y evita null (las columnas CHAR rellenan con espacios)
    private static String limpiar(String s) {
        return s == null ? "" : s.trim();
    }

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
        // TRIM quita espacios sobrantes en ambos lados (la base y lo digitado).
        // Los "?" son parámetros: el texto del usuario nunca se pega a la consulta,
        // lo que evita la inyección SQL.
        String sql = "SELECT COUNT(*) FROM USUARIO WHERE TRIM(DOCUMENTO_IDENTIDAD) = TRIM(?) AND TRIM(CONTRASENNA) = TRIM(?)";

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
     * Autentica y devuelve el usuario completo (id, nombre, rol, ciudad y departamento).
     * Si las credenciales no coinciden o hay un error, devuelve null.
     * Este es el método que usa InicioSesionController para llenar la sesión.
     */
    public Usuario autenticar(String documento, String contrasenna) {
        // Se une USUARIO con CIUDAD y DEPARTAMENTO para obtener la ubicación
        String sql = "SELECT U.ID_USUARIO, U.DOCUMENTO_IDENTIDAD, U.NOMBRE_COMPLETO, U.ROL, "
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
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(limpiar(rs.getString("ID_USUARIO")));
                usuario.setDocumentoIdentidad(limpiar(rs.getString("DOCUMENTO_IDENTIDAD")));
                usuario.setNombreCompleto(limpiar(rs.getString("NOMBRE_COMPLETO")));
                usuario.setRol(limpiar(rs.getString("ROL")));
                usuario.setCiudad(limpiar(rs.getString("NOMBRE_CIUDAD")));
                usuario.setDepartamento(limpiar(rs.getString("NOMBRE_DEPARTAMENTO")));
                // la contraseña no se guarda en el objeto: ya no hace falta
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

    /**
     * Devuelve todos los usuarios registrados (id, nombre, documento y rol),
     * ordenados por nombre. Si falla la conexión o la consulta, devuelve una lista vacía.
     * La usa la tabla de GestionUsuarios.
     */
    public List<Usuario> listarUsuarios() {
        String sql = "SELECT ID_USUARIO, NOMBRE_COMPLETO, DOCUMENTO_IDENTIDAD, ROL FROM USUARIO ORDER BY NOMBRE_COMPLETO";

        List<Usuario> usuarios = new ArrayList<>();
        Connection con = Conexion.init();

        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return usuarios;
        }

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(limpiar(rs.getString("ID_USUARIO")));
                u.setNombreCompleto(limpiar(rs.getString("NOMBRE_COMPLETO")));
                u.setDocumentoIdentidad(limpiar(rs.getString("DOCUMENTO_IDENTIDAD")));
                u.setRol(limpiar(rs.getString("ROL")));
                usuarios.add(u);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexion.close(con);
        }
        return usuarios;
    }

    /**
     * Trae todos los datos de un usuario (incluida ciudad, teléfono y contraseña)
     * para mostrarlos en EditarUsuario. Devuelve null si no existe o hay un error.
     */
    public Usuario buscarPorId(String idUsuario) {
        String sql = "SELECT ID_USUARIO, ID_CIUDAD, NOMBRE_COMPLETO, DOCUMENTO_IDENTIDAD, "
                   + "CONTRASENNA, ROL, TELEFONO FROM USUARIO WHERE ID_USUARIO = ?";

        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return null;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(limpiar(rs.getString("ID_USUARIO")));
                    u.setIdCiudad(limpiar(rs.getString("ID_CIUDAD")));
                    u.setNombreCompleto(limpiar(rs.getString("NOMBRE_COMPLETO")));
                    u.setDocumentoIdentidad(limpiar(rs.getString("DOCUMENTO_IDENTIDAD")));
                    u.setContrasenna(limpiar(rs.getString("CONTRASENNA")));
                    u.setRol(limpiar(rs.getString("ROL")));
                    u.setTelefono(limpiar(rs.getString("TELEFONO")));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexion.close(con);
        }
        return null;
    }

    /**
     * Ciudades para el combo de EditarUsuario, con el formato "Ciudad - Departamento".
     * Cada ItemCiudad guarda el ID_CIUDAD (lo que va a la base) y el texto que se ve.
     */
    public List<ItemCiudad> listarCiudades() {
        String sql = "SELECT C.ID_CIUDAD, C.NOMBRE_CIUDAD || ' - ' || D.NOMBRE_DEPARTAMENTO "
                   + "FROM CIUDAD C JOIN DEPARTAMENTO D ON D.ID_DEPARTAMENTO = C.ID_DEPARTAMENTO "
                   + "ORDER BY C.NOMBRE_CIUDAD";

        List<ItemCiudad> lista = new ArrayList<>();
        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ItemCiudad(limpiar(rs.getString(1)), limpiar(rs.getString(2))));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar ciudades: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexion.close(con);
        }
        return lista;
    }

    /**
     * true si otro usuario (distinto de idExcluido) ya tiene ese documento.
     * Se usa antes de guardar para no repetir cédulas.
     */
    public boolean existeDocumento(String documento, String idExcluido) {
        String sql = "SELECT COUNT(*) FROM USUARIO "
                   + "WHERE TRIM(DOCUMENTO_IDENTIDAD) = TRIM(?) AND ID_USUARIO <> ?";

        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            ps.setString(2, idExcluido);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar documento: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Conexion.close(con);
        }
    }

    /**
     * Actualiza en la base los datos del usuario (ciudad, nombre, documento,
     * contraseña, rol y teléfono). Devuelve true si se modificó exactamente una fila.
     */
    public boolean actualizarUsuario(Usuario u) {
        String sql = "UPDATE USUARIO SET ID_CIUDAD = ?, NOMBRE_COMPLETO = ?, DOCUMENTO_IDENTIDAD = ?, "
                   + "CONTRASENNA = ?, ROL = ?, TELEFONO = ? WHERE ID_USUARIO = ?";

        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getIdCiudad());
            ps.setString(2, u.getNombreCompleto());
            ps.setString(3, u.getDocumentoIdentidad());
            ps.setString(4, u.getContrasenna());
            ps.setString(5, u.getRol());
            ps.setString(6, u.getTelefono());
            ps.setString(7, u.getIdUsuario());
            return ps.executeUpdate() == 1; // la conexión confirma sola (autocommit)
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Conexion.close(con);
        }
    }
        /**
     * Elimina un usuario junto con los hogares que tiene asignados y todo lo que
     * depende de ellos (cabeza de hogar, encuestas y respuestas). Se hace en una
     * sola transacción: si algo falla, se deshace y no se borra nada.
     */
    public boolean eliminarUsuario(String idUsuario) {
        // Primero las tablas hijas y al final el usuario (por las llaves foráneas)
        String[] consultas = {
            "DELETE FROM CARACTERIZACION_COMPRA WHERE ID_ENCUESTA IN "
                + "(SELECT E.ID_ENCUESTA FROM ENCUESTA E JOIN HOGAR H ON H.ID_HOGAR = E.ID_HOGAR "
                + "WHERE H.ID_USUARIO = ?)",
            "DELETE FROM ENCUESTA WHERE ID_HOGAR IN "
                + "(SELECT ID_HOGAR FROM HOGAR WHERE ID_USUARIO = ?)",
            "DELETE FROM CABEZA_DE_HOGAR WHERE ID_HOGAR IN "
                + "(SELECT ID_HOGAR FROM HOGAR WHERE ID_USUARIO = ?)",
            "DELETE FROM HOGAR WHERE ID_USUARIO = ?",
            "DELETE FROM USUARIO WHERE ID_USUARIO = ?"
        };

        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            con.setAutoCommit(false); // agrupa todos los DELETE en una sola transacción
            int filasUsuario = 0;
            for (int i = 0; i < consultas.length; i++) {
                try (PreparedStatement ps = con.prepareStatement(consultas[i])) {
                    ps.setString(1, idUsuario);
                    int filas = ps.executeUpdate();
                    if (i == consultas.length - 1) {
                        filasUsuario = filas; // filas borradas de USUARIO
                    }
                }
            }
            if (filasUsuario != 1) {
                con.rollback(); // el usuario no existía: no se confirma nada
                return false;
            }
            con.commit(); // confirma todos los borrados a la vez
            return true;
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ignorada) { }
            System.err.println("Error al eliminar el usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Conexion.close(con);
        }
    }
    /**
    * Registra un usuario nuevo en la base de datos.
    * Genera el siguiente ID y guarda sus datos.
    */
   public boolean insertarUsuario(Usuario u) {

       String sqlId =
               "SELECT NVL(MAX(TO_NUMBER(ID_USUARIO)), 0) + 1 FROM USUARIO";

       String sqlInsertar =
               "INSERT INTO USUARIO "
               + "(ID_USUARIO, ID_CIUDAD, NOMBRE_COMPLETO, "
               + "DOCUMENTO_IDENTIDAD, CONTRASENNA, ROL, TELEFONO) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?)";

       Connection con = Conexion.init();

       if (con == null) {
           System.err.println("No se pudo conectar a la base de datos.");
           return false;
       }

       try {
           // Obtener el siguiente ID disponible.
           String idUsuario;

           try (PreparedStatement psId = con.prepareStatement(sqlId);
                ResultSet rs = psId.executeQuery()) {

               if (!rs.next()) {
                   return false;
               }

               idUsuario = rs.getString(1);
           }

           // Insertar el nuevo usuario.
           try (PreparedStatement ps = con.prepareStatement(sqlInsertar)) {

               ps.setString(1, idUsuario);
               ps.setString(2, u.getIdCiudad());
               ps.setString(3, u.getNombreCompleto());
               ps.setString(4, u.getDocumentoIdentidad());
               ps.setString(5, u.getContrasenna());
               ps.setString(6, u.getRol());
               ps.setString(7, u.getTelefono());

               boolean insertado = ps.executeUpdate() == 1;

               if (insertado) {
                   System.out.println(
                           "Usuario creado con ID: " + idUsuario
                   );
               }

               return insertado;
           }

       } catch (SQLException e) {
           System.err.println("Error al insertar usuario: "
                   + e.getMessage());
           e.printStackTrace();
           return false;

       } finally {
           Conexion.close(con);
       }
   }
   
   /**
    * Comprueba si la cédula ya pertenece a un usuario.
    */
   public boolean existeDocumento(String documento) {

       String sql = "SELECT COUNT(*) FROM USUARIO "
               + "WHERE TRIM(DOCUMENTO_IDENTIDAD) = TRIM(?)";

       Connection con = Conexion.init();

       if (con == null) {
           System.err.println("No se pudo conectar a la base de datos.");
           return true; // No permitir continuar si no se puede verificar.
       }

       try (PreparedStatement ps = con.prepareStatement(sql)) {

           ps.setString(1, documento);

           try (ResultSet rs = ps.executeQuery()) {
               return rs.next() && rs.getInt(1) > 0;
           }

       } catch (SQLException e) {
           System.err.println("Error al verificar la cédula: "
                   + e.getMessage());
           e.printStackTrace();
           return true; // Evitar crear el registro si la consulta falla.

       } finally {
           Conexion.close(con);
       }
   }
}