package enph.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla HOGAR: lee las familias que se muestran
 * en las pantallas y permite consultar y actualizar un hogar.
 */
public class HogarDAO {

    // Quita espacios sobrantes y evita null (las columnas CHAR rellenan con espacios)
    private static String limpiar(String s) {
        return s == null ? "" : s.trim();
    }

    // ID_HOGAR es texto en la base; si no es un número devuelve 0 en lugar de fallar
    private static int idNumerico(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Lee todos los hogares, ordenados por nombre (la usa FamiliasRegistradas)
    public List<Hogar> listarHogares() throws SQLException {

        List<Hogar> hogares = new ArrayList<>();
        String sql = "SELECT ID_HOGAR, NOMBRE_HOGAR, "
                   + "DIR_DIRECCION, DIR_BARRIO "
                   + "FROM HOGAR "
                   + "ORDER BY NOMBRE_HOGAR";

        try (Connection con = Conexion.init()) {
            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Hogar hogar = new Hogar(
                        rs.getInt("ID_HOGAR"),
                        rs.getString("NOMBRE_HOGAR"),
                        rs.getString("DIR_DIRECCION"),
                        rs.getString("DIR_BARRIO")
                    );
                    hogares.add(hogar);
                }
            }
        }
        return hogares;
    }

    /**
     * Lista los hogares con su ciudad y el nombre de su cabeza de hogar,
     * ordenados por nombre. Es la que usa la tabla de GestionHogar.
     */
    public List<Hogar> listarHogaresConDetalle() {
        String sql = "SELECT H.ID_HOGAR, H.ID_CIUDAD, H.NOMBRE_HOGAR, H.DIR_DIRECCION, H.DIR_BARRIO, "
                   + "C.NOMBRE_CIUDAD || ' - ' || D.NOMBRE_DEPARTAMENTO, "
                   + "(SELECT MIN(CH.NOMBRE_COMPLETO) FROM CABEZA_DE_HOGAR CH WHERE CH.ID_HOGAR = H.ID_HOGAR) "
                   + "FROM HOGAR H "
                   + "JOIN CIUDAD C ON C.ID_CIUDAD = H.ID_CIUDAD "
                   + "JOIN DEPARTAMENTO D ON D.ID_DEPARTAMENTO = C.ID_DEPARTAMENTO "
                   + "ORDER BY H.NOMBRE_HOGAR";

        List<Hogar> hogares = new ArrayList<>();
        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return hogares;
        }

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = limpiar(rs.getString(1));
                Hogar h = new Hogar(idNumerico(id), limpiar(rs.getString(3)),
                        limpiar(rs.getString(4)), limpiar(rs.getString(5)));
                h.setIdTexto(id);
                h.setIdCiudad(limpiar(rs.getString(2)));
                h.setCiudad(limpiar(rs.getString(6)));
                h.setNombreCabeza(limpiar(rs.getString(7)));
                hogares.add(h);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar hogares: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexion.close(con);
        }
        return hogares;
    }

    // Trae los datos de un hogar (incluida su ciudad) para EditarHogar
    public Hogar buscarPorId(String idHogar) {
        String sql = "SELECT ID_HOGAR, ID_CIUDAD, NOMBRE_HOGAR, DIR_DIRECCION, DIR_BARRIO "
                   + "FROM HOGAR WHERE ID_HOGAR = ?";

        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return null;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String id = limpiar(rs.getString("ID_HOGAR"));
                    Hogar h = new Hogar(idNumerico(id), limpiar(rs.getString("NOMBRE_HOGAR")),
                            limpiar(rs.getString("DIR_DIRECCION")), limpiar(rs.getString("DIR_BARRIO")));
                    h.setIdTexto(id);
                    h.setIdCiudad(limpiar(rs.getString("ID_CIUDAD")));
                    return h;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar hogar: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Conexion.close(con);
        }
        return null;
    }

    /**
 * Inserta un hogar nuevo en la base de datos.
 *
 * @param h hogar con ciudad, nombre, dirección y barrio
 * @return true si se insertó correctamente; false en caso contrario
 */
public boolean insertarHogar(Hogar h) {

    String sql = "INSERT INTO HOGAR "
               + "(ID_CIUDAD, NOMBRE_HOGAR, DIR_DIRECCION, DIR_BARRIO) "
               + "VALUES (?, ?, ?, ?)";

    Connection con = Conexion.init();

    if (con == null) {
        System.err.println("No se pudo conectar a la base de datos.");
        return false;
    }

    try (PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, h.getIdCiudad());
        ps.setString(2, h.getNombreFamilia());
        ps.setString(3, h.getDireccion());
        ps.setString(4, h.getBarrio());

        return ps.executeUpdate() == 1;

    } catch (SQLException e) {
        System.err.println("Error al insertar el hogar: " + e.getMessage());
        e.printStackTrace();
        return false;

    } finally {
        Conexion.close(con);
    }
}
    
    // Actualiza ciudad, nombre, dirección y barrio del hogar
    public boolean actualizarHogar(Hogar h) {
        String sql = "UPDATE HOGAR SET ID_CIUDAD = ?, NOMBRE_HOGAR = ?, DIR_DIRECCION = ?, DIR_BARRIO = ? "
                   + "WHERE ID_HOGAR = ?";

        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, h.getIdCiudad());
            ps.setString(2, h.getNombreFamilia());
            ps.setString(3, h.getDireccion());
            ps.setString(4, h.getBarrio());
            ps.setString(5, h.getIdTexto());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error al actualizar hogar: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Conexion.close(con);
        }
    }
    
        /**
     * Elimina un hogar junto con su cabeza de hogar, sus encuestas y las
     * respuestas de esas encuestas. Todo se hace en una sola transacción:
     * si algo falla, se deshace y no se borra nada.
     */
    public boolean eliminarHogar(String idHogar) {
        // Primero las tablas hijas y al final el hogar (por las llaves foráneas)
        String sqlRespuestas = "DELETE FROM CARACTERIZACION_COMPRA WHERE ID_ENCUESTA IN "
                             + "(SELECT ID_ENCUESTA FROM ENCUESTA WHERE ID_HOGAR = ?)";
        String sqlEncuestas  = "DELETE FROM ENCUESTA WHERE ID_HOGAR = ?";
        String sqlCabeza     = "DELETE FROM CABEZA_DE_HOGAR WHERE ID_HOGAR = ?";
        String sqlHogar      = "DELETE FROM HOGAR WHERE ID_HOGAR = ?";

        Connection con = Conexion.init();
        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try {
            con.setAutoCommit(false); // agrupa los 4 DELETE en una sola transacción
            String[] consultas = {sqlRespuestas, sqlEncuestas, sqlCabeza, sqlHogar};
            int filasHogar = 0;
            for (String sql : consultas) {
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, idHogar);
                    int filas = ps.executeUpdate();
                    if (sql == sqlHogar) {
                        filasHogar = filas;
                    }
                }
            }
            if (filasHogar != 1) {
                con.rollback(); // el hogar no existía: no se confirma nada
                return false;
            }
            con.commit(); // confirma todos los borrados a la vez
            return true;
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ignorada) { }
            System.err.println("Error al eliminar el hogar: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Conexion.close(con);
        }
    }
}