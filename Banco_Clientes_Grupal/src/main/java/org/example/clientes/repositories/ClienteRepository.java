package org.example.clientes.repositories;

import org.example.clientes.model.Cliente;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    List<Cliente> getAll();

    Optional<Cliente> getById(long id);

    Cliente create(Cliente cliente);

    Cliente update(long id, Cliente cliente);

    boolean delete(long id);

    boolean deleteAll();
}
