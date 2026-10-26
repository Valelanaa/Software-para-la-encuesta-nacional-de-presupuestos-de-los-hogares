package enph.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla HOGAR: lee las familias que se muestran
 * en la tabla de FamiliasRegistradas.
 */

public class HogarDAO {

    //Lee todos los hogares de la base de datos, ordenados por nombre de familia.
    //si no hay conexión o falla la consulta; no se captura
    //aquí para que FamiliasRegistradas pueda mostrar el error al usuario
    public List<Hogar> listarHogares() throws SQLException {

        List<Hogar> hogares = new ArrayList<>();
         // Los nombres de columna deben coincidir con los de la tabla HOGAR;
        // si no, Oracle responde con ORA-00904 (identificador no válido)
        String sql = "SELECT ID_HOGAR, NOMBRE_FAMILIA, "
                   + "DIR_DIRECCION, DIR_BARRIO "
                   + "FROM HOGAR "
                   + "ORDER BY NOMBRE_FAMILIA";
          // try-with-resources: la conexión se cierra sola al terminar el bloque
        try (Connection con = Conexion.init()) {
            // Conexion.init() devuelve null si no pudo conectar
            if (con == null) {
                throw new SQLException(
                    "No fue posible conectar con Oracle."
                );
            }
            // La consulta y su resultado también se cierran solos
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                 // Cada fila de la tabla se convierte en un objeto Hogar
                while (rs.next()) {

                    Hogar hogar = new Hogar(
                        rs.getInt("ID_HOGAR"),
                        rs.getString("NOMBRE_FAMILIA"),
                        rs.getString("DIR_DIRECCION"),
                        rs.getString("DIR_BARRIO")
                    );

                    hogares.add(hogar);
                }
            }
        }

        return hogares;
    }
}