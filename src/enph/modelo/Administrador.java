/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */
/** Administrador: hereda de Usuario (igual que en el diagrama de dominio). */
public class Administrador extends Usuario {
 
    public Administrador() {
        super();
        setRol("ADMINISTRADOR");
    }
}