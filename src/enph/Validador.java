/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph;

/**
 *
 * @author Valeria
 */

import java.util.regex.Pattern;

public class Validador {

    private static final Pattern DOCUMENTO = Pattern.compile("^[0-9]{6,10}$");

    private static final Pattern TELEFONO = Pattern.compile("^([0-9]{7}|[0-9]{10})$");

    private static final Pattern NOMBRE = Pattern.compile("^\\p{L}+( \\p{L}+)*$");

    private static final Pattern CORREO = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Dirección compatible con la restricción de Oracle
    private static final Pattern DIRECCION = Pattern.compile("^[A-Za-z0-9#.,/ -]+$");

    public static boolean documentoValido(String s) {
        return s != null && DOCUMENTO.matcher(s.trim()).matches();
    }

    public static boolean telefonoValido(String s) {
        return s != null && TELEFONO.matcher(s.trim()).matches();
    }

    public static boolean nombreValido(String s) {
        return s != null && s.trim().length() >= 3 && NOMBRE.matcher(s.trim()).matches();
    }

    public static boolean correoValido(String s) {
        return s != null && CORREO.matcher(s.trim()).matches();
    }

    // Validación del nombre del hogar
    public static boolean nombreHogarValido(String s) {
        return nombreValido(s);
    }

    // Validación de la dirección del hogar
    public static boolean direccionValida(String s) {
        return s != null && s.trim().length() >= 11 && s.trim().matches(".*[0-9].*") && DIRECCION.matcher(s.trim()).matches();
    }
}