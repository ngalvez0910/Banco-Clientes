package org.example.clientes.mappers;

import org.example.clientes.dto.ClienteDto;
import org.example.clientes.model.Cliente;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase encargada de mapear entre las entidades Cliente y ClienteDto.
 * Proporciona métodos para convertir objetos de tipo Cliente a ClienteDto y viceversa.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 */
public class ClienteMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Convierte un objeto Cliente en un objeto ClienteDto.
     *
     * @param cliente El objeto Cliente a convertir
     * @return El objeto ClienteDto resultante, o null si el objeto Cliente es null
     */
    public static ClienteDto toDto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        return ClienteDto.builder()
                .id(cliente.getId())
                .usuario(UsuarioMapper.toDto(cliente.getUsuario()))
                .tarjeta(TarjetaMapper.toDtoList(cliente.getTarjeta()))
                .createdAt(cliente.getCreatedAt().format(formatter))
                .updatedAt(cliente.getUpdatedAt().format(formatter))
                .build();
    }

    /**
     * Convierte un objeto ClienteDto en un objeto Cliente.
     *
     * @param dto El objeto ClienteDto a convertir
     * @return El objeto Cliente resultante, o null si el objeto ClienteDto es null
     */
    public static Cliente fromDto(ClienteDto dto) {
        if (dto == null) {
            return null;
        }

        return Cliente.builder()
                .id(dto.getId())
                .usuario(UsuarioMapper.toEntity(dto.getUsuario()))
                .tarjeta(TarjetaMapper.toEntityList(dto.getTarjeta()))
                .createdAt(LocalDateTime.parse(dto.getCreatedAt(), formatter))
                .updatedAt(LocalDateTime.parse(dto.getUpdatedAt(), formatter))
                .build();
    }
}
