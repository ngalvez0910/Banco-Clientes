package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClienteDto {
    private Long id;
    private UsuarioDTO usuario;  // Usuario asociado al cliente
    private List<TarjetaDTO> tarjetas;  // Lista de tarjetas asociadas al cliente
}
