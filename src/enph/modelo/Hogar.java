package enph.modelo;

/**
 * Hogar. Dominio: Hogar (idHogar, nombreFamilia, direccion) que
 *  - está en un Barrio (Hogar 0..* - 1 Barrio; la Ciudad sale del barrio),
 *  - tiene como máximo una CabezaHogar (0..1),
 *  - puede tener una Asignacion a un Encuestador (0..1).
 * Tabla: HOGAR.
 */
public class Hogar {

    private int idHogar;               // ID_HOGAR
    private String nombreFamilia;      // NOMBRE_HOGAR
    private String direccion;          // NUM_DIRECCION
    private Barrio barrio;             // ID_BARRIO (con su Ciudad cuando se pudo cargar)
    private CabezaHogar cabeza;        // cabeza de hogar (null si no se ha cargado)
    private Asignacion asignacion;     // null si el hogar no tiene encuestador
    private boolean incompleta;        // true si la encuesta del hogar está incompleta (se llena en el listado)

    public Hogar(int idHogar, String nombreFamilia, String direccion, Barrio barrio) {
        this.idHogar = idHogar;
        this.nombreFamilia = nombreFamilia;
        this.direccion = direccion;
        this.barrio = barrio;
    }

    /** Hogar nuevo, todavía sin ID (se asigna al guardarlo). */
    public Hogar(String nombreFamilia, String direccion, Barrio barrio) {
        this(0, nombreFamilia, direccion, barrio);
    }

    public int getIdHogar() {
        return idHogar;
    }

    public void setIdHogar(int idHogar) {
        this.idHogar = idHogar;
    }

    /** El ID como texto, que es lo que reciben los procedimientos almacenados. */
    public String getIdTexto() {
        return String.valueOf(idHogar);
    }

    public String getNombreFamilia() {
        return nombreFamilia;
    }

    public void setNombreFamilia(String nombreFamilia) {
        this.nombreFamilia = nombreFamilia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Barrio getBarrio() {
        return barrio;
    }

    public void setBarrio(Barrio barrio) {
        this.barrio = barrio;
    }

    /** La ciudad del hogar es la de su barrio; null si no está cargada. */
    public Ciudad getCiudad() {
        return barrio == null ? null : barrio.getCiudad();
    }

    public CabezaHogar getCabeza() {
        return cabeza;
    }

    public void setCabeza(CabezaHogar cabeza) {
        this.cabeza = cabeza;
    }

    public Asignacion getAsignacion() {
        return asignacion;
    }

    public void setAsignacion(Asignacion asignacion) {
        this.asignacion = asignacion;
    }

    public boolean isIncompleta() {
        return incompleta;
    }

    public void setIncompleta(boolean incompleta) {
        this.incompleta = incompleta;
    }

    // ---------- Atajos de texto (para tablas y para los procedimientos) ----------

    /** ID del barrio como texto; "" si no tiene barrio. */
    public String getIdBarrio() {
        return barrio == null || barrio.getIdBarrio() == null ? "" : barrio.getIdBarrio();
    }

    /** ID de la ciudad como texto; "" si no está cargada. */
    public String getIdCiudad() {
        return barrio == null ? "" : barrio.getIdCiudad();
    }

    /** Nombre del barrio; "" si no tiene. */
    public String getNombreBarrio() {
        return barrio == null || barrio.getNombreBarrio() == null ? "" : barrio.getNombreBarrio();
    }

    /** Texto "Ciudad - Departamento"; "" si no está cargada. */
    public String getNombreCiudad() {
        Ciudad ciudad = getCiudad();
        return ciudad == null ? "" : ciudad.toString();
    }

    /** Nombre de la cabeza de hogar; "" si no se ha cargado. */
    public String getNombreCabeza() {
        return cabeza == null || cabeza.getNombreCompleto() == null ? "" : cabeza.getNombreCompleto();
    }
}