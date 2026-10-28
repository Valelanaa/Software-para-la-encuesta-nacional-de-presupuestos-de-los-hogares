/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 * Acceso a datos de los catálogos de la encuesta: los grupos de alimentos
 * y las frecuencias de compra. Ambos se leen de la base de datos, no están
 * escritos en el código.
 * @author SALA-2
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CatalogoDAO {

    //Lista los grupos de alimentos (los 25 de la encuesta) en orden por id.
    //Cada ItemCatalogo trae el id y el nombre del grupo.
    public List<ItemCatalogo> listarGrupos() {
        return listar("SELECT ID_GRUPO_ALIMENTO, NOMBRE_GRUPO_ALIMENTO "
        + "FROM GRUPO_ALIMENTO ORDER BY ID_GRUPO_ALIMENTO");
    }

    /**
     * Lista las frecuencias de compra (Nunca, Diario, Semanal, etc.)
     * en orden por id. Es lo que se muestra en el combo de FrecuenciaGasto.
     */
    
    public List<ItemCatalogo> listarFrecuencias() {
        return listar("SELECT ID_FRECUENCIA_COMPRA, NOMBRE_FRECUENCIA_COMPRA "
        + "FROM FRECUENCIA_COMPRA ORDER BY ID_FRECUENCIA_COMPRA");
    }

    //Ejecuta una consulta de catálogo y convierte cada fila en un ItemCatalogo.
    // La consulta debe devolver dos columnas: primero el id y después el nombre.
    // Así los dos catálogos comparten el mismo código de lectura.
    
    private List<ItemCatalogo> listar(String sql) {
        List<ItemCatalogo> lista = new ArrayList<>();
        Connection con = Conexion.init();

        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return lista;
        }
        // try-with-resources: el PreparedStatement y el ResultSet se cierran solos
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // trim() quita espacios sobrantes (las columnas CHAR los rellenan)
                lista.add(new ItemCatalogo(rs.getInt(1), rs.getString(2).trim()));
            }

        } catch (SQLException e) {
            System.err.println("Error al leer el catálogo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Siempre se cierra la conexión, haya o no error
            Conexion.close(con);
        }
        return lista;
    }

    // ===== Gestión de grupos de alimentos (pantalla GestionGruposAlimentos) =====

    // Crea un grupo nuevo; el id es el mayor existente + 1 (igual que en UsuarioDAO).
    public boolean crearGrupo(String nombre) {
        String sqlId = "SELECT NVL(MAX(TO_NUMBER(ID_GRUPO_ALIMENTO)), 0) + 1 FROM GRUPO_ALIMENTO";
        String sqlIns = "INSERT INTO GRUPO_ALIMENTO (ID_GRUPO_ALIMENTO, NOMBRE_GRUPO_ALIMENTO) VALUES (?, ?)";
        Connection con = Conexion.init();
        if (con == null) return false;
        try (PreparedStatement psId = con.prepareStatement(sqlId);
             ResultSet rs = psId.executeQuery()) {
            rs.next();
            int nuevoId = rs.getInt(1);
            try (PreparedStatement ps = con.prepareStatement(sqlIns)) {
                ps.setInt(1, nuevoId);
                ps.setString(2, nombre);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al crear el grupo: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Conexion.close(con);
        }
    }

    // Cambia el nombre de un grupo existente.
    public boolean editarGrupo(int idGrupo, String nombre) {
        String sql = "UPDATE GRUPO_ALIMENTO SET NOMBRE_GRUPO_ALIMENTO = ? WHERE ID_GRUPO_ALIMENTO = ?";
        Connection con = Conexion.init();
        if (con == null) return false;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, idGrupo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar el grupo: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Conexion.close(con);
        }
    }

    // Indica si el grupo ya tiene respuestas guardadas en alguna encuesta.
    public boolean grupoEnUso(int idGrupo) {
        String sql = "SELECT COUNT(*) FROM CARACTERIZACION_COMPRA WHERE ID_GRUPO_ALIMENTO = ?";
        Connection con = Conexion.init();
        if (con == null) return true; // ante la duda no se permite borrar
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar el grupo: " + e.getMessage());
            e.printStackTrace();
            return true;
        } finally {
            Conexion.close(con);
        }
    }

    // Elimina un grupo (solo debe llamarse si grupoEnUso devolvió false).
    public boolean eliminarGrupo(int idGrupo) {
        String sql = "DELETE FROM GRUPO_ALIMENTO WHERE ID_GRUPO_ALIMENTO = ?";
        Connection con = Conexion.init();
        if (con == null) return false;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el grupo: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Conexion.close(con);
        }
    }
    
    // ===== Gestión de frecuencias de compra =====

    /**
     * Crea una frecuencia nueva.
     * El ID se calcula como el mayor existente más uno.
     */
    public boolean crearFrecuencia(String nombre) {
        String sqlId = "SELECT NVL(MAX(TO_NUMBER(ID_FRECUENCIA_COMPRA)), 0) + 1 "
                     + "FROM FRECUENCIA_COMPRA";

        String sqlIns = "INSERT INTO FRECUENCIA_COMPRA "
                      + "(ID_FRECUENCIA_COMPRA, NOMBRE_FRECUENCIA_COMPRA) "
                      + "VALUES (?, ?)";

        Connection con = Conexion.init();

        if (con == null) {
            return false;
        }

        try (PreparedStatement psId = con.prepareStatement(sqlId);
             ResultSet rs = psId.executeQuery()) {

            if (!rs.next()) {
                return false;
            }

            int nuevoId = rs.getInt(1);

            try (PreparedStatement ps = con.prepareStatement(sqlIns)) {
                ps.setInt(1, nuevoId);
                ps.setString(2, nombre.trim());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al crear la frecuencia: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Conexion.close(con);
        }
    }

    /**
     * Modifica el nombre de una frecuencia existente.
     */
    public boolean editarFrecuencia(int idFrecuencia, String nombre) {
        String sql = "UPDATE FRECUENCIA_COMPRA "
                   + "SET NOMBRE_FRECUENCIA_COMPRA = ? "
                   + "WHERE ID_FRECUENCIA_COMPRA = ?";

        Connection con = Conexion.init();

        if (con == null) {
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre.trim());
            ps.setInt(2, idFrecuencia);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al editar la frecuencia: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Conexion.close(con);
        }
    }

    /**
     * Comprueba si la frecuencia está siendo utilizada.
     * Si no se puede comprobar, impide el borrado por seguridad.
     */
    public boolean frecuenciaEnUso(int idFrecuencia) {
        String sql = "SELECT COUNT(*) "
                   + "FROM CARACTERIZACION_COMPRA "
                   + "WHERE ID_FRECUENCIA_COMPRA = ?";

        Connection con = Conexion.init();

        if (con == null) {
            return true;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFrecuencia);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar el uso de la frecuencia: "
                             + e.getMessage());
            e.printStackTrace();
            return true;
        } finally {
            Conexion.close(con);
        }
    }

    /**
     * Elimina una frecuencia que no esté siendo utilizada.
     */
    public boolean eliminarFrecuencia(int idFrecuencia) {
        String sql = "DELETE FROM FRECUENCIA_COMPRA "
                   + "WHERE ID_FRECUENCIA_COMPRA = ?";

        Connection con = Conexion.init();

        if (con == null) {
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFrecuencia);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar la frecuencia: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Conexion.close(con);
        }
    }

}