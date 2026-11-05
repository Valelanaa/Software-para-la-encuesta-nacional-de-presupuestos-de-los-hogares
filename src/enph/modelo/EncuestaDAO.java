
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
    public boolean hogarTieneEncuesta(int idHogar) {

        String sql = "{? = call FN_HOGAR_TIENE_ENCUESTA(?)}";

        Connection con = Conexion.init();

        if (con == null) {
            return true;
        }

        try (CallableStatement cs = con.prepareCall(sql)) {

            cs.registerOutParameter(1, Types.NUMERIC);
            cs.setInt(2, idHogar);

            cs.execute();

            return cs.getInt(1) == 1;

        } catch (SQLException e) {

            System.err.println(
                "Error al consultar la encuesta del hogar: "
                + e.getMessage()
            );

            e.printStackTrace();
            return true;

        } finally {
            Conexion.close(con);
        }
    }

    /**
     * Obtiene el ID de la encuesta más reciente de un hogar.
     *
     * Invoca FN_OBTENER_ID_ENCUESTA.
     *
     * Devuelve -1 si no existe o ocurre un error.
     */
    public int obtenerIdEncuesta(int idHogar) {

        String sql = "{? = call FN_OBTENER_ID_ENCUESTA(?)}";

        Connection con = Conexion.init();

        if (con == null) {
            return -1;
        }

        try (CallableStatement cs = con.prepareCall(sql)) {

            cs.registerOutParameter(1, Types.NUMERIC);
            cs.setInt(2, idHogar);

            cs.execute();

            return cs.getInt(1);

        } catch (SQLException e) {

            System.err.println(
                "Error al buscar la encuesta del hogar: "
                + e.getMessage()
            );

            e.printStackTrace();
            return -1;

        } finally {
            Conexion.close(con);
        }
    }

    /**
     * Elimina los detalles, caracterizaciones y encuestas
     * pertenecientes a un hogar.
     *
     * Invoca PR_ELIMINAR_ENCUESTAS_HOGAR.
     */
    public boolean eliminarEncuestasDelHogar(int idHogar) {

        String sql =
            "{call PR_ELIMINAR_ENCUESTAS_HOGAR(?, ?)}";

        Connection con = Conexion.init();

        if (con == null) {
            return false;
        }

        try {

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
            }

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException ex) {
                System.err.println(
                    "Error al revertir la eliminación: "
                    + ex.getMessage()
                );
            }

            System.err.println(
                "Error al eliminar las encuestas del hogar: "
                + e.getMessage()
            );

            e.printStackTrace();
            return false;

        } finally {
            Conexion.close(con);
        }
    }

    /**
     * Crea una encuesta para un hogar.
     *
     * Invoca PR_CREAR_ENCUESTA.
     *
     * Devuelve el ID generado o -1 si ocurre un error.
     */
    public int crearEncuesta(int idHogar) {

        String sql = "{call PR_CREAR_ENCUESTA(?, ?)}";

        Connection con = Conexion.init();

        if (con == null) {
            return -1;
        }

        try (CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idHogar);
            cs.registerOutParameter(2, Types.NUMERIC);

            cs.execute();

            return cs.getInt(2);

        } catch (SQLException e) {

            System.err.println(
                "Error al crear la encuesta: "
                + e.getMessage()
            );

            e.printStackTrace();
            return -1;

        } finally {
            Conexion.close(con);
        }
    }

    /**
     * Actualiza la fecha de finalización de una encuesta.
     *
     * Invoca PR_FINALIZAR_ENCUESTA.
     */
    public void finalizarEncuesta(int idEncuesta) {

        String sql = "{call PR_FINALIZAR_ENCUESTA(?)}";

        Connection con = Conexion.init();

        if (con == null) {
            return;
        }

        try (CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idEncuesta);
            cs.execute();

        } catch (SQLException e) {

            System.err.println(
                "Error al finalizar la encuesta: "
                + e.getMessage()
            );

            e.printStackTrace();

        } finally {
            Conexion.close(con);
        }
    }

    /**
     * Elimina una encuesta únicamente si no tiene
     * caracterizaciones registradas.
     *
     * Invoca PR_ELIMINAR_ENCUESTA_VACIA.
     */
    public void eliminarEncuestaVacia(int idEncuesta) {

        String sql =
            "{call PR_ELIMINAR_ENCUESTA_VACIA(?, ?)}";

        Connection con = Conexion.init();

        if (con == null) {
            return;
        }

        try (CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idEncuesta);
            cs.registerOutParameter(2, Types.NUMERIC);

            cs.execute();

            int resultado = cs.getInt(2);

            if (resultado == 0) {
                System.out.println(
                    "La encuesta no se eliminó porque tiene "
                    + "caracterizaciones o no existe."
                );
            }

        } catch (SQLException e) {

            System.err.println(
                "Error al eliminar la encuesta vacía: "
                + e.getMessage()
            );

            e.printStackTrace();

        } finally {
            Conexion.close(con);
        }
    }

    /**
     * Siguiente ID disponible (máximo + 1).
     *
     * Se conserva para no afectar otros DAO del proyecto
     * que puedan utilizar este método.
     */
    static int siguienteId(
        Connection con,
        String tabla,
        String columna
    ) throws SQLException {

        String sql =
            "SELECT NVL(MAX(TO_NUMBER(" + columna + ")), 0) + 1 "
            + "FROM " + tabla;

        try (
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)
        ) {
            return rs.next() ? rs.getInt(1) : 1;
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
    public boolean marcarIncompleta(int idEncuesta) {

        Usuario usuario = SesionUsuario.getActual();

        if (usuario == null || usuario.getRol() == null) {
            System.err.println("No hay una sesión activa.");
            return false;
        }

        String rol = usuario.getRol().trim();

        if ("ADMINISTRADOR".equalsIgnoreCase(rol)) {

            return marcarIncompletaAdministrador(idEncuesta);

        } else if ("ENCUESTADOR".equalsIgnoreCase(rol)) {

            return actualizarMarca(
                idEncuesta,
                ESTADO_INCOMPLETA
            );
        }

        return false;
    }

    /**
     * Procedimiento exclusivo del administrador.
     *
     * Invoca PR_MARCAR_ENCUESTA_INCOMPLETA.
     */
    private boolean marcarIncompletaAdministrador(int idEncuesta) {

        String sql =
            "{call PR_MARCAR_ENCUESTA_INCOMPLETA(?, ?)}";

        Connection con = Conexion.init();

        if (con == null) {
            return false;
        }

        try (CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idEncuesta);
            cs.registerOutParameter(2, Types.NUMERIC);

            cs.execute();

            return cs.getInt(2) == 1;

        } catch (SQLException e) {

            System.err.println(
                "Error al marcar la encuesta como incompleta: "
                + e.getMessage()
            );

            e.printStackTrace();
            return false;

        } finally {
            Conexion.close(con);
        }
    }

    /**
     * Quita la marca de incompleta.
     *
     * Esta operación corresponde al encuestador.
     */
    public void quitarMarcaIncompleta(int idEncuesta) {

        Usuario usuario = SesionUsuario.getActual();

        if (usuario == null
                || usuario.getRol() == null
                || !usuario.getRol().trim()
                    .equalsIgnoreCase("ENCUESTADOR")) {

            System.err.println(
                "Esta operación solo está permitida para el encuestador."
            );

            return;
        }

        actualizarMarca(
            idEncuesta,
            ESTADO_COMPLETA
        );
    }

    /**
     * Actualiza el estado de una encuesta.
     *
     * Invoca PR_ACTUALIZAR_ESTADO_ENCUESTA.
     *
     * Se utiliza para las operaciones del encuestador.
     */
    private boolean actualizarMarca(
        int idEncuesta,
        String estado
    ) {

        String sql =
            "{call PR_ACTUALIZAR_ESTADO_ENCUESTA(?, ?, ?)}";

        Connection con = Conexion.init();

        if (con == null) {
            return false;
        }

        try (CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idEncuesta);
            cs.setString(2, estado);
            cs.registerOutParameter(3, Types.NUMERIC);

            cs.execute();

            return cs.getInt(3) == 1;

        } catch (SQLException e) {

            System.err.println(
                "Error al actualizar el estado de la encuesta: "
                + e.getMessage()
            );

            e.printStackTrace();
            return false;

        } finally {
            Conexion.close(con);
        }
    }
}