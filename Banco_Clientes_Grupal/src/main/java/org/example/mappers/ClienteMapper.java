package org.example.mappers;

import org.example.dto.ClienteDto;
import org.example.models.Cliente;

public class ClienteMapper {
    public static Cliente toEntity(ClienteDto clienteDto) {
        if (clienteDto == null) {
            return null;
        }

        return Cliente.builder()
                .usuario(UsuarioMapper.toEntity(clienteDto.getUsuario()))
                .tarjetas(TarjetaMapper.toEntityList(clienteDto.getTarjetas()))
                .build();
    }

    public static ClienteDto toDto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        return ClienteDto.builder()
                .usuario(UsuarioMapper.toDto(cliente.getUsuario()))
                .tarjetas(TarjetaMapper.toDtoList(cliente.getTarjetas()))
                .build();
    }
}

