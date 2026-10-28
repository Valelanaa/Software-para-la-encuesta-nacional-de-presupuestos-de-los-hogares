/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CabezaHogarDAO {

    private static String limpiar(String s) {
        return s == null ? "" : s.trim();
    }

    public CabezaHogar buscarPorHogar(String idHogar) {

    String sql = "SELECT ID_CABEZA_DE_HOGAR, ID_HOGAR, "
               + "NOMBRE_COMPLETO, DOCUMENTO_IDENTIDAD, "
               + "CORREO_ELECTRONICO, TELEFONO "
               + "FROM CABEZA_DE_HOGAR "
               + "WHERE TRIM(ID_HOGAR) = TRIM(?)";

    Connection con = Conexion.init();

    if (con == null) {
        System.err.println("No se pudo conectar a la base de datos.");
        return null;
    }

    try (PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, idHogar);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                CabezaHogar c = new CabezaHogar();

                c.setIdCabezaHogar(
                    limpiar(rs.getString("ID_CABEZA_DE_HOGAR"))
                );

                c.setIdHogar(
                    limpiar(rs.getString("ID_HOGAR"))
                );

                c.setNombreCompleto(
                    limpiar(rs.getString("NOMBRE_COMPLETO"))
                );

                c.setDocumentoIdentidad(
                    limpiar(rs.getString("DOCUMENTO_IDENTIDAD"))
                );

                c.setCorreo(
                    limpiar(rs.getString("CORREO_ELECTRONICO"))
                );

                c.setTelefono(
                    limpiar(rs.getString("TELEFONO"))
                );

                return c;
            }
        }

    } catch (SQLException e) {
        System.err.println(
            "Error al buscar la cabeza de hogar: " + e.getMessage()
        );
        e.printStackTrace();

    } finally {
        Conexion.close(con);
    }

    return null;
}

    public boolean existeDocumento(String documento, String idExcluido) {
        String sql = "SELECT COUNT(*) FROM CABEZA_DE_HOGAR "
                   + "WHERE TRIM(DOCUMENTO_IDENTIDAD) = TRIM(?) AND ID_CABEZA_DE_HOGAR <> ?";

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

    public boolean actualizar(CabezaHogar c) {
        String sql = "UPDATE CABEZA_DE_HOGAR SET NOMBRE_COMPLETO = ?, DOCUMENTO_IDENTIDAD = ?, "
                   + "CORREO_ELECTRONICO = ?, TELEFONO = ? WHERE ID_CABEZA_DE_HOGAR = ?";

        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombreCompleto());
            ps.setString(2, c.getDocumentoIdentidad());
            ps.setString(3, c.getCorreo());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getIdCabezaHogar());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la cabeza de hogar: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Conexion.close(con);
        }
    }
}
