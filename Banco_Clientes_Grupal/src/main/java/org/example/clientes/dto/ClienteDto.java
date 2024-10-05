package org.example.clientes.dto;

import lombok.Builder;
import lombok.Data;
import org.example.tarjetas.dto.TarjetaDto;
import org.example.usuarios.dto.UsuarioDto;

import java.util.List;

@Data
@Builder
public class ClienteDto {
    private UsuarioDto usuario;
    private List<TarjetaDto> tarjetas;
}
