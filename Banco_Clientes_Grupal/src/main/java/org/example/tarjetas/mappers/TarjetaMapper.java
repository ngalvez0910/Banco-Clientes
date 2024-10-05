package org.example.tarjetas.mappers;

import org.example.tarjetas.dto.TarjetaDto;
import org.example.tarjetas.model.Tarjeta;

import java.time.LocalDate;
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
                .build();
    }

    public static Tarjeta toTarjetaFromCreateAll(TarjetaGetAll tarjetaGetAll){
        return Tarjeta.builder()
               .id(tarjetaGetAll.getId())
               .nombreTitular(tarjetaGetAll.getNombreTitular())
               .numeroTarjeta(tarjetaGetAll.getNumeroTarjeta())
               .fechaCaducidad(LocalDate.parse(tarjetaGetAll.getFechaCaducidad(), formatter))
               .build();
    }

    public static Tarjeta toTarjetaFromCreateId(TarjetaGetById tarjetaGetById){
        return Tarjeta.builder()
                .id(tarjetaGetById.getId())
                .nombreTitular(tarjetaGetById.getNombreTitular())
                .numeroTarjeta(tarjetaGetById.getNumeroTarjeta())
                .fechaCaducidad(LocalDate.parse(tarjetaGetById.getFechaCaducidad(), formatter))
                .build();
    }
    public static Request toRequest(Tarjeta tarjeta){
        return Request.builder()
                .id(tarjeta.getId())
                .nombreTitular(tarjeta.getNombreTitular())
                .numeroTarjeta(tarjeta.getNumeroTarjeta())
                .fechaCaducidad(tarjeta.getFechaCaducidad().format(formatter))
                .build();
    }

    public static Tarjeta toTarjetaFromCreate(Response response){
        return Tarjeta.builder()
               .id(response.getId())
               .nombreTitular(response.getNombreTitular())
               .numeroTarjeta(response.getNumeroTarjeta())
               .fechaCaducidad(LocalDate.parse(response.getFechaCaducidad(), formatter))
               .build();
    }

    public static Tarjeta toTarjetaFromUpdate(Response response, Long id){
        return Tarjeta.builder()
               .id(id)
               .nombreTitular(response.getNombreTitular())
               .numeroTarjeta(response.getNumeroTarjeta())
               .fechaCaducidad(LocalDate.parse(response.getFechaCaducidad(), formatter))
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
