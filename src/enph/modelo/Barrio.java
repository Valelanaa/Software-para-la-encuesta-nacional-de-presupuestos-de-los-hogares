/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 * Barrio. Dominio: Barrio (idBarrio, nombre) que pertenece a una Ciudad
 * (Barrio 0..* - 1 Ciudad). Tabla: BARRIO.
 *
 * @author Valeria
 */
public class Barrio {

    private String idBarrio;
    private String nombreBarrio;
    private Ciudad ciudad;   // ciudad con su departamento (puede ser null si no se pudo cargar)

    public Barrio(String idBarrio, String nombreBarrio, Ciudad ciudad) {
        this.idBarrio = idBarrio;
        this.nombreBarrio = nombreBarrio;
        this.ciudad = ciudad;
    }

    public String getIdBarrio() {
        return idBarrio;
    }

    public String getNombreBarrio() {
        return nombreBarrio;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    /** ID de la ciudad como texto; "" si el barrio no tiene ciudad cargada. */
    public String getIdCiudad() {
        return ciudad == null ? "" : ciudad.getId();
    }

    /** Texto "Ciudad - Departamento"; "" si el barrio no tiene ciudad cargada. */
    public String getNombreCiudad() {
        return ciudad == null ? "" : ciudad.toString();
    }

    public void setIdBarrio(String idBarrio) {
        this.idBarrio = idBarrio;
    }

    public void setNombreBarrio(String nombreBarrio) {
        this.nombreBarrio = nombreBarrio;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public String toString() {
        return nombreBarrio;
    }
}
