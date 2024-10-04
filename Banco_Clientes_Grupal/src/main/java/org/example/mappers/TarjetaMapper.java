package org.example.mappers;

import org.example.dto.TarjetaDto;
import org.example.models.Tarjeta;

import java.util.List;
import java.util.stream.Collectors;

public class TarjetaMapper {
    public static Tarjeta toEntity(TarjetaDto tarjetaDto) {
        if (tarjetaDto == null) {
            return null;
        }

        return Tarjeta.builder()
                .id(tarjetaDto.getId())
                .nombreTitular(tarjetaDto.getNombreTitular())
                .numeroTarjeta(tarjetaDto.getNumeroTarjeta())
                .fechaCaducidad(tarjetaDto.getFechaCaducidad())
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
                .fechaCaducidad(tarjeta.getFechaCaducidad())
                .build();
    }

    public static List<Tarjeta> toEntityList(List<TarjetaDto> tarjetaDtos) {
        return tarjetaDtos.stream()
                .map(TarjetaMapper::toEntity)
                .collect(Collectors.toList());
    }

    public static List<TarjetaDto> toDtoList(List<Tarjeta> tarjetas) {
        return tarjetas.stream()
                .map(TarjetaMapper::toDto)
                .collect(Collectors.toList());
    }
}
