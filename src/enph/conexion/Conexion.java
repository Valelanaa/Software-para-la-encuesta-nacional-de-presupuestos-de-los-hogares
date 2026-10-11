
package enph.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Administra la conexión con la base de datos Oracle.
 * Todas las clases DAO obtienen su conexión con Conexion.init()
 * y la cierran con Conexion.close() cuando terminan.
 */

public class Conexion {
    
    //Conexion con el servidor de la universidad 
    //private static final String URL = "jdbc:oracle:thin:@192.168.254.215:1521:orcl"; 
    //Conexion de manera local 
    private static final String URL = "jdbc:oracle:thin:@//localhost:1522/XE";
    // Usuario (esquema) de Oracle y su contraseña
    private static final String USER = "bd1095801365";
    private static final String PASSWORD = "bd1095801365";

    //Abre una conexión nueva con Oracle.
    public static Connection init() {
        try {
            // Cargar el controlador JDBC de Oracle
            Class.forName("oracle.jdbc.OracleDriver");

            Connection connection =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Conexión exitosa a Oracle.");
            return connection;

        } catch (ClassNotFoundException e) {
            // Falta el jar del controlador en el proyecto
            System.err.println("No se encontró el controlador JDBC de Oracle.");
            e.printStackTrace();

        } catch (SQLException e) {
             // Error de red, usuario, contraseña o servicio (ORA-12541, ORA-01017, etc.)
            System.err.println(
                    "Error al conectar a Oracle: " + e.getMessage()
            );
            e.printStackTrace();
        }
        // Quien llama debe revisar si el resultado es null antes de usarlo
        return null;
    }
    /**
     * Cierra una conexión abierta con init().
     * Acepta null sin problema, así se puede llamar siempre desde un finally.
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println(
                        "Error al cerrar la conexión: " + e.getMessage()
                );
            }
        }
    }
}