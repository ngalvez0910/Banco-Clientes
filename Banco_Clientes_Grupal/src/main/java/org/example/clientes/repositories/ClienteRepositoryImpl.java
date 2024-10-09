package org.example.clientes.repositories;

import org.example.clientes.model.Cliente;
import org.example.database.LocalDataBaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteRepositoryImpl implements ClienteRepository {
    private final Logger logger = LoggerFactory.getLogger(ClienteRepositoryImpl.class);
    private final LocalDataBaseManager dataBaseManager;

    public ClienteRepositoryImpl(LocalDataBaseManager dataBaseManager) {
        this.dataBaseManager = dataBaseManager;
    }

    public List<Cliente> getAll() {
        logger.info("Obteniendo clientes...");
        List<Cliente> clientes = new ArrayList<>();
        String query = "SELECT * FROM clientes";

        try (Connection connection = dataBaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Cliente cliente = Cliente.builder()
                        .id(resultSet.getLong("id"))
                        .uuid((java.util.UUID) resultSet.getObject("uuid"))
                        .nombre(resultSet.getString("nombre"))
                        .createdAt(resultSet.getObject("created_at", LocalDateTime.class))
                        .updatedAt(resultSet.getObject("updated_at", LocalDateTime.class))
                        .build();
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener clientes", e);
        }

        return clientes;
    }

    @Override
    public Optional<Cliente> getById(long id) {
        logger.info("Obteniendo cliente por id...");
        String query = "SELECT * FROM clientes WHERE id = ?";

        try (Connection connection = dataBaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Cliente.builder()
                            .id(resultSet.getLong("id"))
                            .uuid((java.util.UUID) resultSet.getObject("uuid"))
                            .nombre(resultSet.getString("nombre"))
                            .createdAt(resultSet.getObject("created_at", LocalDateTime.class))
                            .updatedAt(resultSet.getObject("updated_at", LocalDateTime.class))
                            .build());
                }
            }
        } catch (SQLException e) {
            logger.error("Error al obtener cliente por id", e);
        }
        return Optional.empty();
    }

    @Override
    public Cliente create(Cliente cliente) {
        logger.info("Creando cliente...");
        String query = "INSERT INTO clientes (uuid, nombre, created_at, updated_at) VALUES (?, ?, ?, ?)";
        var uuid = java.util.UUID.randomUUID();
        var timeStamp = LocalDateTime.now();

        try (Connection connection = dataBaseManager.connect();

             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {


            statement.setObject(1, uuid);
            statement.setString(2, cliente.getNombre());
            statement.setObject(3, timeStamp);
            statement.setObject(4, timeStamp);

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    cliente.setId(id);
                    cliente.setUuid(uuid);
                    cliente.setCreatedAt(timeStamp);
                    cliente.setUpdatedAt(timeStamp);
                    return cliente;
                } else {
                    throw new SQLException("No se pudo obtener la clave generada.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error al crear cliente", e);
        }

        return cliente;
    }

    @Override
    public Cliente update(long id, Cliente cliente) {
        logger.info("Actualizando cliente...");
        String query = "UPDATE clientes SET nombre = ?, updated_at = ? WHERE id = ?";
        LocalDateTime timeStamp = LocalDateTime.now();

        try (Connection connection = dataBaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, cliente.getNombre());
            statement.setObject(2, timeStamp);
            statement.setLong(3, id);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                cliente.setUpdatedAt(timeStamp);
                return cliente;
            }
        } catch (SQLException e) {
            logger.error("Error al actualizar cliente", e);
        }

        return null;
    }

    @Override
    public boolean delete(long id) {
        logger.info("Borrando cliente...");
        String query = "DELETE FROM clientes WHERE id = ?";
        try (Connection connection = dataBaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                return true;
            } else {
                logger.warn("No se ha borrado ninguna cliente");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error al borrar cliente", e);
        }

        return false;
    }

}
