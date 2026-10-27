/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/** Una ciudad para el combo: el id se guarda en la base, el nombre se ve en pantalla. */
public class ItemCiudad {
    private final String id;      // ID_CIUDAD
    private final String nombre;  // "Ciudad - Departamento"

    public ItemCiudad(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() { return nombre; }
}
