package org.example.clientes.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO para representar a un cliente.
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
