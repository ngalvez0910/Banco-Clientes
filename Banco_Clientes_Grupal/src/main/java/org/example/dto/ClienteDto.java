package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClienteDto {
    private UsuarioDto usuario;
    private List<TarjetaDto> tarjetas;
}
