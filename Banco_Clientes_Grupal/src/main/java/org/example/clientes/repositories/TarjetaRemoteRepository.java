package org.example.clientes.repositories;

import io.vavr.control.Either;
import org.example.clientes.model.Tarjeta;

import java.util.List;

public interface TarjetaRemoteRepository {
    public Either<String, List<Tarjeta>> getAll();
    public Either<String, Tarjeta> getById(Long id);
    public Either<String, Tarjeta> create(Tarjeta tarjeta);
    public Either<String, Tarjeta> update(Long id, Tarjeta tarjeta);
    public Either<String, Boolean> delete(Long id);
}
