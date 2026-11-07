/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */
public class ReporteRendimientoEncuestador {

    private String idEncuestador;
    private String nombreEncuestador;
    private int realizadas;
    private int pendientes;

    public ReporteRendimientoEncuestador(
            String idEncuestador,
            String nombreEncuestador,
            int realizadas,
            int pendientes) {

        this.idEncuestador = idEncuestador;
        this.nombreEncuestador = nombreEncuestador;
        this.realizadas = realizadas;
        this.pendientes = pendientes;
    }

    public String getIdEncuestador() {
        return idEncuestador;
    }

    public String getNombreEncuestador() {
        return nombreEncuestador;
    }

    public int getRealizadas() {
        return realizadas;
    }

    public int getPendientes() {
        return pendientes;
    }
}
