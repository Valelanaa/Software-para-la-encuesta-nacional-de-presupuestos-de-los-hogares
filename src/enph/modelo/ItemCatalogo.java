/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *Elemento genérico de un catálogo: un id y un nombre.
 * Sirve tanto para los grupos de alimentos (GRUPO_DE_ALIMENTOS) como
 * para las frecuencias de compra (FRECUENCIA_COMPRA), porque las dos
 * tablas tienen la misma forma: un identificador y un texto.
 * @author SALA-2
 */
public class ItemCatalogo {
    // final: los valores se asignan una sola vez en el constructor
    // y no se pueden cambiar después
    private final int id;  // llave primaria del catálogo (lo que se guarda en la base)
    private final String nombre; // texto que ve el usuario en pantalla

    public ItemCatalogo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    /**
     * Devuelve el nombre del ítem. Swing llama a toString() para mostrar el
     * texto de cada elemento en listas y combos, por eso aquí se devuelve
     * el nombre y no el texto por defecto de Java (ej. "ItemCatalogo@1a2b3c").
     */
    @Override
    public String toString() { return nombre; }
}
