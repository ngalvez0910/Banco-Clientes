package org.example.clientes.mappers;

import org.example.clientes.dto.ClienteDto;
import org.example.clientes.model.Cliente;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClienteMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static ClienteDto toDto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        return ClienteDto.builder()
                .id(cliente.getId())
                .usuario(UsuarioMapper.toDto(cliente.getUsuario()))
                .tarjeta(TarjetaMapper.toDtoList(cliente.getTarjeta()))
                .createdAt(cliente.getCreatedAt().format(formatter)) // Convertir a String
                .updatedAt(cliente.getUpdatedAt().format(formatter)) // Convertir a String
                .build();
    }

    public static Cliente fromDto(ClienteDto dto) {
        if (dto == null) {
            return null;
        }

        return Cliente.builder()
                .id(dto.getId())
                .usuario(UsuarioMapper.toEntity(dto.getUsuario()))
                .tarjeta(TarjetaMapper.toEntityList(dto.getTarjeta()))
                .createdAt(LocalDateTime.parse(dto.getCreatedAt(), formatter)) // Convertir de String
                .updatedAt(LocalDateTime.parse(dto.getUpdatedAt(), formatter)) // Convertir de String
                .build();
    }
}
