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
 * Modelo de la cabeza de hogar: guarda los datos de la tabla CABEZA_DE_HOGAR
 * que se muestran y se editan en la pantalla EditarCabezaHogar.
 */
public class CabezaHogar {

    private String idCabezaHogar;      // ID_CABEZA_DE_HOGAR (llave primaria)
    private String idHogar;            // ID_HOGAR al que pertenece
    private String nombreCompleto;     // NOMBRE_COMPLETO
    private String documentoIdentidad; // DOCUMENTO_IDENTIDAD
    private String correo;             // CORREO_ELECTRONICO
    private String telefono;           // TELEFONO

    public String getIdCabezaHogar() { return idCabezaHogar; }
    public void setIdCabezaHogar(String idCabezaHogar) { this.idCabezaHogar = idCabezaHogar; }

    public String getIdHogar() { return idHogar; }
    public void setIdHogar(String idHogar) { this.idHogar = idHogar; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}