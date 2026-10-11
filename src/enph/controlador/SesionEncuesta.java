/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.controlador;

import enph.conexion.CatalogoDAO;
import enph.conexion.EncuestaDAO;
import enph.conexion.ItemCatalogo;
import java.util.List;

/**
 * Guarda el estado de una encuesta mientras el encuestador la va llenando.
 * Las pantallas FrecuenciaGasto y PrimeraCondicion se pasan este mismo objeto
 * de una a otra, para saber en qué hogar, en qué encuesta y en qué grupo de
 * alimentos van, y si es una encuesta nueva o una edición.
 */

public class SesionEncuesta {

    private final int idHogar;                    // hogar (familia) que se está encuestando
    private final int idEncuesta;                 // encuesta en la que se guardan las respuestas
    private final List<ItemCatalogo> grupos;      // los 25 grupos de alimentos, en orden
    private final List<ItemCatalogo> frecuencias; // opciones de frecuencia de compra
    private int grupoActual = 0;                  // posición del grupo en pantalla (0 = el primero)
    private boolean edicion = false;              // true si se está editando una encuesta existente
    
    /**
     * Constructor privado: una sesión solo se crea con iniciar() o editar(),
     * que antes cargan y validan los datos necesarios.
     */
    private SesionEncuesta(int idHogar, int idEncuesta,
                           List<ItemCatalogo> grupos, List<ItemCatalogo> frecuencias) {
        this.idHogar = idHogar;
        this.idEncuesta = idEncuesta;
        this.grupos = grupos;
        this.frecuencias = frecuencias;
    }

    /** Carga los catálogos y crea la encuesta. Devuelve null si algo falla. */
    public static SesionEncuesta iniciar(int idHogar) {
        CatalogoDAO catalogoDAO = new CatalogoDAO();
        List<ItemCatalogo> grupos = catalogoDAO.listarGrupos();
        List<ItemCatalogo> frecuencias = catalogoDAO.listarFrecuencias();
        // Sin grupos o frecuencias no hay encuesta posible
        if (grupos.isEmpty() || frecuencias.isEmpty()) {
            return null;
        }
        // crearEncuesta devuelve -1 si falla el INSERT
        int idEncuesta = new EncuestaDAO().crearEncuesta(idHogar);
        if (idEncuesta < 0) {
            return null;
        }
        return new SesionEncuesta(idHogar, idEncuesta, grupos, frecuencias);
    }
    /** Reabre la encuesta EXISTENTE del hogar para editarla. No crea una
     * encuesta nueva: usa la que ya tiene y marca la sesión como edición,
     * para que las pantallas muestren las respuestas ya guardadas.
     */
public static SesionEncuesta editar(int idHogar) {
    // obtenerIdEncuesta devuelve -1 si el hogar no tiene encuesta
    int idEncuesta = new EncuestaDAO().obtenerIdEncuesta(idHogar);
    if (idEncuesta < 0) {
        return null;
    }

    CatalogoDAO catalogoDAO = new CatalogoDAO();
    List<ItemCatalogo> grupos = catalogoDAO.listarGrupos();
    List<ItemCatalogo> frecuencias = catalogoDAO.listarFrecuencias();

    if (grupos.isEmpty() || frecuencias.isEmpty()) {
        return null;
    }
    
    SesionEncuesta sesion = new SesionEncuesta(idHogar, idEncuesta, grupos, frecuencias);
    sesion.edicion = true;
    return sesion;
}

    //Datos de la encuesta
    public int getIdHogar() { return idHogar; }
    public int getIdEncuesta() { return idEncuesta; }
    public List<ItemCatalogo> getFrecuencias() { return frecuencias; }
    public boolean isEdicion() { return edicion; }
    //Navegación entre los grupos de alimentos
    public int getGrupoActual() { return grupoActual; }
    public int getTotalGrupos() { return grupos.size(); }
    public ItemCatalogo getGrupo() { return grupos.get(grupoActual); } //Grupo de alimentos que se muestra en pantalla ahora.
    public boolean hayMasGrupos() { return grupoActual < grupos.size() - 1; } //true si todavía queda algún grupo después del actual
    public void avanzar() { grupoActual++; } //Pasa al siguiente grupo. Llamar solo si hayMasGrupos() es true
    /**
     * Nombres de las frecuencias como arreglo de texto, que es lo que necesita
     * el combo de FrecuenciaGasto. Están en el mismo orden que la lista, así
     * que la posición elegida en el combo es la posición en getFrecuencias().
     */
    public String[] getNombresFrecuencias() {
        String[] nombres = new String[frecuencias.size()];
        for (int i = 0; i < nombres.length; i++) {
            nombres[i] = frecuencias.get(i).getNombre();
        }
        return nombres;
    }

    public boolean hayAnterior() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void retroceder() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}