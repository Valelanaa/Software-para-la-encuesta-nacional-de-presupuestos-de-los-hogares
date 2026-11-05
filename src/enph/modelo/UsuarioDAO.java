
package enph.modelo;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;

/**
 * Acceso a datos de los usuarios del sistema.
 *
 * Todas las operaciones se realizan mediante funciones y procedimientos
 * almacenados en Oracle.
 *
 * Las credenciales y los datos generales están en PAR_USUARIO.
 * Los usuarios con rol ENCUESTADOR también tienen un registro en ENCUESTADOR.
 */
public class UsuarioDAO {

    // Quita espacios sobrantes y evita valores null.
    private static String limpiar(String s) {
        return s == null ? "" : s.trim();
    }

    // Devuelve null cuando el valor está vacío.
    private static String limpiarONull(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }

    private static boolean esEncuestador(String rol) {
        return rol != null && "ENCUESTADOR".equalsIgnoreCase(rol.trim());
    }

    /**
     * Valida el acceso recibiendo un objeto Usuario.
     */
    public boolean validarUsuario(Usuario usuario) {
        if (usuario == null) {
            return false;
        }

        return validarUsuario(
                usuario.getDocumentoIdentidad(),
                usuario.getContrasenna()
        );
    }

    /**
     * Valida el documento y la contraseña mediante FN_VALIDAR_USUARIO.
     *
     * La función devuelve 1 si las credenciales son correctas
     * y 0 si no existe coincidencia.
     */
    public boolean validarUsuario(String documento, String contrasenna) {

        String llamada = "{? = call FN_VALIDAR_USUARIO(?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                System.err.println("No se pudo conectar a la base de datos.");
                return false;
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.registerOutParameter(1, Types.NUMERIC);
                cs.setString(2, documento);
                cs.setString(3, contrasenna);

                cs.execute();

                return cs.getInt(1) == 1;
            }

        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Autentica al usuario mediante PR_AUTENTICAR_USUARIO.
     *
     * Devuelve los datos necesarios para iniciar la sesión.
     * Si las credenciales no coinciden, devuelve null.
     */
    public Usuario autenticar(String documento, String contrasenna) {

        String llamada = "{call PR_AUTENTICAR_USUARIO(?, ?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                System.err.println("No se pudo conectar a la base de datos.");
                return null;
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, documento);
                cs.setString(2, contrasenna);

                cs.registerOutParameter(3, OracleTypes.CURSOR);

                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(3)) {

                    if (rs != null && rs.next()) {

                        Usuario usuario = new Usuario();

                        usuario.setIdUsuario(
                                limpiar(rs.getString("ID_USUARIO"))
                        );

                        usuario.setDocumentoIdentidad(
                                limpiar(rs.getString("DOCUMENTO_IDENTIDAD"))
                        );

                        usuario.setNombreCompleto(
                                limpiar(rs.getString("NOMBRE_COMPLETO"))
                        );

                        usuario.setRol(
                                limpiar(rs.getString("ROL"))
                        );

                        usuario.setIdEncuestador(
                                limpiarONull(rs.getString("ID_ENCUESTADOR"))
                        );

                        usuario.setCiudad(
                                limpiarONull(rs.getString("NOMBRE_CIUDAD"))
                        );

                        usuario.setDepartamento(
                                limpiarONull(rs.getString("NOMBRE_DEPARTAMENTO"))
                        );

                        return usuario;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Lista todos los usuarios mediante PR_LISTAR_USUARIOS.
     */
    public List<Usuario> listarUsuarios() {

        String llamada = "{call PR_LISTAR_USUARIOS(?)}";

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection con = Conexion.init()) {

            if (con == null) {
                System.err.println("No se pudo conectar a la base de datos.");
                return usuarios;
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.registerOutParameter(1, OracleTypes.CURSOR);

                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(1)) {

                    while (rs != null && rs.next()) {

                        Usuario usuario = new Usuario();

                        usuario.setIdUsuario(
                                limpiar(rs.getString("ID_USUARIO"))
                        );

                        usuario.setNombreCompleto(
                                limpiar(rs.getString("NOMBRE_COMPLETO"))
                        );

                        usuario.setDocumentoIdentidad(
                                limpiar(rs.getString("DOCUMENTO_IDENTIDAD"))
                        );

                        usuario.setRol(
                                limpiar(rs.getString("ROL"))
                        );

                        usuario.setIdEncuestador(
                                limpiarONull(rs.getString("ID_ENCUESTADOR"))
                        );

                        usuarios.add(usuario);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
            e.printStackTrace();
        }

        return usuarios;
    }

    /**
     * Busca un usuario por su ID mediante PR_BUSCAR_USUARIO.
     */
    public Usuario buscarPorId(String idUsuario) {

        String llamada = "{call PR_BUSCAR_USUARIO(?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                System.err.println("No se pudo conectar a la base de datos.");
                return null;
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, idUsuario);
                cs.registerOutParameter(2, OracleTypes.CURSOR);

                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {

                    if (rs != null && rs.next()) {

                        Usuario usuario = new Usuario();

                        usuario.setIdUsuario(
                                limpiar(rs.getString("ID_USUARIO"))
                        );

                        usuario.setNombreCompleto(
                                limpiar(rs.getString("NOMBRE_COMPLETO"))
                        );

                        usuario.setDocumentoIdentidad(
                                limpiar(rs.getString("DOCUMENTO_IDENTIDAD"))
                        );

                        usuario.setContrasenna(
                                limpiar(rs.getString("CONTRASENNA"))
                        );

                        usuario.setRol(
                                limpiar(rs.getString("ROL"))
                        );

                        usuario.setIdEncuestador(
                                limpiarONull(rs.getString("ID_ENCUESTADOR"))
                        );

                        usuario.setIdCiudad(
                                limpiar(rs.getString("ID_CIUDAD"))
                        );

                        usuario.setTelefono(
                                limpiar(rs.getString("TELEFONO"))
                        );

                        return usuario;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Lista las ciudades mediante PR_LISTAR_CIUDADES.
     *
     * Cada ItemCiudad almacena el ID de la ciudad y el texto
     * que se muestra en el combo.
     */
    public List<ItemCiudad> listarCiudades() {

        String llamada = "{call PR_LISTAR_CIUDADES(?)}";

        List<ItemCiudad> ciudades = new ArrayList<>();

        try (Connection con = Conexion.init()) {

            if (con == null) {
                System.err.println("No se pudo conectar a la base de datos.");
                return ciudades;
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.registerOutParameter(1, OracleTypes.CURSOR);

                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(1)) {

                    while (rs != null && rs.next()) {

                        ciudades.add(
                                new ItemCiudad(
                                        limpiar(rs.getString("ID_CIUDAD")),
                                        limpiar(rs.getString("CIUDAD_DEPARTAMENTO"))
                                )
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar ciudades: " + e.getMessage());
            e.printStackTrace();
        }

        return ciudades;
    }

    /**
     * Verifica si un documento ya pertenece a algún usuario.
     *
     * Se utiliza principalmente al crear un usuario.
     * Si no se puede verificar la existencia, devuelve true
     * para impedir que se continúe con el registro.
     */
    public boolean existeDocumento(String documento) {

        String llamada = "{? = call FN_EXISTE_DOCUMENTO(?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                System.err.println("No se pudo conectar a la base de datos.");
                return true;
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.registerOutParameter(1, Types.NUMERIC);
                cs.setString(2, documento);
                cs.setNull(3, Types.VARCHAR);

                cs.execute();

                return cs.getInt(1) == 1;
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar documento: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }

    /**
     * Verifica si otro usuario tiene el mismo documento.
     *
     * P_ID_EXCLUIDO permite ignorar el usuario que se está editando.
     */
    public boolean existeDocumento(String documento, String idExcluido) {

        String llamada = "{? = call FN_EXISTE_DOCUMENTO(?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                System.err.println("No se pudo conectar a la base de datos.");
                return false;
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.registerOutParameter(1, Types.NUMERIC);
                cs.setString(2, documento);

                if (idExcluido == null) {
                    cs.setNull(3, Types.VARCHAR);
                } else {
                    cs.setString(3, idExcluido);
                }

                cs.execute();

                return cs.getInt(1) == 1;
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar documento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza un usuario mediante PR_ACTUALIZAR_USUARIO.
     *
     * El procedimiento también administra el registro de ENCUESTADOR
     * y libera los hogares cuando el usuario pasa a administrador.
     */
    public boolean actualizarUsuario(Usuario usuario) {

        String llamada = "{call PR_ACTUALIZAR_USUARIO(?, ?, ?, ?, ?, ?, ?, ?)}";

        Connection con = Conexion.init();

        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            con.setAutoCommit(false);

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, usuario.getIdUsuario());
                cs.setString(2, usuario.getNombreCompleto());
                cs.setString(3, usuario.getDocumentoIdentidad());
                cs.setString(4, usuario.getContrasenna());
                cs.setString(5, usuario.getRol());
                cs.setString(6, usuario.getTelefono());
                cs.setString(7, usuario.getIdCiudad());

                cs.registerOutParameter(8, Types.NUMERIC);

                cs.execute();

                int resultado = cs.getInt(8);

                if (resultado == 1) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            }

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException ignorada) {
                System.err.println("Error al deshacer la actualización.");
            }

            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();

            return false;

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (SQLException ignorada) {
                System.err.println("Error al restaurar la transacción.");
            }

            Conexion.close(con);
        }
    }

    /**
     * Elimina un usuario mediante PR_ELIMINAR_USUARIO.
     *
     * El procedimiento libera los hogares asignados al encuestador,
     * elimina su registro y finalmente elimina el usuario.
     */
    public boolean eliminarUsuario(String idUsuario) {

        String llamada = "{call PR_ELIMINAR_USUARIO(?, ?)}";

        Connection con = Conexion.init();

        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            con.setAutoCommit(false);

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, idUsuario);
                cs.registerOutParameter(2, Types.NUMERIC);

                cs.execute();

                int resultado = cs.getInt(2);

                if (resultado == 1) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            }

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException ignorada) {
                System.err.println("Error al deshacer la eliminación.");
            }

            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();

            return false;

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (SQLException ignorada) {
                System.err.println("Error al restaurar la transacción.");
            }

            Conexion.close(con);
        }
    }

    /**
     * Inserta un usuario mediante PR_INSERTAR_USUARIO.
     *
     * Si el rol es ENCUESTADOR, el procedimiento también crea
     * el registro correspondiente en ENCUESTADOR.
     */
    public boolean insertarUsuario(Usuario usuario) {

        String llamada = "{call PR_INSERTAR_USUARIO(?, ?, ?, ?, ?, ?, ?, ?)}";

        Connection con = Conexion.init();

        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            con.setAutoCommit(false);

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, usuario.getDocumentoIdentidad());
                cs.setString(2, usuario.getContrasenna());
                cs.setString(3, usuario.getRol());
                cs.setString(4, usuario.getNombreCompleto());
                cs.setString(5, usuario.getTelefono());
                cs.setString(6, usuario.getIdCiudad());

                cs.registerOutParameter(7, Types.VARCHAR);
                cs.registerOutParameter(8, Types.NUMERIC);

                cs.execute();

                String idUsuario = cs.getString(7);
                int resultado = cs.getInt(8);

                if (resultado == 1) {
                    con.commit();
                    System.out.println("Usuario creado con ID: " + idUsuario);
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            }

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException ignorada) {
                System.err.println("Error al deshacer la inserción.");
            }

            System.err.println("Error al insertar usuario: " + e.getMessage());
            e.printStackTrace();

            return false;

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (SQLException ignorada) {
                System.err.println("Error al restaurar la transacción.");
            }

            Conexion.close(con);
        }
    }
}