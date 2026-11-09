/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */
/**
 * Encuestador: hereda de Usuario (igual que en el diagrama de dominio).
 * Sus datos salen de la tabla ENCUESTADOR; el ID_ENCUESTADOR
 * ya está en Usuario (getIdEncuestador / setIdEncuestador).
 */
public class Encuestador extends Usuario {
 
    public Encuestador() {
        super();
        setRol("ENCUESTADOR");
    }
}