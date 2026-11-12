/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

/**
 *
 * @author Valeria
 */
public class AppException extends Exception {

    public AppException(String mensaje) {
        super(mensaje);
    }

    public AppException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
