package org.example.clientes.repositories;


import org.example.clientes.model.Cliente;
import org.example.database.LocalDataBaseManager;
import org.example.tarjetas.repositories.TarjetaRemoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClienteRepositoryImpl implements ClienteRepository {
    private final Logger logger = LoggerFactory.getLogger(ClienteRepositoryImpl.class);
    private final LocalDataBaseManager localDataBaseManager;

    public ClienteRepositoryImpl(LocalDataBaseManager localDataBaseManager) {
        this.localDataBaseManager = localDataBaseManager;
    }
    @Override
    public List<Cliente> findAllCliente()  {
        logger.debug("Obteniendo todos los clientes...");
        List<Cliente> clients = new ArrayList<>();
        String query = "SELECT * FROM Cliente";
        try (Connection connection = localDataBaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    clients.add(Cliente.builder()
                            .usuario(findUserById(resultSet.getObject("usuarioID", UUID.class)))
                            .tarjetas(findAllTarjetasById(resultSet.getObject("usuarioID", UUID.class)))
                            .build());
                }
            }
        } catch (SQLException e) {
            logger.error("Error al obtener todos los clientes", e);
        }
        return clients;
    }

    @Override
    public Cliente findClienteById(UUID id) {
        logger.debug("Obteniendo el cliente con id: {}", id);
        Cliente client = null;
        String query = "SELECT * FROM Cliente WHERE id = ?";
        try (Connection connection = localDataBaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    client = Cliente.builder()
                            .usuario(findUserById(resultSet.getObject("usuarioID", UUID.class)))
                            .tarjetas(findAllTarjetasById(resultSet.getObject("usuarioID", UUID.class)))
                            .build();
                }
            }
        } catch (SQLException e) {
            logger.error("Error al obtener el cliente con id: {}", id, e);
        }
        return client;
    }

    @Override
    public List<Cliente> findClienteByName(String name) {
        logger.debug("Obteniendo el cliente con nombre: {}", name);
        String query = "SELECT * FROM Cliente WHERE name = ?";
        try (Connection connection = localDataBaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Cliente> cliente = new ArrayList<>();
                while (resultSet.next()) {
                    cliente.add(Cliente.builder()
                            .usuario(findUserById(resultSet.getObject("usuarioID", UUID.class)))
                            .tarjetas(findAllTarjetasById(resultSet.getObject("usuarioID", UUID.class)))
                            .build());
                }
                return cliente;
            }
        } catch (SQLException e) {
            logger.error("Error al obtener el cliente con nombre: {}", name, e);
        }
        return List.of();
    }

    @Override
    public Cliente saveCliente(Cliente cliente) {
        logger.debug("Guardando el cliente: {}", cliente);
        String query = "INSERT INTO Cliente (id, usuarioID) VALUES (?, ?)";
        try (Connection connection = localDataBaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, cliente.getId());
            statement.setObject(2, cliente.getUsuario().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al guardar el cliente: {}", cliente, e);
        }
        return cliente;
    }


    @Override
    public Boolean deleteClienteById(UUID id) {
        logger.debug("Eliminando el cliente con id: {}", id);
        String query = "DELETE FROM Cliente WHERE id = ?";
        try (Connection connection = localDataBaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al eliminar el cliente con id: {}", id, e);
        }
        return true;
    }

    @Override
    public Boolean deleteAllCliente() {
        logger.debug("Eliminando todos los clientes...");
        String query = "DELETE FROM Cliente";
        try (Connection connection = localDataBaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al eliminar todos los clientes", e);
        }
        return true;
    }

}