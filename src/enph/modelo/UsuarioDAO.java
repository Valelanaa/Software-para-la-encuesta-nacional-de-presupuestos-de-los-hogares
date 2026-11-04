package enph.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de los usuarios del sistema.
 * Las credenciales y el rol están en la tabla PAR_USUARIO. Los usuarios con rol
 * ENCUESTADOR además tienen una fila en la tabla ENCUESTADOR (teléfono, ciudad y su
 * ID_ENCUESTADOR); las dos tablas se relacionan por DOCUMENTO_IDENTIDAD.
 * Sirve a InicioSesion, GestionUsuarios, CrearUsuario y EditarUsuario.
 */
public class UsuarioDAO {

    // Quita espacios sobrantes y evita null (las columnas CHAR rellenan con espacios)
    private static String limpiar(String s) {
        return s == null ? "" : s.trim();
    }

    // Igual que limpiar, pero devuelve null si no hay valor (por ejemplo, un administrador sin ciudad)
    private static String limpiarONull(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }

    private static boolean esEncuestador(String rol) {
        return rol != null && "ENCUESTADOR".equalsIgnoreCase(rol.trim());
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
        String sql = "SELECT COUNT(*) FROM PAR_USUARIO WHERE TRIM(DOCUMENTO_IDENTIDAD) = TRIM(?) AND TRIM(CONTRASENNA) = TRIM(?)";

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
            if (rs.next()) {
                esValido = rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
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

        return esValido;
    }

    /**
     * Autentica y devuelve el usuario completo (id, nombre, rol y, si es encuestador,
     * su ID_ENCUESTADOR, ciudad y departamento). Los administradores no tienen fila en
     * ENCUESTADOR, por eso se usa LEFT JOIN: su ciudad y departamento quedan en null.
     * Si las credenciales no coinciden o hay un error, devuelve null.
     * Este es el método que usa InicioSesionController para llenar la sesión.
     */
    public Usuario autenticar(String documento, String contrasenna) {
        String sql = "SELECT U.ID_USUARIO, U.DOCUMENTO_IDENTIDAD, U.NOMBRE_COMPLETO, U.ROL, "
                   + "E.ID_ENCUESTADOR, C.NOMBRE_CIUDAD, D.NOMBRE_DEPARTAMENTO "
                   + "FROM PAR_USUARIO U "
                   + "LEFT JOIN ENCUESTADOR E ON TRIM(E.DOCUMENTO_IDENTIDAD) = TRIM(U.DOCUMENTO_IDENTIDAD) "
                   + "LEFT JOIN CIUDAD C ON C.ID_CIUDAD = E.ID_CIUDAD "
                   + "LEFT JOIN DEPARTAMENTO D ON D.ID_DEPARTAMENTO = C.ID_DEPARTAMENTO "
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
                usuario.setIdEncuestador(limpiarONull(rs.getString("ID_ENCUESTADOR")));
                usuario.setCiudad(limpiarONull(rs.getString("NOMBRE_CIUDAD")));
                usuario.setDepartamento(limpiarONull(rs.getString("NOMBRE_DEPARTAMENTO")));
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
     * Devuelve todos los usuarios registrados (id, nombre, documento, rol y, si es
     * encuestador, su ID_ENCUESTADOR), ordenados por nombre.
     * Si falla la conexión o la consulta, devuelve una lista vacía.
     * La usan GestionUsuarios, AsignarFamilias e InformeEncuesta.
     */
    public List<Usuario> listarUsuarios() {
        String sql = "SELECT U.ID_USUARIO, U.NOMBRE_COMPLETO, U.DOCUMENTO_IDENTIDAD, U.ROL, E.ID_ENCUESTADOR "
                   + "FROM PAR_USUARIO U "
                   + "LEFT JOIN ENCUESTADOR E ON TRIM(E.DOCUMENTO_IDENTIDAD) = TRIM(U.DOCUMENTO_IDENTIDAD) "
                   + "ORDER BY U.NOMBRE_COMPLETO";

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
                u.setIdEncuestador(limpiarONull(rs.getString("ID_ENCUESTADOR")));
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
     * Trae todos los datos de un usuario para mostrarlos en EditarUsuario.
     * La ciudad y el teléfono salen de ENCUESTADOR (quedan vacíos para un administrador).
     * Devuelve null si no existe o hay un error.
     */
    public Usuario buscarPorId(String idUsuario) {
        String sql = "SELECT U.ID_USUARIO, U.NOMBRE_COMPLETO, U.DOCUMENTO_IDENTIDAD, "
                   + "U.CONTRASENNA, U.ROL, E.ID_ENCUESTADOR, E.ID_CIUDAD, E.TELEFONO "
                   + "FROM PAR_USUARIO U "
                   + "LEFT JOIN ENCUESTADOR E ON TRIM(E.DOCUMENTO_IDENTIDAD) = TRIM(U.DOCUMENTO_IDENTIDAD) "
                   + "WHERE U.ID_USUARIO = ?";

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
                    u.setNombreCompleto(limpiar(rs.getString("NOMBRE_COMPLETO")));
                    u.setDocumentoIdentidad(limpiar(rs.getString("DOCUMENTO_IDENTIDAD")));
                    u.setContrasenna(limpiar(rs.getString("CONTRASENNA")));
                    u.setRol(limpiar(rs.getString("ROL")));
                    u.setIdEncuestador(limpiarONull(rs.getString("ID_ENCUESTADOR")));
                    u.setIdCiudad(limpiar(rs.getString("ID_CIUDAD")));
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
        String sql = "SELECT COUNT(*) FROM PAR_USUARIO "
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
     * Actualiza el usuario en PAR_USUARIO (nombre, documento, contraseña y rol) y, si es
     * encuestador, también su fila en ENCUESTADOR (nombre, documento, teléfono y ciudad);
     * si todavía no tenía fila, la crea. Si el rol pasa a ADMINISTRADOR, se borra su fila
     * de ENCUESTADOR y sus hogares quedan sin encuestador asignado.
     * Todo en una sola transacción. Devuelve true si se modificó exactamente un usuario.
     */
    public boolean actualizarUsuario(Usuario u) {
        String sqlDocActual = "SELECT DOCUMENTO_IDENTIDAD FROM PAR_USUARIO WHERE ID_USUARIO = ?";
        String sqlUsuario = "UPDATE PAR_USUARIO SET NOMBRE_COMPLETO = ?, DOCUMENTO_IDENTIDAD = ?, "
                          + "CONTRASENNA = ?, ROL = ? WHERE ID_USUARIO = ?";
        String sqlEncActualizar = "UPDATE ENCUESTADOR SET NOMBRE_COMPLETO = ?, DOCUMENTO_IDENTIDAD = ?, "
                                + "TELEFONO = ?, ID_CIUDAD = ? WHERE TRIM(DOCUMENTO_IDENTIDAD) = TRIM(?)";
        String sqlEncInsertar = "INSERT INTO ENCUESTADOR "
                              + "(ID_ENCUESTADOR, NOMBRE_COMPLETO, DOCUMENTO_IDENTIDAD, TELEFONO, ID_CIUDAD) "
                              + "VALUES (?, ?, ?, ?, ?)";
        String sqlDesasignar = "UPDATE HOGAR SET ID_ENCUESTADOR = NULL WHERE ID_ENCUESTADOR IN "
                             + "(SELECT ID_ENCUESTADOR FROM ENCUESTADOR WHERE TRIM(DOCUMENTO_IDENTIDAD) = TRIM(?))";
        String sqlEncEliminar = "DELETE FROM ENCUESTADOR WHERE TRIM(DOCUMENTO_IDENTIDAD) = TRIM(?)";

        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            con.setAutoCommit(false);

            // Documento que tiene hoy el usuario: con él se ubica su fila en ENCUESTADOR
            String docActual = null;
            try (PreparedStatement ps = con.prepareStatement(sqlDocActual)) {
                ps.setString(1, u.getIdUsuario());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) docActual = limpiar(rs.getString(1));
                }
            }
            if (docActual == null) {
                con.rollback(); // el usuario no existe
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sqlUsuario)) {
                ps.setString(1, u.getNombreCompleto());
                ps.setString(2, u.getDocumentoIdentidad());
                ps.setString(3, u.getContrasenna());
                ps.setString(4, u.getRol());
                ps.setString(5, u.getIdUsuario());
                if (ps.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }
            }

            if (esEncuestador(u.getRol())) {
                int filas;
                try (PreparedStatement ps = con.prepareStatement(sqlEncActualizar)) {
                    ps.setString(1, u.getNombreCompleto());
                    ps.setString(2, u.getDocumentoIdentidad());
                    ps.setString(3, u.getTelefono());
                    ps.setString(4, u.getIdCiudad());
                    ps.setString(5, docActual);
                    filas = ps.executeUpdate();
                }
                if (filas == 0) { // era administrador: ahora se crea su fila de encuestador
                    try (PreparedStatement ps = con.prepareStatement(sqlEncInsertar)) {
                        ps.setString(1, String.valueOf(
                                EncuestaDAO.siguienteId(con, "ENCUESTADOR", "ID_ENCUESTADOR")));
                        ps.setString(2, u.getNombreCompleto());
                        ps.setString(3, u.getDocumentoIdentidad());
                        ps.setString(4, u.getTelefono());
                        ps.setString(5, u.getIdCiudad());
                        ps.executeUpdate();
                    }
                }
            } else {
                // Pasó a administrador: libera sus hogares y borra su fila de encuestador
                try (PreparedStatement ps = con.prepareStatement(sqlDesasignar)) {
                    ps.setString(1, docActual);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(sqlEncEliminar)) {
                    ps.setString(1, docActual);
                    ps.executeUpdate();
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ignorada) { }
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException ignorada) { }
            Conexion.close(con);
        }
    }

    /**
     * Elimina un usuario de PAR_USUARIO y, si era encuestador, su fila de ENCUESTADOR.
     * Los hogares que tenía asignados NO se borran: quedan sin encuestador (ID_ENCUESTADOR
     * en null) y conservan sus encuestas. Todo en una sola transacción.
     */
    public boolean eliminarUsuario(String idUsuario) {
        String sqlDoc = "SELECT DOCUMENTO_IDENTIDAD FROM PAR_USUARIO WHERE ID_USUARIO = ?";
        String sqlDesasignar = "UPDATE HOGAR SET ID_ENCUESTADOR = NULL WHERE ID_ENCUESTADOR IN "
                             + "(SELECT ID_ENCUESTADOR FROM ENCUESTADOR WHERE TRIM(DOCUMENTO_IDENTIDAD) = TRIM(?))";
        String sqlEncuestador = "DELETE FROM ENCUESTADOR WHERE TRIM(DOCUMENTO_IDENTIDAD) = TRIM(?)";
        String sqlUsuario = "DELETE FROM PAR_USUARIO WHERE ID_USUARIO = ?";

        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            con.setAutoCommit(false); // agrupa todos los cambios en una sola transacción

            String documento = null;
            try (PreparedStatement ps = con.prepareStatement(sqlDoc)) {
                ps.setString(1, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) documento = limpiar(rs.getString(1));
                }
            }
            if (documento == null) {
                con.rollback(); // el usuario no existía
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sqlDesasignar)) {
                ps.setString(1, documento);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(sqlEncuestador)) {
                ps.setString(1, documento);
                ps.executeUpdate();
            }
            int filasUsuario;
            try (PreparedStatement ps = con.prepareStatement(sqlUsuario)) {
                ps.setString(1, idUsuario);
                filasUsuario = ps.executeUpdate();
            }

            if (filasUsuario != 1) {
                con.rollback();
                return false;
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ignorada) { }
            System.err.println("Error al eliminar el usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException ignorada) { }
            Conexion.close(con);
        }
    }

    /**
     * Registra un usuario nuevo: lo guarda en PAR_USUARIO y, si su rol es ENCUESTADOR,
     * también en ENCUESTADOR (con teléfono y ciudad). Los IDs se calculan con MAX + 1.
     * Si falla cualquiera de los dos inserts no se guarda nada.
     */
    public boolean insertarUsuario(Usuario u) {
        String sqlUsuario = "INSERT INTO PAR_USUARIO "
                          + "(ID_USUARIO, DOCUMENTO_IDENTIDAD, CONTRASENNA, ROL, NOMBRE_COMPLETO) "
                          + "VALUES (?, ?, ?, ?, ?)";
        String sqlEncuestador = "INSERT INTO ENCUESTADOR "
                              + "(ID_ENCUESTADOR, NOMBRE_COMPLETO, DOCUMENTO_IDENTIDAD, TELEFONO, ID_CIUDAD) "
                              + "VALUES (?, ?, ?, ?, ?)";

        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            con.setAutoCommit(false);

            String idUsuario = String.valueOf(
                    EncuestaDAO.siguienteId(con, "PAR_USUARIO", "ID_USUARIO"));

            try (PreparedStatement ps = con.prepareStatement(sqlUsuario)) {
                ps.setString(1, idUsuario);
                ps.setString(2, u.getDocumentoIdentidad());
                ps.setString(3, u.getContrasenna());
                ps.setString(4, u.getRol());
                ps.setString(5, u.getNombreCompleto());
                ps.executeUpdate();
            }

            if (esEncuestador(u.getRol())) {
                try (PreparedStatement ps = con.prepareStatement(sqlEncuestador)) {
                    ps.setString(1, String.valueOf(
                            EncuestaDAO.siguienteId(con, "ENCUESTADOR", "ID_ENCUESTADOR")));
                    ps.setString(2, u.getNombreCompleto());
                    ps.setString(3, u.getDocumentoIdentidad());
                    ps.setString(4, u.getTelefono());
                    ps.setString(5, u.getIdCiudad());
                    ps.executeUpdate();
                }
            }

            con.commit();
            System.out.println("Usuario creado con ID: " + idUsuario);
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            e.printStackTrace();
            try { con.rollback(); } catch (SQLException ignorada) { }
            return false;
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException ignorada) { }
            Conexion.close(con);
        }
    }

    /**
     * Comprueba si la cédula ya pertenece a un usuario.
     */
    public boolean existeDocumento(String documento) {
        String sql = "SELECT COUNT(*) FROM PAR_USUARIO "
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
            System.err.println("Error al verificar la cédula: " + e.getMessage());
            e.printStackTrace();
            return true; // Evitar crear el registro si la consulta falla.
        } finally {
            Conexion.close(con);
        }
    }
}