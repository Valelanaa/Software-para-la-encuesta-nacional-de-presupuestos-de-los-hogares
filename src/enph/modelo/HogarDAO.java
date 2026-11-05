package enph.modelo;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

/**
 * Acceso a datos de la tabla HOGAR.
 *
 * Todas las operaciones se realizan mediante procedimientos almacenados
 * en Oracle. No se utilizan consultas SQL directamente desde Java.
 */
public class HogarDAO {

    /**
     * Quita espacios sobrantes y evita valores null.
     */
    private static String limpiar(String s) {
        return s == null ? "" : s.trim();
    }

    /**
     * Convierte el identificador del hogar a número.
     * Si no es numérico, devuelve 0.
     */
    private static int idNumerico(String id) {
        try {
            return Integer.parseInt(limpiar(id));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Ejecuta PR_LISTAR_HOGARES.
     *
     * Lista los hogares e identifica si tienen una encuesta incompleta.
     */
    public List<Hogar> listarHogares() throws SQLException {

        List<Hogar> hogares = new ArrayList<>();

        String llamada = "{call PR_LISTAR_HOGARES(?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.registerOutParameter(1, OracleTypes.CURSOR);

                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(1)) {

                    while (rs != null && rs.next()) {

                        String id = limpiar(rs.getString("ID_HOGAR"));

                        Hogar hogar = new Hogar(
                                idNumerico(id),
                                limpiar(rs.getString("NOMBRE_HOGAR")),
                                limpiar(rs.getString("DIR_NUMERO")),
                                limpiar(rs.getString("DIR_BARRIO"))
                        );

                        hogar.setIdTexto(id);

                        hogar.setIncompleta(rs.getInt("INCOMPLETA") == 1);

                        hogares.add(hogar);
                    }
                }
            }
        }

        return hogares;
    }

    /**
     * Ejecuta PR_LISTAR_HOGARES_DETALLE.
     *
     * Lista los hogares con su ciudad y el nombre de la cabeza de hogar.
     */
    public List<Hogar> listarHogaresConDetalle() {

        List<Hogar> hogares = new ArrayList<>();

        String llamada = "{call PR_LISTAR_HOGARES_DETALLE(?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                System.err.println("No se pudo conectar a la base de datos.");
                return hogares;
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.registerOutParameter(1, OracleTypes.CURSOR);

                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(1)) {

                    while (rs != null && rs.next()) {

                        String id = limpiar(rs.getString("ID_HOGAR"));

                        Hogar hogar = new Hogar(
                                idNumerico(id),
                                limpiar(rs.getString("NOMBRE_HOGAR")),
                                limpiar(rs.getString("DIR_NUMERO")),
                                limpiar(rs.getString("DIR_BARRIO"))
                        );

                        hogar.setIdTexto(id);

                        hogar.setIdCiudad(limpiar(rs.getString("ID_CIUDAD")));

                        hogar.setCiudad(limpiar(rs.getString("CIUDAD")));

                        hogar.setNombreCabeza(limpiar(rs.getString("NOMBRE_CABEZA")));

                        hogares.add(hogar);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar hogares: " + e.getMessage());
            e.printStackTrace();
        }

        return hogares;
    }

    /**
     * Ejecuta PR_BUSCAR_HOGAR.
     *
     * Busca un hogar por su identificador.
     */
    public Hogar buscarPorId(String idHogar) {

        String llamada = "{call PR_BUSCAR_HOGAR(?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                System.err.println("No se pudo conectar a la base de datos.");
                return null;
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, idHogar);

                cs.registerOutParameter(2, OracleTypes.CURSOR);

                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {

                    if (rs != null && rs.next()) {

                        String id = limpiar(rs.getString("ID_HOGAR"));

                        Hogar hogar = new Hogar(
                                idNumerico(id),
                                limpiar(rs.getString("NOMBRE_HOGAR")),
                                limpiar(rs.getString("DIR_NUMERO")),
                                limpiar(rs.getString("DIR_BARRIO"))
                        );

                        hogar.setIdTexto(id);

                        hogar.setIdCiudad(
                                limpiar(rs.getString("ID_CIUDAD"))
                        );

                        return hogar;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar hogar: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Ejecuta PR_INSERTAR_HOGAR.
     *
     * Inserta un hogar nuevo.
     */
    public boolean insertarHogar(Hogar h) {

        String llamada = "{call PR_INSERTAR_HOGAR(?, ?, ?, ?, ?)}";

        Connection con = Conexion.init();

        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try {

            con.setAutoCommit(false);

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, h.getIdCiudad());
                cs.setString(2, h.getNombreFamilia());
                cs.setString(3, h.getDireccion());
                cs.setString(4, h.getBarrio());

                cs.registerOutParameter(5, Types.NUMERIC);

                cs.execute();

                boolean resultado = cs.getInt(5) == 1;

                if (resultado) {
                    con.commit();
                } else {
                    con.rollback();
                }

                return resultado;
            }

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException errorRollback) {
                e.addSuppressed(errorRollback);
            }

            System.err.println("Error al insertar el hogar: " + e.getMessage());
            e.printStackTrace();

            return false;

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restaurar la transacción.");
            }

            Conexion.close(con);
        }
    }

    /**
     * Ejecuta PR_ACTUALIZAR_HOGAR.
     *
     * Actualiza ciudad, nombre, dirección y barrio.
     */
    public boolean actualizarHogar(Hogar h) {

        String llamada = "{call PR_ACTUALIZAR_HOGAR(?, ?, ?, ?, ?, ?)}";

        Connection con = Conexion.init();

        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try {

            con.setAutoCommit(false);

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, h.getIdTexto());
                cs.setString(2, h.getIdCiudad());
                cs.setString(3, h.getNombreFamilia());
                cs.setString(4, h.getDireccion());
                cs.setString(5, h.getBarrio());

                cs.registerOutParameter(6, Types.NUMERIC);

                cs.execute();

                boolean resultado = cs.getInt(6) == 1;

                if (resultado) {
                    con.commit();
                } else {
                    con.rollback();
                }

                return resultado;
            }

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException errorRollback) {
                e.addSuppressed(errorRollback);
            }

            System.err.println("Error al actualizar hogar: " + e.getMessage());
            e.printStackTrace();

            return false;

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restaurar la transacción.");
            }

            Conexion.close(con);
        }
    }

    /**
     * Ejecuta PR_ELIMINAR_HOGAR.
     *
     * Elimina el hogar y sus registros relacionados en una transacción.
     */
    public boolean eliminarHogar(String idHogar) {

        String llamada = "{call PR_ELIMINAR_HOGAR(?, ?)}";

        Connection con = Conexion.init();

        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try {

            con.setAutoCommit(false);

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, idHogar);

                cs.registerOutParameter(2, Types.NUMERIC);

                cs.execute();

                boolean resultado = cs.getInt(2) == 1;

                if (resultado) {
                    con.commit();
                } else {
                    con.rollback();
                }

                return resultado;
            }

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException errorRollback) {
                e.addSuppressed(errorRollback);
            }

            System.err.println("Error al eliminar el hogar: " + e.getMessage());
            e.printStackTrace();

            return false;

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restaurar la transacción.");
            }

            Conexion.close(con);
        }
    }

    /**
     * Ejecuta PR_ASIGNAR_HOGARES.
     *
     * Asigna uno o varios hogares a un encuestador.
     *
     * Se utiliza SYS.ODCIVARCHAR2LIST para enviar la lista de IDs a Oracle.
     */
    public void asignarHogaresAEncuestador(String idEncuestador,List<String> idsHogares) throws SQLException {

        if (idEncuestador == null || idEncuestador.trim().isEmpty()) {
            throw new SQLException("Debe seleccionar un encuestador.");
        }

        if (idsHogares == null || idsHogares.isEmpty()) {
            throw new SQLException("Debe seleccionar al menos una familia.");
        }

        // Validar los identificadores antes de enviarlos a Oracle.
        String[] ids = new String[idsHogares.size()];

        for (int i = 0; i < idsHogares.size(); i++) {

            String id = idsHogares.get(i);

            if (id == null || id.trim().isEmpty()) {
                throw new SQLException("Se encontró un identificador de hogar inválido.");
            }

            ids[i] = id.trim();
        }

        String llamada = "{call PR_ASIGNAR_HOGARES(?, ?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            boolean autoCommitOriginal = con.getAutoCommit();

            try {

                con.setAutoCommit(false);

                OracleConnection oracleCon = con.unwrap(OracleConnection.class);

                Array arregloIds = oracleCon.createOracleArray("SYS.ODCIVARCHAR2LIST",ids);

                try (CallableStatement cs = con.prepareCall(llamada)) {

                    cs.setString(1, idEncuestador.trim());
                    cs.setArray(2, arregloIds);

                    cs.registerOutParameter(3, Types.NUMERIC);

                    cs.execute();

                    int resultado = cs.getInt(3);

                    if (resultado == 1) {
                        con.commit();
                    } else {
                        con.rollback();
                        throw new SQLException("No se pudieron asignar todos los hogares.");
                    }

                } finally {
                    arregloIds.free();
                }

            } catch (SQLException e) {

                try {
                    con.rollback();
                } catch (SQLException errorRollback) {
                    e.addSuppressed(errorRollback);
                }

                throw e;

            } finally {

                try {
                    con.setAutoCommit(autoCommitOriginal);
                } catch (SQLException e) {
                    System.err.println(
                            "Error al restaurar la transacción."
                    );
                }
            }
        }
    }

    /**
     * Ejecuta PR_HOGARES_SIN_ENCUESTADOR.
     *
     * Lista los hogares que todavía no tienen un encuestador asignado.
     */
    public List<Hogar> listarHogaresSinEncuestador() throws SQLException {

        List<Hogar> hogares = new ArrayList<>();

        String llamada = "{call PR_HOGARES_SIN_ENCUESTADOR(?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.registerOutParameter(1, OracleTypes.CURSOR);

                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(1)) {

                    while (rs != null && rs.next()) {

                        String id = limpiar(rs.getString("ID_HOGAR"));

                        Hogar hogar = new Hogar(
                                idNumerico(id),
                                limpiar(rs.getString("NOMBRE_HOGAR")),
                                limpiar(rs.getString("DIR_NUMERO")),
                                limpiar(rs.getString("DIR_BARRIO"))
                        );

                        hogar.setIdTexto(id);

                        hogares.add(hogar);
                    }
                }
            }
        }

        return hogares;
    }

    /**
     * Ejecuta PR_HOGARES_POR_ENCUESTADOR.
     *
     * Consulta los hogares que tiene asignados un encuestador.
     */
    public List<Hogar> listarHogaresPorEncuestador(String idEncuestador) throws SQLException {

        List<Hogar> hogares = new ArrayList<>();

        if (idEncuestador == null || idEncuestador.trim().isEmpty()) {
            throw new SQLException("No se ha seleccionado un encuestador.");
        }

        String llamada = "{call PR_HOGARES_POR_ENCUESTADOR(?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, idEncuestador.trim());

                cs.registerOutParameter(2, OracleTypes.CURSOR);

                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {

                    while (rs != null && rs.next()) {

                        String id = limpiar(rs.getString("ID_HOGAR"));

                        Hogar hogar = new Hogar(
                                idNumerico(id),
                                limpiar(rs.getString("NOMBRE_HOGAR")),
                                limpiar(rs.getString("DIR_NUMERO")),
                                limpiar(rs.getString("DIR_BARRIO"))
                        );

                        hogar.setIdTexto(id);

                        hogares.add(hogar);
                    }
                }
            }
        }

        return hogares;
    }

    /**
     * Ejecuta PR_DESASIGNAR_HOGAR.
     *
     * Desasigna un hogar de un encuestador sin eliminar el hogar.
     */
    public boolean desasignarHogar(String idEncuestador,String idHogar) throws SQLException {

        if (idEncuestador == null || idEncuestador.trim().isEmpty()) {
            throw new SQLException("No se ha seleccionado un encuestador.");
        }

        if (idHogar == null || idHogar.trim().isEmpty()) {
            throw new SQLException("No se ha seleccionado un hogar.");
        }

        String llamada = "{call PR_DESASIGNAR_HOGAR(?, ?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, idHogar.trim());
                cs.setString(2, idEncuestador.trim());

                cs.registerOutParameter(3, Types.NUMERIC);

                cs.execute();

                return cs.getInt(3) == 1;
            }
        }
    }

    /**
     * Ejecuta PR_CONTAR_ENCUESTAS.
     *
     * Devuelve un mapa cuya llave es el ID del encuestador
     * y cuyo valor contiene:
     *
     * [0] Cantidad de hogares con encuesta realizada.
     * [1] Cantidad de hogares pendientes.
     */
    public Map<String, int[]> contarEncuestasPorEncuestador() throws SQLException {

        Map<String, int[]> conteos = new HashMap<>();

        String llamada = "{call PR_CONTAR_ENCUESTAS(?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.registerOutParameter(1, OracleTypes.CURSOR);

                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(1)) {

                    while (rs != null && rs.next()) {

                        String idEncuestador = limpiar(rs.getString("ID_ENCUESTADOR"));

                        int realizadas = rs.getInt("REALIZADAS");
                        int pendientes = rs.getInt("PENDIENTES");

                        conteos.put(
                                idEncuestador,
                                new int[]{realizadas, pendientes}
                        );
                    }
                }
            }
        }

        return conteos;
    }

    /**
     * Ejecuta PR_ESTADO_ENCUESTAS.
     *
     * Devuelve una lista de arreglos String:
     *
     * [0] Nombre de la familia.
     * [1] Estado: Realizada o Pendiente.
     * [2] ID del hogar.
     */
    public List<String[]> listarEstadoEncuestasPorEncuestador(
            String idEncuestador) throws SQLException {

        List<String[]> filas = new ArrayList<>();

        if (idEncuestador == null || idEncuestador.trim().isEmpty()) {
            throw new SQLException("No se ha seleccionado un encuestador.");
        }

        String llamada ="{call PR_ESTADO_ENCUESTAS(?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, idEncuestador.trim());

                cs.registerOutParameter(2, OracleTypes.CURSOR);

                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {

                    while (rs != null && rs.next()) {

                        filas.add(new String[]{
                                limpiar(rs.getString("NOMBRE_HOGAR")),
                                limpiar(rs.getString("ESTADO")),
                                limpiar(rs.getString("ID_HOGAR"))
                        });
                    }
                }
            }
        }

        return filas;
    }

    /**
     * Ejecuta PR_BUSCAR_DETALLE_ENCUESTA.
     *
     * Devuelve un arreglo String:
     *
     * [0] Nombre de la familia.
     * [1] Dirección.
     * [2] Barrio.
     * [3] Ciudad.
     * [4] Fecha de inicio.
     * [5] Fecha de finalización.
     *
     * Las fechas son null si no hay encuesta.
     * Devuelve null si el hogar no existe.
     */
    public String[] buscarDetalleEncuesta(
            String idHogar) throws SQLException {

        String llamada = "{call PR_BUSCAR_DETALLE_ENCUESTA(?, ?)}";

        try (Connection con = Conexion.init()) {

            if (con == null) {
                throw new SQLException("No fue posible conectar con Oracle.");
            }

            try (CallableStatement cs = con.prepareCall(llamada)) {

                cs.setString(1, idHogar);

                cs.registerOutParameter(2, OracleTypes.CURSOR);

                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {

                    if (rs != null && rs.next()) {

                        return new String[]{
                                limpiar(rs.getString("NOMBRE_HOGAR")),
                                limpiar(rs.getString("DIR_NUMERO")),
                                limpiar(rs.getString("DIR_BARRIO")),
                                limpiar(rs.getString("CIUDAD")),
                                rs.getString("FECHA_INICIO"),
                                rs.getString("FECHA_FIN")
                        };
                    }
                }
            }
        }

        return null;
    }

    /**
     * Ejecuta PR_INSERTAR_HOGAR_CON_CABEZA.
     *
     * Crea el hogar y su cabeza de hogar en una sola transacción.
     */
    public boolean insertarHogarConCabeza(Hogar h, CabezaHogar c) {

        String llamada = "{call PR_INSERTAR_HOGAR_CON_CABEZA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        Connection con = Conexion.init();

        if (con == null) {
            System.err.println("No se pudo conectar a la base de datos.");
            return false;
        }

        try {

            con.setAutoCommit(false);

            try (CallableStatement cs = con.prepareCall(llamada)) {

                // Datos del hogar.
                cs.setString(1, h.getIdCiudad());
                cs.setString(2, h.getNombreFamilia());
                cs.setString(3, h.getDireccion());
                cs.setString(4, h.getBarrio());

                // Datos de la cabeza de hogar.
                cs.setString(5, c.getNombreCompleto());
                cs.setString(6, c.getDocumentoIdentidad());
                cs.setString(7, c.getCorreo());
                cs.setString(8, c.getTelefono());

                // Parámetros de salida.
                cs.registerOutParameter(9, Types.VARCHAR);
                cs.registerOutParameter(10, Types.VARCHAR);
                cs.registerOutParameter(11, Types.NUMERIC);

                cs.execute();

                String idHogar = cs.getString(9);
                String idCabeza = cs.getString(10);

                boolean resultado = cs.getInt(11) == 1;

                if (resultado) {

                    con.commit();

                    // Guardar los IDs generados en los objetos.
                    h.setIdTexto(limpiar(idHogar));

                    c.setIdHogar(limpiar(idHogar));
                    c.setIdCabezaHogar(limpiar(idCabeza));

                } else {
                    con.rollback();
                }

                return resultado;
            }

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException errorRollback) {
                e.addSuppressed(errorRollback);
            }

            System.err.println("Error al crear el hogar y su cabeza: "+ e.getMessage());
            e.printStackTrace();

            return false;

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restaurar la transacción.");
            }

            Conexion.close(con);
        }
    }
}