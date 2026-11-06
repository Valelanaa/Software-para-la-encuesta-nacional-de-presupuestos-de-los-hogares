
package enph.modelo;

public class Hogar {
    private int idHogar;
    private String idTexto;
    private String nombreFamilia;
    private String direccion;
    private String idBarrio;
    private String barrio;
    private String idCiudad;
    private String ciudad;
    private String nombreCabeza;
    private boolean incompleta;

    public Hogar(int idHogar, String nombreFamilia, String direccion, String barrio) {
        this.idHogar = idHogar;
        this.nombreFamilia = nombreFamilia;
        this.direccion = direccion;
        this.barrio = barrio;
    }

    public Hogar(String nombreFamilia, String direccion, String barrio) {
        this(0, nombreFamilia, direccion, barrio);
    }

    public Hogar(String nombreFamilia, String direccion, String idBarrio, String barrio, String idCiudad) {
        this(0, nombreFamilia, direccion, barrio);
        this.idBarrio = idBarrio;
        this.idCiudad = idCiudad;
    }

    public int getIdHogar() {
        return idHogar;
    }

    public String getIdTexto() {
        return idTexto != null ? idTexto : String.valueOf(idHogar);
    }

    public String getNombreFamilia() {
        return nombreFamilia;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getIdBarrio() {
        return idBarrio;
    }

    public String getBarrio() {
        return barrio;
    }

    public String getIdCiudad() {
        return idCiudad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getNombreCabeza() {
        return nombreCabeza;
    }

    public boolean isIncompleta() {
        return incompleta;
    }

    public void setIdHogar(int idHogar) {
        this.idHogar = idHogar;
    }

    public void setIdTexto(String idTexto) {
        this.idTexto = idTexto;
    }

    public void setNombreFamilia(String nombreFamilia) {
        this.nombreFamilia = nombreFamilia;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setIdBarrio(String idBarrio) {
        this.idBarrio = idBarrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public void setIdCiudad(String idCiudad) {
        this.idCiudad = idCiudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setNombreCabeza(String nombreCabeza) {
        this.nombreCabeza = nombreCabeza;
    }

    public void setIncompleta(boolean incompleta) {
        this.incompleta = incompleta;
    }
}
