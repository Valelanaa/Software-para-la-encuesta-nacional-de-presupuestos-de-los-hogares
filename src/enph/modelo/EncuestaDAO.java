
package enph.modelo;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 * Acceso a datos de la tabla ENCUESTA.
 *
 * Las operaciones se realizan mediante procedimientos
 * y funciones almacenados en Oracle.
 */
public class EncuestaDAO {

    // Valores de ENCUESTA.ESTADO
    public static final String ESTADO_COMPLETA = "0";
    public static final String ESTADO_INCOMPLETA = "1";

    /**
     * Indica si un hogar ya tiene una encuesta registrada.
     *
     * Invoca FN_HOGAR_TIENE_ENCUESTA.
     */
    public boolean hogarTieneEncuesta(int idHogar) throws SQLException {

        String sql = "{? = call FN_HOGAR_TIENE_ENCUESTA(?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException(
                        "No fue posible conectar con la base de datos."
                );
            }

            try (CallableStatement cs = con.prepareCall(sql)) {

                cs.registerOutParameter(1, Types.NUMERIC);
                cs.setInt(2, idHogar);

                cs.execute();

                return cs.getInt(1) == 1;
            }
        }
    }

    /**
     * Obtiene el ID de la encuesta más reciente de un hogar.
     *
     * Invoca FN_OBTENER_ID_ENCUESTA.
     *
     * Devuelve -1 si no existe o ocurre un error.
     */
    public int obtenerIdEncuesta(int idHogar) throws SQLException {

        String sql = "{? = call FN_OBTENER_ID_ENCUESTA(?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException(
                        "No fue posible conectar con la base de datos."
                );
            }

            try (CallableStatement cs = con.prepareCall(sql)) {

                cs.registerOutParameter(1, Types.NUMERIC);
                cs.setInt(2, idHogar);

                cs.execute();

                int resultado = cs.getInt(1);

                return cs.wasNull() ? -1 : resultado;
            }
        }
    }

    /**
     * Elimina los detalles, caracterizaciones y encuestas
     * pertenecientes a un hogar.
     *
     * Invoca PR_ELIMINAR_ENCUESTAS_HOGAR.
     */
    public boolean eliminarEncuestasDelHogar(int idHogar)
            throws SQLException {

        String sql = "{call PR_ELIMINAR_ENCUESTAS_HOGAR(?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException(
                        "No fue posible conectar con la base de datos."
                );
            }

            con.setAutoCommit(false);

            try (CallableStatement cs = con.prepareCall(sql)) {

                cs.setInt(1, idHogar);
                cs.registerOutParameter(2, Types.NUMERIC);

                cs.execute();

                int resultado = cs.getInt(2);

                if (resultado == 1) {
                    con.commit();
                    return true;
                }

                con.rollback();
                return false;

            } catch (SQLException e) {

                try {
                    con.rollback();
                } catch (SQLException errorRollback) {
                    e.addSuppressed(errorRollback);
                }

                throw e;
            }
        }
    }

    /**
     * Crea una encuesta para un hogar.
     *
     * Invoca PR_CREAR_ENCUESTA.
     *
     * Devuelve el ID generado o -1 si ocurre un error.
     */
    public int crearEncuesta(int idHogar) throws SQLException {

        String sql = "{call PR_CREAR_ENCUESTA(?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException(
                        "No fue posible conectar con la base de datos."
                );
            }

            try (CallableStatement cs = con.prepareCall(sql)) {

                cs.setInt(1, idHogar);
                cs.registerOutParameter(2, Types.NUMERIC);

                cs.execute();

                int resultado = cs.getInt(2);

                return cs.wasNull() ? -1 : resultado;
            }
        }
    }

    /**
     * Actualiza la fecha de finalización de una encuesta.
     *
     * Invoca PR_FINALIZAR_ENCUESTA.
     */
    public void finalizarEncuesta(int idEncuesta) throws SQLException {

        if (idEncuesta <= 0) {
            throw new SQLException("El identificador de la encuesta no es válido.");
        }

        String sql = "{call PR_FINALIZAR_ENCUESTA(?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.setInt(1, idEncuesta);
                cs.execute();
            }
        }
    }

    /**
     * Elimina una encuesta únicamente si no tiene
     * caracterizaciones registradas.
     *
     * Invoca PR_ELIMINAR_ENCUESTA_VACIA.
     */
    public boolean eliminarEncuestaVacia(int idEncuesta) throws SQLException {

        if (idEncuesta <= 0) {
            throw new SQLException("El identificador de la encuesta no es válido.");
        }

        String sql = "{call PR_ELIMINAR_ENCUESTA_VACIA(?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(sql)) {

                cs.setInt(1, idEncuesta);
                cs.registerOutParameter(2, Types.NUMERIC);

                cs.execute();

                return cs.getInt(2) == 1;
            }
        }
    }

    /**
     * Marca una encuesta como incompleta.
     *
     * Administrador:
     * Invoca PR_MARCAR_ENCUESTA_INCOMPLETA.
     *
     * Encuestador:
     * Invoca PR_ACTUALIZAR_ESTADO_ENCUESTA.
     */
    public boolean marcarIncompleta(int idEncuesta) throws SQLException {

        if (idEncuesta <= 0) {
            throw new SQLException("El identificador de la encuesta no es válido.");
        }

        Usuario usuario = SesionUsuario.getActual();

        if (usuario == null || usuario.getRol() == null) {
            throw new SQLException("No hay una sesión activa.");
        }

        String rol = usuario.getRol().trim();

        if ("ADMINISTRADOR".equalsIgnoreCase(rol)) {
            return marcarIncompletaAdministrador(idEncuesta);
        }

        if ("ENCUESTADOR".equalsIgnoreCase(rol)) {
            return actualizarMarca(idEncuesta, ESTADO_INCOMPLETA);
        }

        throw new SQLException("El usuario no tiene permisos para esta operación.");
    }

    private boolean marcarIncompletaAdministrador(int idEncuesta)
            throws SQLException {

        String sql = "{call PR_MARCAR_ENCUESTA_INCOMPLETA(?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(sql)) {

                cs.setInt(1, idEncuesta);
                cs.registerOutParameter(2, Types.NUMERIC);

                cs.execute();

                return cs.getInt(2) == 1;
            }
        }
    }

    public void quitarMarcaIncompleta(int idEncuesta) throws SQLException {

        if (idEncuesta <= 0) {
            throw new SQLException("El identificador de la encuesta no es válido.");
        }

        Usuario usuario = SesionUsuario.getActual();

        if (usuario == null || usuario.getRol() == null
                || !"ENCUESTADOR".equalsIgnoreCase(usuario.getRol().trim())) {

            throw new SQLException(
                    "Esta operación solo está permitida para el encuestador."
            );
        }

        actualizarMarca(idEncuesta, ESTADO_COMPLETA);
    }

    private boolean actualizarMarca(int idEncuesta, String estado)
            throws SQLException {

        String sql = "{call PR_ACTUALIZAR_ESTADO_ENCUESTA(?, ?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(sql)) {

                cs.setInt(1, idEncuesta);
                cs.setString(2, estado);
                cs.registerOutParameter(3, Types.NUMERIC);

                cs.execute();

                return cs.getInt(3) == 1;
            }
        }
    }
    // ==================== BUSCAR (devuelven Encuesta) ====================

    /**
     * Busca una encuesta por su identificador.
     * Devuelve null únicamente si no existe.
     */
    public Encuesta buscarPorId(int idEncuesta) throws SQLException {
        if (idEncuesta <= 0) {
            throw new SQLException("El identificador de la encuesta no es válido.");
        }

        return buscarEncuesta("{call PR_BUSCAR_ENCUESTA(?,?)}", idEncuesta);
    }

    /**
     * Busca la encuesta más reciente de un hogar.
     * Devuelve null únicamente si no existe.
     */
    public Encuesta buscarPorHogar(int idHogar) throws SQLException {
        if (idHogar <= 0) {
            throw new SQLException("El identificador del hogar no es válido.");
        }

        return buscarEncuesta("{call PR_BUSCAR_ENCUESTA_HOGAR(?,?)}", idHogar);
    }

    /**
     * Ejecuta la consulta correspondiente y convierte el resultado.
     */
    private Encuesta buscarEncuesta(String llamada, int id) throws SQLException {

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException(
                    "No fue posible conectar con la base de datos."
                );
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, String.valueOf(id));
                cs.registerOutParameter(2, Types.REF_CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                    if (rs != null && rs.next()) {
                        return obtenerEncuesta(rs);
                    }
                }
            }
        }

        return null;
    }

 
    /** Convierte una fila de ENCUESTA en un objeto Encuesta (estado true = completa). */
    private Encuesta obtenerEncuesta(ResultSet rs) throws SQLException {
 
        Hogar hogar = new Hogar(
            Integer.parseInt(rs.getString("ID_HOGAR").trim()),
            null, null, null
        );
 
        String estado = rs.getString("ESTADO");
 
        return new Encuesta(
            Integer.parseInt(rs.getString("ID_ENCUESTA").trim()),
            rs.getTimestamp("FECHA_INICIO"),
            rs.getTimestamp("FECHA_FIN"),
            estado != null && ESTADO_COMPLETA.equals(estado.trim()),
            hogar
        );
    }
}