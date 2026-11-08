/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enph.modelo;

/**
 *
 * @author Valeria
 */

public class ReporteTiempoDetalleDTO {

    private String idHogar;
    private String nombreHogar;
    private Long tiempoSegundos;

    public ReporteTiempoDetalleDTO(
            String idHogar,
            String nombreHogar,
            Long tiempoSegundos) {

        this.idHogar = idHogar;
        this.nombreHogar = nombreHogar;
        this.tiempoSegundos = tiempoSegundos;
    }

    public String getIdHogar() {
        return idHogar;
    }

    public String getNombreHogar() {
        return nombreHogar;
    }

    public Long getTiempoSegundos() {
        return tiempoSegundos;
    }

    public String getTiempoFormato() {

        if (tiempoSegundos == null) {
            return "Sin finalizar";
        }

        long horas = tiempoSegundos / 3600;
        long minutos = (tiempoSegundos % 3600) / 60;
        long segundos = tiempoSegundos % 60;

        return String.format(
                "%02d:%02d:%02d",
                horas,
                minutos,
                segundos
        );
    }
}
