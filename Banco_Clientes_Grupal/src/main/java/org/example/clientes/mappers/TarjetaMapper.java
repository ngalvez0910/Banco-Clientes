package org.example.clientes.mappers;

import org.example.clientes.dto.TarjetaDto;
import org.example.clientes.model.Tarjeta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase encargada de mapear entre las entidades Tarjeta y TarjetaDto.
 * Proporciona métodos para convertir objetos de tipo Tarjeta a TarjetaDto y viceversa,
 * así como listas de ambos tipos.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 */
public class TarjetaMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Convierte un objeto TarjetaDto en un objeto Tarjeta.
     *
     * @param tarjetaDto El objeto TarjetaDto a convertir
     * @return El objeto Tarjeta resultante, o null si el objeto TarjetaDto es null
     */
    public static Tarjeta toEntity(TarjetaDto tarjetaDto) {
        if (tarjetaDto == null) {
            return null;
        }

        return Tarjeta.builder()
                .id(tarjetaDto.getId())
                .nombreTitular(tarjetaDto.getNombreTitular())
                .numeroTarjeta(tarjetaDto.getNumeroTarjeta())
                .fechaCaducidad(tarjetaDto.getFechaCaducidad() != null
                        ? LocalDate.parse(tarjetaDto.getFechaCaducidad(), formatter)
                        : null)
                .createdAt(tarjetaDto.getCreatedAt() != null
                        ? LocalDateTime.parse(tarjetaDto.getCreatedAt(), formatter)
                        : null)
                .updatedAt(tarjetaDto.getUpdatedAt() != null
                        ? LocalDateTime.parse(tarjetaDto.getUpdatedAt(), formatter)
                        : null)
                .build();
    }

    /**
     * Convierte un objeto Tarjeta en un objeto TarjetaDto.
     *
     * @param tarjeta El objeto Tarjeta a convertir
     * @return El objeto TarjetaDto resultante, o null si el objeto Tarjeta es null
     */
    public static TarjetaDto toDto(Tarjeta tarjeta) {
        if (tarjeta == null) {
            return null;
        }

        return TarjetaDto.builder()
                .id(tarjeta.getId())
                .nombreTitular(tarjeta.getNombreTitular())
                .numeroTarjeta(tarjeta.getNumeroTarjeta())
                .fechaCaducidad(tarjeta.getFechaCaducidad() != null
                        ? tarjeta.getFechaCaducidad().format(formatter)
                        : null)
                .createdAt(tarjeta.getCreatedAt() != null
                        ? tarjeta.getCreatedAt().format(formatter)
                        : null)
                .updatedAt(tarjeta.getUpdatedAt() != null
                        ? tarjeta.getUpdatedAt().format(formatter)
                        : null)
                .build();
    }

    /**
     * Convierte una lista de objetos TarjetaDto en una lista de objetos Tarjeta.
     *
     * @param tarjetasDto Lista de objetos TarjetaDto a convertir
     * @return Lista de objetos Tarjeta resultantes
     */
    public static List<Tarjeta> toEntityList(List<TarjetaDto> tarjetasDto) {
        return tarjetasDto.stream()
                .map(TarjetaMapper::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una lista de objetos Tarjeta en una lista de objetos TarjetaDto.
     *
     * @param tarjetas Lista de objetos Tarjeta a convertir
     * @return Lista de objetos TarjetaDto resultantes
     */
    public static List<TarjetaDto> toDtoList(List<Tarjeta> tarjetas) {
        return tarjetas.stream()
                .map(TarjetaMapper::toDto)
                .collect(Collectors.toList());
    }
}
