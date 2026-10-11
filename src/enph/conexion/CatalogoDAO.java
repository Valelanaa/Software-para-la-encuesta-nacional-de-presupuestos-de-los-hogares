/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.conexion;

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
                + "FROM GRUPO_DE_ALIMENTOS ORDER BY ID_GRUPO_ALIMENTO");
    }

    /**
     * Lista las frecuencias de compra (Nunca, Diario, Semanal, etc.)
     * en orden por id. Es lo que se muestra en el combo de FrecuenciaGasto.
     */
    
    public List<ItemCatalogo> listarFrecuencias() {
        return listar("SELECT ID_FRECUENCIA_COMPRA, NOMBRE_FRECUENCIA "
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
}
