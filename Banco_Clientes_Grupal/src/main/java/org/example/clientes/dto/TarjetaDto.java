package org.example.clientes.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO para representar a una tarjeta de crédito.
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 */
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
