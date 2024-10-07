package org.example.clientes.repositories;

import org.example.clientes.model.Cliente;

import java.util.List;
import java.util.UUID;

public interface ClienteRepository {
    List<Cliente> findAllCliente();
    Cliente findClienteById(UUID id);
    List<Cliente> findClienteByName(String name);
    Cliente saveCliente(Cliente client);
    Boolean deleteClienteById(UUID id);
    Boolean deleteAllCliente();
}