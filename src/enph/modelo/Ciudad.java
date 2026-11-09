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
 * Ciudad. Dominio: Ciudad (idCiudad : int, nombre : String) que pertenece
 * a un Departamento (Ciudad 0..* - 1 Departamento). Tabla: CIUDAD.
 *
 * Reemplaza a ItemCiudad: tiene getId() (texto) y getNombre(), y su
 * toString() devuelve "Ciudad - Departamento", igual que el combo actual.
 */
public class Ciudad {

    private int idCiudad;               // ID_CIUDAD
    private String nombre;              // NOMBRE_CIUDAD
    private Departamento departamento;  // ID_DEPARTAMENTO

    public Ciudad(int idCiudad, String nombre, Departamento departamento) {
        this.idCiudad = idCiudad;
        this.nombre = nombre;
        this.departamento = departamento;
    }

    public int getIdCiudad() { return idCiudad; }
    public void setIdCiudad(int idCiudad) { this.idCiudad = idCiudad; }

    /** El id como texto: las columnas ID_CIUDAD de la base son VARCHAR2. */
    public String getId() { return String.valueOf(idCiudad); }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }

    @Override
    public String toString() {
        return departamento == null ? nombre : nombre + " - " + departamento.getNombre();
    }
}