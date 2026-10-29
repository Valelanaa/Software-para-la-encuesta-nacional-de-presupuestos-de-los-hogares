package enph.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Acceso a datos de la tabla ENCUESTA: consulta, crea, finaliza y elimina
 * las encuestas de cada hogar (familia).
 */

public class EncuestaDAO {

    /**
     * Indica si el hogar ya tiene al menos una encuesta registrada.
     * Lo usa FamiliasRegistradas para decidir entre iniciar una encuesta
     * nueva o preguntar si se edita o se elimina la existente.
     */
    public boolean hogarTieneEncuesta(int idHogar) {
        String sql = "SELECT COUNT(*) FROM ENCUESTA WHERE ID_HOGAR = ?";

        Connection con = Conexion.init();
        PreparedStatement ps = null;
        ResultSet rs = null;

        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return true; // sin conexión, no dejar iniciar una encuesta nueva
        }

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, idHogar);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar la encuesta del hogar: " + e.getMessage());
            e.printStackTrace();
            return true; // ante un error, tampoco dejar iniciar una encuesta nueva
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

        return false;
    }
    /** Id de la encuesta del hogar, o -1 si no tiene. 
     * Busca el id de la encuesta del hogar (se usa al editar).
     * Si hubiera varias, devuelve la más reciente (la de id mayor).
     */
public int obtenerIdEncuesta(int idHogar) {
    String sql = "SELECT MAX(TO_NUMBER(ID_ENCUESTA)) FROM ENCUESTA WHERE ID_HOGAR = ?";

    Connection con = Conexion.init();
    if (con == null) return -1;

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idHogar);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int id = rs.getInt(1);
                // MAX devuelve NULL si no hay filas; getInt lo vería como 0,
                // por eso se usa wasNull() para distinguirlo de un id real
                return rs.wasNull() ? -1 : id;
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al buscar la encuesta del hogar: " + e.getMessage());
        e.printStackTrace();
    } finally {
        Conexion.close(con);
    }
    return -1;
}

/** Borra las respuestas (CARACTERIZACION_COMPRA) y la encuesta del hogar. 
 *  Se hace en una transacción: o se borra todo, o no se borra nada.
 */
public boolean eliminarEncuestasDelHogar(int idHogar) {
    // Primero las respuestas (tabla hija), porque dependen de la encuesta
    String sqlRespuestas = "DELETE FROM CARACTERIZACION_COMPRA WHERE ID_ENCUESTA IN "
            + "(SELECT ID_ENCUESTA FROM ENCUESTA WHERE ID_HOGAR = ?)";
    // Después la encuesta (tabla padre)
    String sqlEncuesta = "DELETE FROM ENCUESTA WHERE ID_HOGAR = ?";

    Connection con = Conexion.init();
    if (con == null) return false;

    try {
        // Desactiva el guardado automático para agrupar los dos DELETE
        con.setAutoCommit(false);
        try (PreparedStatement a = con.prepareStatement(sqlRespuestas);
             PreparedStatement b = con.prepareStatement(sqlEncuesta)) {
            a.setInt(1, idHogar);
            a.executeUpdate();
            b.setInt(1, idHogar);
            b.executeUpdate();
        }
        con.commit(); // confirma ambos borrados a la vez
        return true;
    } catch (SQLException e) {
         // Si algo falla, se deshace todo y la encuesta queda intacta
        try { con.rollback(); } catch (SQLException ignorada) { }
        System.err.println("Error al eliminar la encuesta: " + e.getMessage());
        e.printStackTrace();
        return false;
    } finally {
        Conexion.close(con);
    }
}
    /**
     * Crea la encuesta del hogar con las fechas del sistema.
     * Devuelve el id generado, o -1 si falla.
     */
    public int crearEncuesta(int idHogar) {
        String sql = "INSERT INTO ENCUESTA (ID_ENCUESTA, FECHA_INICIO, FECHA_FIN, ID_HOGAR) "
                   + "VALUES (?, SYSDATE, SYSDATE, ?)";

        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return -1;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int idEncuesta = siguienteId(con, "ENCUESTA", "ID_ENCUESTA");
            ps.setInt(1, idEncuesta);
            ps.setInt(2, idHogar);
            ps.executeUpdate();
            return idEncuesta;

        } catch (SQLException e) {
            System.err.println("Error al crear la encuesta: " + e.getMessage());
            e.printStackTrace();
            return -1;
        } finally {
            Conexion.close(con);
        }
    }

    /**
     * Al terminar los grupos, actualiza la fecha de fin con la del sistema.
     * También se ejecuta al terminar una edición.
     */
    public void finalizarEncuesta(int idEncuesta) {
        String sql = "UPDATE ENCUESTA SET FECHA_FIN = SYSDATE WHERE ID_ENCUESTA = ?";

        Connection con = Conexion.init();
        if (con == null) return;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEncuesta);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al finalizar la encuesta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexion.close(con);
        }
    }
        /** Borra la encuesta solo si todavía no tiene respuestas guardadas. 
        * Se usa cuando el encuestador sale con "Anterior" desde el primer grupo,
        * para no dejar una encuesta vacía que bloquee a la familia.
        * Si ya tiene respuestas (por ejemplo, en una edición), no borra nada.
        */
    public void eliminarEncuestaVacia(int idEncuesta) {
        String sql = "DELETE FROM ENCUESTA WHERE ID_ENCUESTA = ? "
                   + "AND NOT EXISTS (SELECT 1 FROM CARACTERIZACION_COMPRA WHERE ID_ENCUESTA = ?)";

        Connection con = Conexion.init();
        if (con == null) return;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEncuesta);
            ps.setInt(2, idEncuesta);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar la encuesta vacía: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexion.close(con);
        }
    }
    /**
     * Siguiente id disponible (máximo + 1) usando la misma conexión.
     * NVL cambia el NULL por 0 cuando la tabla está vacía, así el primero es 1.
     * Es de paquete (sin public) porque solo lo usan los DAO de enph.conexion,
     * por ejemplo CaracterizacionDAO.
     */
    static int siguienteId(Connection con, String tabla, String columna) throws SQLException {
        String sql = "SELECT NVL(MAX(TO_NUMBER(" + columna + ")), 0) + 1 FROM " + tabla;
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 1;
        }
    }
        /** Marca la encuesta como incompleta (lo hace el administrador). */
    public boolean marcarIncompleta(int idEncuesta) {
        return actualizarMarca(idEncuesta, 1);
    }

    /** Quita la marca de incompleta (al terminar los grupos que faltaban). */
    public void quitarMarcaIncompleta(int idEncuesta) {
        actualizarMarca(idEncuesta, 0);
    }

    private boolean actualizarMarca(int idEncuesta, int valor) {
        String sql = "UPDATE ENCUESTA SET INCOMPLETA = ? WHERE ID_ENCUESTA = ?";

        Connection con = Conexion.init();
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, valor);
            ps.setInt(2, idEncuesta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la marca de incompleta: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Conexion.close(con);
        }
    }
}