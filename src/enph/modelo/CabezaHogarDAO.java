
package enph.modelo;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class CabezaHogarDAO {

    private static String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
    }

    /**
     * Busca la cabeza de hogar asociada a un hogar.
     */
    public CabezaHogar buscarPorHogar(String idHogar) {
        Connection con = Conexion.init();
        if (con == null) return null;

        try (CallableStatement cs = con.prepareCall("{CALL PR_BUSCAR_CABEZA_HOGAR(?,?)}")) {
            cs.setString(1, limpiar(idHogar));
            cs.registerOutParameter(2, Types.REF_CURSOR);
            cs.execute();

            try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                if (rs != null && rs.next()) {
                    CabezaHogar c = new CabezaHogar();
                    c.setIdCabezaHogar(limpiar(rs.getString("ID_CABEZA_DE_HOGAR")));
                    c.setIdHogar(limpiar(rs.getString("ID_HOGAR")));
                    c.setNombreCompleto(limpiar(rs.getString("NOMBRE_COMPLETO")));
                    c.setDocumentoIdentidad(limpiar(rs.getString("DOCUMENTO_IDENTIDAD")));
                    c.setCorreo(limpiar(rs.getString("CORREO_ELECTRONICO")));
                    c.setTelefono(limpiar(rs.getString("TELEFONO")));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar la cabeza de hogar: " + e.getMessage());
        } finally {
            Conexion.close(con);
        }

        return null;
    }

    /**
     * Verifica si un documento ya está registrado.
     */
    public boolean existeDocumento(String documento, String idExcluido) {
        Connection con = Conexion.init();
        if (con == null) return false;

        try (CallableStatement cs = con.prepareCall("{? = CALL FN_EXISTE_DOCUMENTO_CABEZA(?,?)}")) {
            cs.registerOutParameter(1, Types.NUMERIC);
            cs.setString(2, limpiar(documento));

            if (idExcluido == null || idExcluido.trim().isEmpty()) cs.setNull(3, Types.VARCHAR);
            else cs.setString(3, idExcluido.trim());

            cs.execute();
            return cs.getInt(1) == 1;

        } catch (SQLException e) {
            System.err.println("Error al verificar el documento: " + e.getMessage());
            return false;
        } finally {
            Conexion.close(con);
        }
    }

    /**
     * Actualiza los datos de una cabeza de hogar.
     */
    public boolean actualizar(CabezaHogar c) {
        if (c == null) return false;

        Connection con = Conexion.init();
        if (con == null) return false;

        try (CallableStatement cs = con.prepareCall("{CALL PR_ACTUALIZAR_CABEZA_HOGAR(?,?,?,?,?,?)}")) {
            cs.setString(1, limpiar(c.getIdCabezaHogar()));
            cs.setString(2, limpiar(c.getNombreCompleto()));
            cs.setString(3, limpiar(c.getDocumentoIdentidad()));
            cs.setString(4, limpiar(c.getCorreo()));
            cs.setString(5, limpiar(c.getTelefono()));
            cs.registerOutParameter(6, Types.NUMERIC);
            cs.execute();

            return cs.getInt(6) == 1;

        } catch (SQLException e) {
            System.err.println("Error al actualizar la cabeza de hogar: " + e.getMessage());
            return false;
        } finally {
            Conexion.close(con);
        }
    }
}
