/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package enph.controlador;

import enph.modelo.FrecuenciaCompra;
import enph.modelo.GrupoAlimento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Mantiene el estado de una encuesta durante su diligenciamiento.
 * No realiza consultas ni operaciones sobre la base de datos.
 */
public class SesionEncuesta {

    private final int idHogar;
    private final int idEncuesta;
    private final List<GrupoAlimento> grupos;
    private final List<FrecuenciaCompra> frecuencias;

    private int grupoActual = 0;
    private final boolean edicion;

    public SesionEncuesta(
            int idHogar,
            int idEncuesta,
            List<GrupoAlimento> grupos,
            List<FrecuenciaCompra> frecuencias,
            boolean edicion) {

        if (idHogar <= 0 || idEncuesta <= 0) {
            throw new IllegalArgumentException(
                    "Los identificadores de hogar y encuesta deben ser válidos."
            );
        }

        if (grupos == null || grupos.isEmpty()) {
            throw new IllegalArgumentException(
                    "La encuesta debe tener grupos de alimentos."
            );
        }

        if (frecuencias == null || frecuencias.isEmpty()) {
            throw new IllegalArgumentException(
                    "La encuesta debe tener frecuencias de compra."
            );
        }

        this.idHogar = idHogar;
        this.idEncuesta = idEncuesta;
        this.grupos = Collections.unmodifiableList(
                new ArrayList<>(grupos)
        );
        this.frecuencias = Collections.unmodifiableList(
                new ArrayList<>(frecuencias)
        );
        this.edicion = edicion;
    }

    // Datos de la encuesta

    public int getIdHogar() {
        return idHogar;
    }

    public int getIdEncuesta() {
        return idEncuesta;
    }

    public List<FrecuenciaCompra> getFrecuencias() {
        return frecuencias;
    }

    public List<GrupoAlimento> getGrupos() {
        return grupos;
    }

    public boolean isEdicion() {
        return edicion;
    }

    // Navegación entre grupos

    public int getGrupoActual() {
        return grupoActual;
    }

    public int getTotalGrupos() {
        return grupos.size();
    }

    public GrupoAlimento getGrupo() {
        return grupos.get(grupoActual);
    }

    public boolean hayMasGrupos() {
        return grupoActual < grupos.size() - 1;
    }

    public void avanzar() {
        if (hayMasGrupos()) {
            grupoActual++;
        }
    }

    public boolean hayAnterior() {
        return grupoActual > 0;
    }

    public void retroceder() {
        if (hayAnterior()) {
            grupoActual--;
        }
    }

    // Nombres para el combo de frecuencias

    public String[] getNombresFrecuencias() {
        String[] nombres = new String[frecuencias.size()];

        for (int i = 0; i < nombres.length; i++) {
            nombres[i] = frecuencias.get(i).getNombre();
        }

        return nombres;
    }
}
