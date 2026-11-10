/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */

import java.math.BigDecimal;

/**
 * Caracterización de compra de un grupo de alimentos dentro de una encuesta.
 * Dominio: CaracterizacionCompra (idCaracterizacion, valorUltimaCompra, intencionCompra),
 * asociada a una Encuesta, un GrupoAlimento y una FrecuenciaCompra.
 * Tablas: CARACTERIZACION_COMPRA y DETALLE_COMPRA (valor e intención).
 * Reemplaza a RespuestaGrupo.
 */
public class CaracterizacionCompra {

    private int idCaracterizacion;           // ID_CARACTERIZACION (0 si aún no se ha guardado)
    private BigDecimal valorUltimaCompra;    // null si la frecuencia no lo pidió
    private Boolean intencionCompra;         // true = Sí, false = No, null si no se preguntó
    private Encuesta encuesta;
    private GrupoAlimento grupo;
    private FrecuenciaCompra frecuencia;

    public CaracterizacionCompra(Encuesta encuesta, GrupoAlimento grupo, FrecuenciaCompra frecuencia,
                                 BigDecimal valorUltimaCompra, Boolean intencionCompra) {
        this.encuesta = encuesta;
        this.grupo = grupo;
        this.frecuencia = frecuencia;
        this.valorUltimaCompra = valorUltimaCompra;
        this.intencionCompra = intencionCompra;
    }

    public int getIdCaracterizacion() { return idCaracterizacion; }
    public void setIdCaracterizacion(int idCaracterizacion) { this.idCaracterizacion = idCaracterizacion; }

    public BigDecimal getValorUltimaCompra() { return valorUltimaCompra; }
    public void setValorUltimaCompra(BigDecimal valorUltimaCompra) { this.valorUltimaCompra = valorUltimaCompra; }

    public Boolean getIntencionCompra() { return intencionCompra; }
    public void setIntencionCompra(Boolean intencionCompra) { this.intencionCompra = intencionCompra; }

    public Encuesta getEncuesta() { return encuesta; }
    public void setEncuesta(Encuesta encuesta) { this.encuesta = encuesta; }

    public GrupoAlimento getGrupo() { return grupo; }
    public void setGrupo(GrupoAlimento grupo) { this.grupo = grupo; }

    public FrecuenciaCompra getFrecuencia() { return frecuencia; }
    public void setFrecuencia(FrecuenciaCompra frecuencia) { this.frecuencia = frecuencia; }
}
