/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */

public class ReporteTiempoDTO {

    private String idEncuestador;
    private String nombreEncuestador;
    private Long tiempoPromedioSegundos;

    public ReporteTiempoDTO(
            String idEncuestador,
            String nombreEncuestador,
            Long tiempoPromedioSegundos) {

        this.idEncuestador = idEncuestador;
        this.nombreEncuestador = nombreEncuestador;
        this.tiempoPromedioSegundos = tiempoPromedioSegundos;
    }

    public String getIdEncuestador() {
        return idEncuestador;
    }

    public String getNombreEncuestador() {
        return nombreEncuestador;
    }

    public Long getTiempoPromedioSegundos() {
        return tiempoPromedioSegundos;
    }

    public String getTiempoPromedioFormato() {

        if (tiempoPromedioSegundos == null) {
            return "Sin encuestas finalizadas";
        }

        long segundos = tiempoPromedioSegundos;

        long horas = segundos / 3600;
        long minutos = (segundos % 3600) / 60;
        long segundosRestantes = segundos % 60;

        return String.format(
                "%02d:%02d:%02d",
                horas,
                minutos,
                segundosRestantes
        );
    }
}
