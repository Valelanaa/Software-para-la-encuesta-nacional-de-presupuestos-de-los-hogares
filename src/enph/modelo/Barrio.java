/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */

public class Barrio {

    private String idBarrio;
    private String nombreBarrio;
    private String idCiudad;

    public Barrio(String idBarrio, String nombreBarrio, String idCiudad) {
        this.idBarrio = idBarrio;
        this.nombreBarrio = nombreBarrio;
        this.idCiudad = idCiudad;
    }

    public String getIdBarrio() { return idBarrio; }
    public String getNombreBarrio() { return nombreBarrio; }
    public String getIdCiudad() { return idCiudad; }

    public void setIdBarrio(String idBarrio) { this.idBarrio = idBarrio; }
    public void setNombreBarrio(String nombreBarrio) { this.nombreBarrio = nombreBarrio; }
    public void setIdCiudad(String idCiudad) { this.idCiudad = idCiudad; }

    @Override
    public String toString() {
        return nombreBarrio;
    }
}
