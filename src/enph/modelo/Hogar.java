package enph.modelo;
/**
 * Modelo de un hogar (familia) de la encuesta. Es una clase sencilla que solo
 * guarda los datos que HogarDAO lee de la tabla HOGAR y que
 * FamiliasRegistradas muestra en la tabla de familias.
 */
public class Hogar {

    private int idHogar;  // id del hogar en la tabla HOGAR (llave primaria)
    private String nombreFamilia; // nombre de la familia, ej. "Pérez Gómez"
    private String direccion; // dirección de la vivienda
    private String barrio; // barrio donde queda la vivienda

    /**
     * Constructor completo: lo usa HogarDAO al leer cada fila de la base de datos.
     * El idHogar es el que luego se pasa a EncuestaDAO y a SesionEncuesta
     * para iniciar, editar o eliminar la encuesta de esa familia.
     */
    
    public Hogar(int idHogar, String nombreFamilia, String direccion, String barrio) {
        this.idHogar = idHogar;
        this.nombreFamilia = nombreFamilia;
        this.direccion = direccion;
        this.barrio = barrio;
    }

    // Constructor anterior: se deja para que el DAO actual siga compilando
    //Pone el id en 0, que significa "hogar sin id de base de datos".
    public Hogar(String nombreFamilia, String direccion, String barrio) {
        this(0, nombreFamilia, direccion, barrio);
    }

    // Métodos de lectura (getters): la clase no tiene setters porque los datos
    // no se modifican después de leerlos de la base de datos.
    public int getIdHogar() {
        return idHogar;
    }

    public String getNombreFamilia() {
        return nombreFamilia;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getBarrio() {
        return barrio;
    }
}