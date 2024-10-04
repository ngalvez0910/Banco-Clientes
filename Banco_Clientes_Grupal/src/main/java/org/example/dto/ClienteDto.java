package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClienteDto {
    private UsuarioDto usuario;  // Usuario asociado al cliente
    private List<TarjetaDto> tarjetas;  // Lista de tarjetas asociadas al cliente
}
