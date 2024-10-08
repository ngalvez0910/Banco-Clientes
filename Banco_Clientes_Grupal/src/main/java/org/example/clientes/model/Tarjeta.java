package org.example.clientes.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Tarjeta {
    private Long id;
    private String nombreTitular;
    private String numeroTarjeta;
    private LocalDate fechaCaducidad;
}