package enph.modelo;

/**
 * Modelo de un hogar (familia) de la encuesta. Guarda los datos que
 * HogarDAO lee de la tabla HOGAR.
 */
public class Hogar {

    private int idHogar;          // id numérico (lo usan las pantallas de la encuesta)
    private String idTexto;       // ID_HOGAR tal cual está en la base (VARCHAR2)
    private String nombreFamilia; // nombre de la familia, ej. "Pérez Gómez"
    private String direccion;     // dirección de la vivienda
    private String barrio;        // barrio de la vivienda
    private String idCiudad;      // ID_CIUDAD
    private String ciudad;        // "Ciudad - Departamento"
    private String nombreCabeza;  // nombre de la cabeza de hogar
    private boolean incompleta; // true si el administrador marcó su encuesta como incompleta

    public Hogar(int idHogar, String nombreFamilia, String direccion, String barrio) {
        this.idHogar = idHogar;
        this.nombreFamilia = nombreFamilia;
        this.direccion = direccion;
        this.barrio = barrio;
    }

    // Constructor anterior: pone el id en 0 ("hogar sin id de base de datos")
    public Hogar(String nombreFamilia, String direccion, String barrio) {
        this(0, nombreFamilia, direccion, barrio);
    }

    public int getIdHogar() { return idHogar; }

    // Id como texto: si no se asignó uno, usa el id numérico
    public String getIdTexto() {
        return idTexto != null ? idTexto : String.valueOf(idHogar);
    }

    public String getNombreFamilia() { return nombreFamilia; }
    public String getDireccion() { return direccion; }
    public String getBarrio() { return barrio; }
    public String getIdCiudad() { return idCiudad; }
    public String getCiudad() { return ciudad; }
    public String getNombreCabeza() { return nombreCabeza; }
    public boolean isIncompleta() { return incompleta; }
    public void setIncompleta(boolean incompleta) { this.incompleta = incompleta; }
    public void setIdTexto(String idTexto) { this.idTexto = idTexto; }
    public void setNombreFamilia(String nombreFamilia) { this.nombreFamilia = nombreFamilia; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setBarrio(String barrio) { this.barrio = barrio; }
    public void setIdCiudad(String idCiudad) { this.idCiudad = idCiudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public void setNombreCabeza(String nombreCabeza) { this.nombreCabeza = nombreCabeza; }
}