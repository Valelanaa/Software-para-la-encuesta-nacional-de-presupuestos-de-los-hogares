/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */
public class ReporteBarrioDetalle {
 
    private final String familia;
    private final String encuestador;
 
    public ReporteBarrioDetalle(String familia, String encuestador) {
        this.familia = familia;
        this.encuestador = encuestador;
    }
 
    public String getFamilia() { return familia; }
    public String getEncuestador() { return encuestador; }
}
