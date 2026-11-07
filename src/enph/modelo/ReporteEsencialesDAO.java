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
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleTypes;

public class ReporteEsencialesDAO {

    public List<ReporteEsencial> generarReporte(String frecuencia)
            throws SQLException {

        List<ReporteEsencial> resultados = new ArrayList<>();

        Connection conexion = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {

            conexion = Conexion.init();

            if (conexion == null) {
                throw new SQLException("No se pudo establecer la conexión con Oracle.");
            }

            String sql = "{call PR_REPORTE_ESENCIALES(?, ?)}";

            cs = conexion.prepareCall(sql);

            cs.setString(1, frecuencia);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            cs.execute();

            rs = (ResultSet) cs.getObject(2);

            while (rs.next()) {

                ReporteEsencial reporte = new ReporteEsencial(
                    rs.getString("GRUPO_ALIMENTO"),
                    rs.getInt("CANTIDAD")
                );

                resultados.add(reporte);
            }

        } finally {

            if (rs != null) {
                rs.close();
            }

            if (cs != null) {
                cs.close();
            }

            Conexion.close(conexion);
        }

        return resultados;
    }
}
