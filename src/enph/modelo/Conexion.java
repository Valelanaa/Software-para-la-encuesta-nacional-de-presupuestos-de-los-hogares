package enph.modelo;

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
    // Dueño de las tablas (esquema). Los demás usuarios trabajan sobre sus objetos.
    private static final String ESQUEMA = "bd1095801365";

    // Usuario general: se usa SOLO para verificar el inicio de sesión (tabla USUARIO)
    private static final String USER_GENERAL = "bd1095801365";
    private static final String PASSWORD_GENERAL = "bd1095801365";

    // Usuarios de base de datos según el rol de quien inició sesión
    private static final String USER_ADMIN = "administrador";
    private static final String PASSWORD_ADMIN = "a1095801365";
    private static final String USER_ENCUESTADOR = "encuestador";
    private static final String PASSWORD_ENCUESTADOR = "e1127955066";

    // Credenciales que usa init() en este momento (al arrancar: el usuario general)
    private static String userActual = USER_GENERAL;
    private static String passwordActual = PASSWORD_GENERAL;

    /** Vuelve al usuario general (antes de iniciar sesión o al cerrarla). */
    public static void usarGeneral() {
        userActual = USER_GENERAL;
        passwordActual = PASSWORD_GENERAL;
    }

    /**
     * Cambia las credenciales según el rol del usuario que inició sesión.
     * Devuelve false si el rol no es ADMINISTRADOR ni ENCUESTADOR
     * (en ese caso se queda con el usuario general).
     */
    public static boolean usarRol(String rol) {
        String r = rol == null ? "" : rol.trim();
        if ("ADMINISTRADOR".equalsIgnoreCase(r)) {
            userActual = USER_ADMIN;
            passwordActual = PASSWORD_ADMIN;
            return true;
        } else if ("ENCUESTADOR".equalsIgnoreCase(r)) {
            userActual = USER_ENCUESTADOR;
            passwordActual = PASSWORD_ENCUESTADOR;
            return true;
        }
        usarGeneral();
        return false;
    }

    //Abre una conexión nueva con Oracle.
    public static Connection init() {
        try {
            // Cargar el controlador JDBC de Oracle
            Class.forName("oracle.jdbc.OracleDriver");

            Connection connection =
                    DriverManager.getConnection(URL, userActual, passwordActual);

            // Los usuarios administrador/encuestador no son dueños de las tablas:
            // se apunta la sesión al esquema que las contiene para no tener que
            // escribir el prefijo en cada consulta.
            if (!userActual.equalsIgnoreCase(ESQUEMA)) {
                try (java.sql.Statement st = connection.createStatement()) {
                    st.execute("ALTER SESSION SET CURRENT_SCHEMA = " + ESQUEMA);
                }
            }

            System.out.println("Conexión exitosa a Oracle como " + userActual + ".");
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