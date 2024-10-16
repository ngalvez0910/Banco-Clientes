package org.example.clientes.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO para representar a un cliente.
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
@Data
@Builder
public class ClienteDto {

    private Long id;
    private UsuarioDto usuario;
    private List<TarjetaDto> tarjeta;
    private String createdAt;
    private String updatedAt;
}
