/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */
public class FrecuenciaCompra {
    private final int idFrecuencia;
    private final String nombreFrecuencia;

    public FrecuenciaCompra(int idFrecuencia, String nombreFrecuencia) {
        this.idFrecuencia = idFrecuencia;
        this.nombreFrecuencia = nombreFrecuencia;
    }

    public int getIdFrecuencia() { return idFrecuencia; }
    public String getNombreFrecuencia() { return nombreFrecuencia; }

    // Atajos: las pantallas usan getId() y getNombre()
    public int getId() { return idFrecuencia; }
    public String getNombre() { return nombreFrecuencia; }

    @Override
    public String toString() { return nombreFrecuencia; }
}