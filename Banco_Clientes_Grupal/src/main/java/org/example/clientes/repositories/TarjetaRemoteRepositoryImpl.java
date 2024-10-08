package org.example.clientes.repositories;

import io.vavr.control.Either;
import org.example.clientes.mappers.TarjetaMapper;
import org.example.clientes.model.Tarjeta;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;

public class TarjetaRemoteRepositoryImpl implements TarjetaRemoteRepository {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(TarjetaRemoteRepositoryImpl.class);

    public TarjetaRemoteRepositoryImpl(UserService userService) {
        this.userService = userService;
    }

    public Either<String,List<Tarjeta>> getAll() {
        var llamada = userService.getAll();
        try {
            var response = llamada.execute();
            if (!response.isSuccessful()) {
                return Either.left("Error: " + response.code());
            }
            var tarjetas = response.body().getData().stream()
                    .map(TarjetaMapper::toTarjetaFromCreateAll)
                    .toList();
            return Either.right(tarjetas);
        } catch (Exception e) {
            logger.error("Error al obtener todas las tarjetas", e);
            return Either.left("Error al obtener todas las tarjetas: " + e.getMessage());
        }
    }

    public Either<String,Tarjeta> getById(Long id) {
        var llamada = userService.getById(id);
        try {
            var response = llamada.get();
            var tarjeta = TarjetaMapper.toTarjetaFromCreateId(response.getData());
            return Either.right(tarjeta);
        } catch (Exception e) {
            logger.error("Tarjeta no encontrada con id: " + id, e);
            return Either.left("Tarjeta no encontrada con id: " + id);
        }
    }

    public Either<String,Tarjeta> create(Tarjeta tarjeta) {
        var llamada = userService.create(TarjetaMapper.toRequest(tarjeta));
        try {
            var response = llamada.get();
            var nuevaTarjeta = TarjetaMapper.toTarjetaFromCreate(response);
            return Either.right(nuevaTarjeta);
        } catch (Exception e) {
            logger.error("Error al crear la tarjeta", e);
            return Either.left("Error al crear la tarjeta: " + e.getMessage());
        }
    }

    public Either<String,Tarjeta> update(Long id, Tarjeta tarjeta) {
        var llamada = userService.update(id, TarjetaMapper.toRequest(tarjeta));
        try {
            var response = llamada.get();
            var tarjetaActualizada = TarjetaMapper.toTarjetaFromUpdate(response, id);
            return Either.right(tarjetaActualizada);
        } catch (Exception e) {
            if(e.getCause() == null){
                logger.error("Tarjeta no encontrada con id: " + id, e);
                return Either.left("Tarjeta no encontrada con id: " + id);
            } else {
                logger.error("Error al actualizar la tarjeta con id: " + id, e);
                return Either.left("Error al actualizar la tarjeta: " + e.getMessage());
            }
        }
    }

    public Either<String,Boolean> delete(Long id) {
        var llamada = userService.delete(id);
        try {
            llamada.get();
            return Either.right(true);
        } catch (Exception e) {
            if (e.getCause() == null){
                logger.error("Tarjeta no encontrada con id: " + id, e);
                return Either.left("Tarjeta no encontrada con id: " + id);
            } else {
                logger.error("Error al eliminar la tarjeta con id: " + id, e);
                return Either.left("Error al eliminar la tarjeta: " + e.getMessage());
            }
        }
    }
}