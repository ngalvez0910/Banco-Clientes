package org.example.clientes.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@Builder
public class Tarjeta {
    private Long id;
    private String nombreTitular;
    private String numeroTarjeta;
    private LocalDate fechaCaducidad;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/yy");

    public String getFechaCaducidadFormateada() {
        return (fechaCaducidad != null) ? fechaCaducidad.format(FORMATTER) : null;
    }

    public void setFechaCaducidadDesdeString(String fecha) {
        try {
            this.fechaCaducidad = LocalDate.parse(fecha, FORMATTER).withDayOfMonth(1);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido: " + fecha, e);
        }
    }
}