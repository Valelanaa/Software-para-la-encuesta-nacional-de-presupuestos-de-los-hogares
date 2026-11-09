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
 * Encuesta. Dominio: Encuesta (idEncuesta, fechaInicio, fechaFin, estado)
 * asociada a un Hogar. Tabla: ENCUESTA.
 *
 * estado: true = completa (ENCUESTA.ESTADO = '0'),
 *         false = incompleta (ENCUESTA.ESTADO = '1').
 */
public class Encuesta {

    private int idEncuesta;     // ID_ENCUESTA
    private Date fechaInicio;   // FECHA_INICIO
    private Date fechaFin;      // FECHA_FIN (null si no ha terminado)
    private boolean estado;     // ESTADO
    private Hogar hogar;        // ID_HOGAR

    public Encuesta(int idEncuesta, Date fechaInicio, Date fechaFin,
                    boolean estado, Hogar hogar) {
        this.idEncuesta = idEncuesta;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.hogar = hogar;
    }

    public int getIdEncuesta() { return idEncuesta; }
    public void setIdEncuesta(int idEncuesta) { this.idEncuesta = idEncuesta; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public Hogar getHogar() { return hogar; }
    public void setHogar(Hogar hogar) { this.hogar = hogar; }
}