
package enph.modelo;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CatalogoDAO {

    // ==================== LISTAR CATÁLOGOS ====================


    public List<GrupoAlimento> listarGrupos() throws SQLException {

        List<GrupoAlimento> grupos = new ArrayList<>();

        for (Object[] fila : listar("{call PR_LISTAR_GRUPOS(?)}")) {
            grupos.add(new GrupoAlimento(
                    (Integer) fila[0],
                    (String) fila[1]
            ));
        }

        return grupos;
    }

    public List<FrecuenciaCompra> listarFrecuencias() throws SQLException {

        List<FrecuenciaCompra> frecuencias = new ArrayList<>();

        for (Object[] fila : listar("{call PR_LISTAR_FRECUENCIAS(?)}")) {
            frecuencias.add(new FrecuenciaCompra(
                    (Integer) fila[0],
                    (String) fila[1]
            ));
        }

        return frecuencias;
    }

    /**
     * Ejecuta una consulta de catálogo.
     * Devuelve una lista vacía si la consulta no encuentra registros.
     * Propaga los errores de conexión y SQL.
     */
    private List<Object[]> listar(String llamada) throws SQLException {

        List<Object[]> lista = new ArrayList<>();

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException(
                        "No fue posible conectar con la base de datos."
                );
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.registerOutParameter(1, Types.REF_CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(1)) {

                    while (rs != null && rs.next()) {

                        String nombre = rs.getString(2);

                        lista.add(new Object[]{
                            rs.getInt(1),
                            nombre == null ? "" : nombre.trim()
                        });
                    }
                }
            }
        }

        return lista;
    }


    // ==================== GRUPOS DE ALIMENTOS ====================

    public boolean crearGrupo(String nombre) {
        return ejecutarNombre("{call PR_CREAR_GRUPO(?,?)}", nombre);
    }

    public boolean editarGrupo(int idGrupo, String nombre) {
        return ejecutarIdNombre("{call PR_EDITAR_GRUPO(?,?,?)}", idGrupo, nombre);
    }

    public boolean grupoEnUso(int idGrupo) {
        return consultarUso("{? = call FN_GRUPO_EN_USO(?)}", idGrupo);
    }

    public boolean eliminarGrupo(int idGrupo) {
        return ejecutarId("{call PR_ELIMINAR_GRUPO(?,?)}", idGrupo);
    }

    // ==================== FRECUENCIAS DE COMPRA ====================

    public boolean crearFrecuencia(String nombre) {
        return ejecutarNombre("{call PR_CREAR_FRECUENCIA(?,?)}", nombre);
    }

    public boolean editarFrecuencia(int idFrecuencia, String nombre) {
        return ejecutarIdNombre("{call PR_EDITAR_FRECUENCIA(?,?,?)}", idFrecuencia, nombre);
    }

    public boolean frecuenciaEnUso(int idFrecuencia) {
        return consultarUso("{? = call FN_FRECUENCIA_EN_USO(?)}", idFrecuencia);
    }

    public boolean eliminarFrecuencia(int idFrecuencia) {
        return ejecutarId("{call PR_ELIMINAR_FRECUENCIA(?,?)}", idFrecuencia);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    // Ejecuta procedimientos que reciben un nombre y devuelven un resultado.
    private boolean ejecutarNombre(String llamada, String nombre) {
        Connection con = Conexion.init();
        if (con == null) return false;

        try (CallableStatement cs = con.prepareCall(llamada)) {
            con.setAutoCommit(false);
            cs.setString(1, nombre == null ? "" : nombre.trim());
            cs.registerOutParameter(2, Types.NUMERIC);
            cs.execute();

            boolean resultado = cs.getInt(2) == 1;
            if (resultado) {
                con.commit();
            } else {
                con.rollback();
            }
            return resultado;
        } catch (SQLException e) {
            System.err.println("Error en la operación del catálogo: " + e.getMessage());
            try { con.rollback(); } catch (SQLException ex) { }
            return false;
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { }
            Conexion.close(con);
        }
    }

    // Ejecuta procedimientos que reciben un ID y un nombre.
    private boolean ejecutarIdNombre(String llamada, int id, String nombre) {
        Connection con = Conexion.init();
        if (con == null) return false;

        try (CallableStatement cs = con.prepareCall(llamada)) {
            con.setAutoCommit(false);
            cs.setInt(1, id);
            cs.setString(2, nombre == null ? "" : nombre.trim());
            cs.registerOutParameter(3, Types.NUMERIC);
            cs.execute();

            boolean resultado = cs.getInt(3) == 1;
            if (resultado) {
                con.commit();
            } else {
                con.rollback();
            }
            return resultado;
        } catch (SQLException e) {
            System.err.println("Error al modificar el catálogo: " + e.getMessage());
            try { con.rollback(); } catch (SQLException ex) { }
            return false;
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { }
            Conexion.close(con);
        }
    }

    // Ejecuta procedimientos que reciben un ID y devuelven un resultado.
    private boolean ejecutarId(String llamada, int id) {
        Connection con = Conexion.init();
        if (con == null) return false;

        try (CallableStatement cs = con.prepareCall(llamada)) {
            con.setAutoCommit(false);
            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.NUMERIC);
            cs.execute();

            boolean resultado = cs.getInt(2) == 1;
            if (resultado) {
                con.commit();
            } else {
                con.rollback();
            }
            return resultado;
        } catch (SQLException e) {
            System.err.println("Error al eliminar del catálogo: " + e.getMessage());
            try { con.rollback(); } catch (SQLException ex) { }
            return false;
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { }
            Conexion.close(con);
        }
    }

    // Ejecuta funciones que indican si un registro está siendo utilizado.
    private boolean consultarUso(String llamada, int id) {
        Connection con = Conexion.init();
        if (con == null) return true;

        try (CallableStatement cs = con.prepareCall(llamada)) {
            cs.registerOutParameter(1, Types.NUMERIC);
            cs.setInt(2, id);
            cs.execute();
            return cs.getInt(1) == 1;
        } catch (SQLException e) {
            System.err.println("Error al consultar el uso del catálogo: " + e.getMessage());
            return true;
        } finally {
            Conexion.close(con);
        }
    }
}