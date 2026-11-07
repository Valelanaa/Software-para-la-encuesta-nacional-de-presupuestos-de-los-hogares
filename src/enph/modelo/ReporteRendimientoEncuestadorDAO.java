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

public class ReporteRendimientoEncuestadorDAO {

    public List<ReporteRendimientoEncuestador> generarReporte(
            int mes, int anio) throws SQLException {

        List<ReporteRendimientoEncuestador> resultados =
                new ArrayList<>();

        Connection conexion = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {

            conexion = Conexion.init();

            if (conexion == null) {
                throw new SQLException(
                    "No se pudo establecer la conexión con Oracle."
                );
            }

            // Llamar al procedimiento almacenado.
            cs = conexion.prepareCall(
                "{call PR_REPORTE_RENDIMIENTO_MENSUAL(?, ?, ?)}"
            );

            // Parámetros de entrada.
            cs.setInt(1, mes);
            cs.setInt(2, anio);

            // Parámetro de salida.
            cs.registerOutParameter(3, OracleTypes.CURSOR);

            cs.execute();

            rs = (ResultSet) cs.getObject(3);

            // Recorrer los resultados.
            while (rs.next()) {

                resultados.add(
                    new ReporteRendimientoEncuestador(
                        rs.getString("ID_ENCUESTADOR"),
                        rs.getString("NOMBRE_COMPLETO"),
                        rs.getInt("REALIZADAS"),
                        rs.getInt("PENDIENTES")
                    )
                );
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