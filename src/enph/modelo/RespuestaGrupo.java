/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

import java.math.BigDecimal;
/**
 * Respuesta ya guardada de un grupo de alimentos.
 * CaracterizacionDAO.obtener() la crea al leer la base de datos, y
 * FrecuenciaGasto y PrimeraCondicion la usan en el modo edición para
 * mostrar lo que el encuestador registró antes.
 */
public class RespuestaGrupo {
    // final: los datos se asignan una sola vez en el constructor
    private final int idFrecuencia; // id de la frecuencia de compra elegida (FRECUENCIA_COMPRA)
    private final BigDecimal valor; // valor de la última compra; null si la frecuencia no lo pidió
    private final Integer intencion; // 1 = Sí, 0 = No; null si no se preguntó

    // Se usan BigDecimal e Integer (y no double e int) porque pueden valer null,
    // que en la base de datos significa "esta pregunta no se hizo"
    public RespuestaGrupo(int idFrecuencia, BigDecimal valor, Integer intencion) {
        this.idFrecuencia = idFrecuencia;
        this.valor = valor;
        this.intencion = intencion;
    }

    public int getIdFrecuencia() { return idFrecuencia; }
    public BigDecimal getValor() { return valor; }
    public Integer getIntencion() { return intencion; }
}