package org.example.clientes.mappers;

import org.example.clientes.dto.TarjetaDto;
import org.example.clientes.model.Tarjeta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TarjetaMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
                .createdAt(tarjeta.getCreatedAt()!= null
                        ? tarjeta.getCreatedAt().format(formatter)
                        : null)
                .updatedAt(tarjeta.getUpdatedAt()!= null
                        ? tarjeta.getUpdatedAt().format(formatter)
                        : null)
                .build();
    }

    public static List<Tarjeta> toEntityList(List<TarjetaDto> tarjetasDto) {
        return tarjetasDto.stream()
                .map(TarjetaMapper::toEntity)
                .collect(Collectors.toList());
    }

    public static List<TarjetaDto> toDtoList(List<Tarjeta> tarjetas) {
        return tarjetas.stream()
                .map(TarjetaMapper::toDto)
                .collect(Collectors.toList());
    }
}
