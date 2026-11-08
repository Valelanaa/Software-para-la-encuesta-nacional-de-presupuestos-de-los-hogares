/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */

public class ReporteCiudadBarrio {
 
    private final String idBarrio;
    private final String barrio;
    private final int cantidad;
 
    public ReporteCiudadBarrio(String idBarrio, String barrio, int cantidad) {
        this.idBarrio = idBarrio;
        this.barrio = barrio;
        this.cantidad = cantidad;
    }
 
    public String getIdBarrio() { return idBarrio; }
    public String getBarrio() { return barrio; }
    public int getCantidad() { return cantidad; }
}