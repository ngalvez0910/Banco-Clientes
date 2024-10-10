package org.example.clientes.services;

import io.vavr.control.Either;
import org.example.clientes.errors.ClienteError;
import org.example.clientes.model.Cliente;

import java.util.List;

public interface ClienteService {
    Either<ClienteError, List<Cliente>> getAll();

    Either<ClienteError, Cliente> getById(int id);

    Either<ClienteError, Cliente> create(Cliente cliente);

    Either<ClienteError, Cliente> update(long id, Cliente cliente);

    Either<ClienteError, Cliente> delete(long id);
}
