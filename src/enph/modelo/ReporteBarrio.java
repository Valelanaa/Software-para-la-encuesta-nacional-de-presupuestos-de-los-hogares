/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */

public class ReporteBarrio {
    private String barrio;
    private int asignadas;
    private int finalizadas;
    private int pendientes;
    private int incompletas;

    public ReporteBarrio(String barrio, int asignadas, int finalizadas,
                         int pendientes, int incompletas) {
        this.barrio = barrio;
        this.asignadas = asignadas;
        this.finalizadas = finalizadas;
        this.pendientes = pendientes;
        this.incompletas = incompletas;
    }

    public String getBarrio() { return barrio; }
    public int getAsignadas() { return asignadas; }
    public int getFinalizadas() { return finalizadas; }
    public int getPendientes() { return pendientes; }
    public int getIncompletas() { return incompletas; }
}