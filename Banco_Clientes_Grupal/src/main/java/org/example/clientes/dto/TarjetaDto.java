package org.example.clientes.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TarjetaDto {
    private Long id;
    private String nombreTitular;
    private String numeroTarjeta;
    private String fechaCaducidad;
    private String createdAt;
    private String updatedAt;
}
