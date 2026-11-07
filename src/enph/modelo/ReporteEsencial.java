/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */
public class ReporteEsencial {

    private String grupoAlimento;
    private int cantidad;

    public ReporteEsencial(String grupoAlimento, int cantidad) {
        this.grupoAlimento = grupoAlimento;
        this.cantidad = cantidad;
    }

    public String getGrupoAlimento() {
        return grupoAlimento;
    }

    public int getCantidad() {
        return cantidad;
    }
}