/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */

import java.util.Date;

/**
 * Asignacion: clase de asociación entre Hogar y Encuestador.
 * Un hogar tiene como máximo un encuestador (0..1), por eso no tiene tabla
 * propia: se guarda en HOGAR (ID_ENCUESTADOR, FECHA_INICIO_ASIGNACION,
 * FECHA_FIN_ASIGNACION).
 */
public class Asignacion {

    private Hogar hogar;
    private Encuestador encuestador;
    private Date fechaInicioAsignacion;   // FECHA_INICIO_ASIGNACION
    private Date fechaFinAsignacion;      // FECHA_FIN_ASIGNACION

    public Asignacion(Hogar hogar, Encuestador encuestador,
                      Date fechaInicioAsignacion, Date fechaFinAsignacion) {
        this.hogar = hogar;
        this.encuestador = encuestador;
        this.fechaInicioAsignacion = fechaInicioAsignacion;
        this.fechaFinAsignacion = fechaFinAsignacion;
    }

    public Hogar getHogar() { return hogar; }
    public void setHogar(Hogar hogar) { this.hogar = hogar; }

    public Encuestador getEncuestador() { return encuestador; }
    public void setEncuestador(Encuestador encuestador) { this.encuestador = encuestador; }

    public Date getFechaInicioAsignacion() { return fechaInicioAsignacion; }
    public void setFechaInicioAsignacion(Date f) { this.fechaInicioAsignacion = f; }

    public Date getFechaFinAsignacion() { return fechaFinAsignacion; }
    public void setFechaFinAsignacion(Date f) { this.fechaFinAsignacion = f; }
}