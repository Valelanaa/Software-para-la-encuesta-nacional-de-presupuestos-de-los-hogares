package enph;

import enph.modelo.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.SwingUtilities;
import enph.vista.InicioSesion;

public class Main {

    private static final UsuarioDAO USUARIOS = new UsuarioDAO();
    private static final HogarDAO HOGARES = new HogarDAO();
    private static final BarrioDAO BARRIOS = new BarrioDAO();
    private static final CabezaHogarDAO CABEZAS = new CabezaHogarDAO();
    private static final EncuestaDAO ENCUESTAS = new EncuestaDAO();
    private static final CaracterizacionDAO CARACTERIZACION = new CaracterizacionDAO();
    private static final CatalogoDAO CATALOGOS = new CatalogoDAO();
    private static final ReporteEncuestadorDAO REPORTES = new ReporteEncuestadorDAO();
    private static final ReporteEsencialesDAO REPORTE_ESENCIALES = new ReporteEsencialesDAO();

    private Main() { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InicioSesion ventana = new InicioSesion();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }

    // ========================= USUARIOS =========================
    public static Usuario autenticarUsuario(String documento, String contrasena) {
        return USUARIOS.autenticar(documento, contrasena);
    }
    public static boolean validarUsuario(Usuario usuario) { return USUARIOS.validarUsuario(usuario); }
    public static boolean validarCredenciales(String documento, String contrasena) {
        return USUARIOS.validarUsuario(documento, contrasena);
    }
    public static List<Usuario> listarUsuarios() { return USUARIOS.listarUsuarios(); }
    public static Usuario buscarUsuarioPorId(String id) { return USUARIOS.buscarPorId(id); }
    public static List<ItemCiudad> listarCiudades() { return USUARIOS.listarCiudades(); }
    public static boolean usuarioExisteDocumento(String documento) { return USUARIOS.existeDocumento(documento); }
    public static boolean usuarioExisteDocumento(String documento, String idExcluido) {
        return USUARIOS.existeDocumento(documento, idExcluido);
    }
    public static boolean guardarUsuario(Usuario usuario) { return USUARIOS.insertarUsuario(usuario); }
    public static boolean actualizarUsuario(Usuario usuario) { return USUARIOS.actualizarUsuario(usuario); }
    public static boolean eliminarUsuario(String id) { return USUARIOS.eliminarUsuario(id); }

    // ========================= HOGARES =========================
    public static List<Hogar> listarHogares() throws SQLException { return HOGARES.listarHogares(); }
    public static List<Hogar> listarHogaresConDetalle() { return HOGARES.listarHogaresConDetalle(); }
    public static Hogar buscarHogarPorId(String id) { return HOGARES.buscarPorId(id); }
    public static boolean insertarHogar(Hogar hogar) { return HOGARES.insertarHogar(hogar); }
    public static boolean actualizarHogar(Hogar hogar) { return HOGARES.actualizarHogar(hogar); }
    public static boolean eliminarHogar(String id) { return HOGARES.eliminarHogar(id); }
    public static void asignarHogaresAEncuestador(String idEncuestador, List<String> ids) throws SQLException {
        HOGARES.asignarHogaresAEncuestador(idEncuestador, ids);
    }
    public static List<Hogar> listarHogaresSinEncuestador() throws SQLException {
        return HOGARES.listarHogaresSinEncuestador();
    }
    public static List<Hogar> listarHogaresPorEncuestador(String id) throws SQLException {
        return HOGARES.listarHogaresPorEncuestador(id);
    }
    public static boolean desasignarHogar(String idEncuestador, String idHogar) throws SQLException {
        return HOGARES.desasignarHogar(idEncuestador, idHogar);
    }
    public static Map<String, int[]> contarEncuestasPorEncuestador() throws SQLException {
        return HOGARES.contarEncuestasPorEncuestador();
    }
    public static List<String[]> listarEstadoEncuestasPorEncuestador(String id) throws SQLException {
        return HOGARES.listarEstadoEncuestasPorEncuestador(id);
    }
    public static String[] buscarDetalleEncuesta(String idHogar) throws SQLException {
        return HOGARES.buscarDetalleEncuesta(idHogar);
    }
    public static boolean insertarHogarConCabeza(Hogar hogar, CabezaHogar cabeza) {
        return HOGARES.insertarHogarConCabeza(hogar, cabeza);
    }

    // ========================= BARRIOS =========================

    public static List<Barrio> listarBarrios() throws SQLException {
        return BARRIOS.listarBarrios();
    }

    public static List<Barrio> listarBarriosPorCiudad(String idCiudad)
            throws SQLException {
        return BARRIOS.listarPorCiudad(idCiudad);
    }

    public static Barrio buscarBarrioPorId(String id)
            throws SQLException {
        return BARRIOS.buscarPorId(id);
    }

    public static boolean crearBarrio(String nombre, String idCiudad)
            throws SQLException {
        return BARRIOS.crearBarrio(nombre, idCiudad);
    }

    public static boolean editarBarrio(String idBarrio, String nombre,
                                       String idCiudad) throws SQLException {
        return BARRIOS.editarBarrio(idBarrio, nombre, idCiudad);
    }

    public static boolean barrioEnUso(String idBarrio) throws SQLException {
        return BARRIOS.barrioEnUso(idBarrio);
    }

    public static boolean eliminarBarrio(String idBarrio) throws SQLException {
        return BARRIOS.eliminarBarrio(idBarrio);
    }

    // ========================= CABEZA DE HOGAR =========================
    public static CabezaHogar buscarCabezaPorHogar(String idHogar) { return CABEZAS.buscarPorHogar(idHogar); }
    public static boolean cabezaExisteDocumento(String documento, String idExcluido) {
        return CABEZAS.existeDocumento(documento, idExcluido);
    }
    public static boolean actualizarCabezaHogar(CabezaHogar cabeza) { return CABEZAS.actualizar(cabeza); }

    // ========================= ENCUESTAS =========================
    public static boolean hogarTieneEncuesta(int idHogar) { return ENCUESTAS.hogarTieneEncuesta(idHogar); }
    public static int obtenerIdEncuesta(int idHogar) { return ENCUESTAS.obtenerIdEncuesta(idHogar); }
    public static boolean eliminarEncuestasDelHogar(int idHogar) { return ENCUESTAS.eliminarEncuestasDelHogar(idHogar); }
    public static int crearEncuesta(int idHogar) { return ENCUESTAS.crearEncuesta(idHogar); }
    public static void finalizarEncuesta(int idEncuesta) { ENCUESTAS.finalizarEncuesta(idEncuesta); }
    public static void eliminarEncuestaVacia(int idEncuesta) { ENCUESTAS.eliminarEncuestaVacia(idEncuesta); }
    public static boolean marcarEncuestaIncompleta(int idEncuesta) { return ENCUESTAS.marcarIncompleta(idEncuesta); }
    public static void quitarMarcaIncompleta(int idEncuesta) { ENCUESTAS.quitarMarcaIncompleta(idEncuesta); }

    // ========================= CARACTERIZACIÓN =========================
    public static boolean guardarCaracterizacion(int idEncuesta, int idGrupo, int idFrecuencia,
            BigDecimal valor, Integer intencion) {
        return CARACTERIZACION.guardar(idEncuesta, idGrupo, idFrecuencia, valor, intencion);
    }
    public static RespuestaGrupo obtenerCaracterizacion(int idEncuesta, int idGrupo) {
        return CARACTERIZACION.obtener(idEncuesta, idGrupo);
    }
    public static List<String[]> listarRespuestasPorHogar(String idHogar) throws SQLException {
        return CARACTERIZACION.listarRespuestasPorHogar(idHogar);
    }
    public static Set<Integer> listarGruposRespondidos(int idEncuesta) {
        return CARACTERIZACION.listarGruposRespondidos(idEncuesta);
    }

    // ========================= CATÁLOGOS =========================
    public static List<ItemCatalogo> listarGrupos() { return CATALOGOS.listarGrupos(); }
    public static List<ItemCatalogo> listarFrecuencias() { return CATALOGOS.listarFrecuencias(); }
    public static boolean crearGrupo(String nombre) { return CATALOGOS.crearGrupo(nombre); }
    public static boolean editarGrupo(int id, String nombre) { return CATALOGOS.editarGrupo(id, nombre); }
    public static boolean grupoEnUso(int id) { return CATALOGOS.grupoEnUso(id); }
    public static boolean eliminarGrupo(int id) { return CATALOGOS.eliminarGrupo(id); }
    public static boolean crearFrecuencia(String nombre) { return CATALOGOS.crearFrecuencia(nombre); }
    public static boolean editarFrecuencia(int id, String nombre) { return CATALOGOS.editarFrecuencia(id, nombre); }
    public static boolean frecuenciaEnUso(int id) { return CATALOGOS.frecuenciaEnUso(id); }
    public static boolean eliminarFrecuencia(int id) { return CATALOGOS.eliminarFrecuencia(id); }

    // ========================= REPORTES =========================
    public static List<ReporteBarrio> generarReporteEncuestador(String id, LocalDate desde, LocalDate hasta)
            throws SQLException {
        return REPORTES.generarReporte(id, desde, hasta);
    }
    public static List<ReporteEsencial> generarReporteEsenciales(
        String frecuencia) throws SQLException {
    return REPORTE_ESENCIALES.generarReporte(frecuencia);
    }
}