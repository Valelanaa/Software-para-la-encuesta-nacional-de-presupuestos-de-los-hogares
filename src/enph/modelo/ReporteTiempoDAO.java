
package enph.modelo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;

public class ReporteTiempoDAO {

    public List<ReporteTiempoDTO> obtenerReporte() throws SQLException {

        List<ReporteTiempoDTO> lista = new ArrayList<>();

        String sql = "{call PR_REPORTE_TIEMPO_ENCUESTAS(?)}";

        Connection con = null;

        try {

            con = Conexion.init();

            if (con == null) {
                throw new SQLException("No fue posible establecer la conexión con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(sql)) {

                cs.registerOutParameter(1, OracleTypes.CURSOR);

                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(1)) {

                    while (rs.next()) {

                        String id = rs.getString("ID_ENCUESTADOR");

                        String nombre = rs.getString("NOMBRE_COMPLETO");

                        long tiempo = rs.getLong("TIEMPO_PROMEDIO_SEGUNDOS");

                        Long promedio = rs.wasNull() ? null : tiempo;

                        ReporteTiempoDTO reporte = new ReporteTiempoDTO(
                                id,
                                nombre,
                                promedio
                        );

                        lista.add(reporte);
                    }
                }
            }

        } finally {
            Conexion.close(con);
        }

        return lista;
    }
}
