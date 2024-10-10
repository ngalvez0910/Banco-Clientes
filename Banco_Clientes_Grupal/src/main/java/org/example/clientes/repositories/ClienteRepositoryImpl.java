package org.example.clientes.repositories;

import org.example.clientes.model.Cliente;
import org.example.clientes.model.Tarjeta;
import org.example.clientes.model.Usuario;
import org.example.database.LocalDataBaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
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
        String query = "SELECT u.id AS usuarioId, u.nombre, u.userName, u.email, u.createdAt AS usuarioCreatedAt, u.updatedAt AS usuarioUpdatedAt," +
                        "t.id AS tarjetaId, t.numeroTarjeta, t.nombreTitular, t.fechaCaducidad, t.createdAt AS tarjetaCreatedAt, t.updatedAt AS tarjetaUpdatedAt " +
                        "FROM Usuario u LEFT JOIN Tarjeta t ON u.nombre = t.nombreTitular";

        try (Connection connection = dataBaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Usuario usuario = Usuario.builder()
                        .id(resultSet.getLong("usuarioId"))
                        .nombre(resultSet.getString("nombre"))
                        .userName(resultSet.getString("userName"))
                        .email(resultSet.getString("email"))
                        .createdAt(resultSet.getObject("usuarioCreatedAt", LocalDateTime.class))
                        .updatedAt(resultSet.getObject("usuarioUpdatedAt", LocalDateTime.class))
                        .build();

                Tarjeta tarjeta = null;
                if (resultSet.getString("tarjetaId") != null) {
                    tarjeta = Tarjeta.builder()
                            .id(resultSet.getLong("tarjetaId"))
                            .numeroTarjeta(resultSet.getString("numeroTarjeta"))
                            .nombreTitular(resultSet.getString("nombreTitular"))
                            .fechaCaducidad(resultSet.getObject("fechaCaducidad", LocalDate.class))
                            .createdAt(resultSet.getObject("tarjetaCreatedAt", LocalDateTime.class))
                            .updatedAt(resultSet.getObject("tarjetaUpdatedAt", LocalDateTime.class))
                            .build();
                }

                Cliente cliente = Cliente.builder()
                        .id(resultSet.getLong("id"))
                        .usuario(resultSet.getObject("usuario", Usuario.class))
                        .tarjeta(resultSet.getObject("tarjeta", Tarjeta.class))
                        .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
                        .updatedAt(resultSet.getObject("updatedAt", LocalDateTime.class))
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
        String query = "SELECT u.id AS usuarioId, u.nombre, u.userName, u.email, u.createdAt AS usuarioCreatedAt, u.updatedAt AS usuarioUpdatedAt," +
                        "t.id AS tarjetaId, t.numeroTarjeta, t.nombreTitular, t.fechaCaducidad, t.createdAt AS tarjetaCreatedAt, t.updatedAt AS tarjetaUpdatedAt " +
                        "FROM Usuario u LEFT JOIN Tarjeta t ON u.nombre = t.nombreTitular WHERE u.id = ?";

        try (Connection connection = dataBaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Usuario usuario = Usuario.builder()
                            .id(resultSet.getLong("usuarioId"))
                            .nombre(resultSet.getString("nombre"))
                            .userName(resultSet.getString("userName"))
                            .email(resultSet.getString("email"))
                            .createdAt(resultSet.getObject("usuarioCreatedAt", LocalDateTime.class))
                            .updatedAt(resultSet.getObject("usuarioUpdatedAt", LocalDateTime.class))
                            .build();

                    Tarjeta tarjeta = null;
                    if (resultSet.getString("tarjetaId") != null) {
                        tarjeta = Tarjeta.builder()
                                .id(Long.valueOf(resultSet.getString("tarjetaId")))
                                .numeroTarjeta(resultSet.getString("numeroTarjeta"))
                                .nombreTitular(resultSet.getString("nombreTitular"))
                                .fechaCaducidad(resultSet.getObject("fechaCaducidad", LocalDate.class))
                                .createdAt(resultSet.getObject("tarjetaCreatedAt", LocalDateTime.class))
                                .updatedAt(resultSet.getObject("tarjetaUpdatedAt", LocalDateTime.class))
                                .build();
                    }

                    return Optional.of(Cliente.builder()
                            .id(resultSet.getLong("id"))
                            .usuario(resultSet.getObject("usuario", Usuario.class))
                            .tarjeta(resultSet.getObject("tarjeta", Tarjeta.class))
                            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
                            .updatedAt(resultSet.getObject("updatedAt", LocalDateTime.class))
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

        String userQuery = "INSERT INTO Usuario (id, nombre, userName, email, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?)";
        String tarjetaQuery = "INSERT INTO Tarjeta (id, numeroTarjeta, nombreTitular, fechaCaducidad, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?)";

        LocalDateTime timeStamp = LocalDateTime.now();

        try (Connection connection = dataBaseManager.connect()) {

            connection.prepareStatement("BEGIN TRANSACTION").execute();

            try (PreparedStatement statementUsuario = connection.prepareStatement(userQuery)) {
                statementUsuario.setLong(1, cliente.getUsuario().getId());
                statementUsuario.setString(2, cliente.getUsuario().getNombre());
                statementUsuario.setString(3, cliente.getUsuario().getUserName());
                statementUsuario.setString(4, cliente.getUsuario().getEmail());
                statementUsuario.setObject(5, timeStamp);
                statementUsuario.setObject(6, timeStamp);

                statementUsuario.executeUpdate();

                try (ResultSet generatedKeys = statementUsuario.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long clienteId = generatedKeys.getLong(1);
                        cliente.getUsuario().setId(clienteId);
                        cliente.setId(clienteId);
                    } else {
                        throw new SQLException("No se pudo obtener la clave generada para Usuario.");
                    }
                }
            }

            if (cliente.getTarjeta() != null) {
                try (PreparedStatement statementTarjeta = connection.prepareStatement(tarjetaQuery)) {
                    statementTarjeta.setLong(1, cliente.getTarjeta().getId());
                    statementTarjeta.setString(2, cliente.getTarjeta().getNumeroTarjeta());
                    statementTarjeta.setString(3, cliente.getTarjeta().getNombreTitular());
                    statementTarjeta.setObject(4, cliente.getTarjeta().getFechaCaducidad());
                    statementTarjeta.setObject(5, timeStamp);
                    statementTarjeta.setObject(6, timeStamp);

                    statementTarjeta.executeUpdate();

                    try (ResultSet generatedKeys = statementTarjeta.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            cliente.getTarjeta().setId(generatedKeys.getLong(1));
                        } else {
                            throw new SQLException("No se pudo obtener la clave generada para Tarjeta.");
                        }
                    }
                }
            }

            connection.prepareStatement("COMMIT").execute();

            cliente.setCreatedAt(timeStamp);
            cliente.setUpdatedAt(timeStamp);
            return cliente;

        } catch (SQLException e) {
            logger.error("Error al crear cliente", e);
        }

        return cliente;
    }

    @Override
    public Cliente update(long id, Cliente cliente) {
        logger.info("Actualizando cliente...");

        String userQuery = "UPDATE Usuario SET nombre = ?, userName = ?, email = ?, updatedAt = ? WHERE id = ?";
        String tarjetaQuery = "UPDATE Tarjeta SET numeroTarjeta = ?, nombreTitular = ?, fechaCaducidad = ?, updatedAt = ? WHERE nombreTitular = ?";

        LocalDateTime timeStamp = LocalDateTime.now();

        try (Connection connection = dataBaseManager.connect()) {

            connection.prepareStatement("BEGIN TRANSACTION").execute();

            try (PreparedStatement statementUsuario = connection.prepareStatement(userQuery)) {
                statementUsuario.setString(1, cliente.getUsuario().getNombre());
                statementUsuario.setString(2, cliente.getUsuario().getUserName());
                statementUsuario.setString(3, cliente.getUsuario().getEmail());
                statementUsuario.setObject(4, timeStamp);
                statementUsuario.setLong(5, id);
                statementUsuario.executeUpdate();
            }

            if (cliente.getTarjeta() != null) {
                try (PreparedStatement statementTarjeta = connection.prepareStatement(tarjetaQuery)) {
                    statementTarjeta.setString(1, cliente.getTarjeta().getNumeroTarjeta());
                    statementTarjeta.setString(2, cliente.getTarjeta().getNombreTitular());
                    statementTarjeta.setObject(3, cliente.getTarjeta().getFechaCaducidad());
                    statementTarjeta.setObject(4, timeStamp);
                    statementTarjeta.setLong(5, id);
                    statementTarjeta.executeUpdate();
                }
            }

            connection.prepareStatement("COMMIT").execute();

            cliente.setUpdatedAt(timeStamp);
            return cliente;

        } catch (SQLException e) {
            logger.error("Error al actualizar cliente", e);
        }

        return null;
    }

    @Override
    public boolean delete(long id) {
        logger.info("Borrando cliente...");

        String deleteUsuarioQuery = "DELETE FROM Usuario WHERE id = ?";
        String deleteTarjetaQuery = "DELETE FROM Tarjeta WHERE nombreTitular = ?";

        try (Connection connection = dataBaseManager.connect()) {
            connection.prepareStatement("BEGIN TRANSACTION").execute();

            try (PreparedStatement statementTarjeta = connection.prepareStatement(deleteTarjetaQuery)) {
                statementTarjeta.setLong(1, id);
                statementTarjeta.executeUpdate();
            }

            try (PreparedStatement statementUsuario = connection.prepareStatement(deleteUsuarioQuery)) {
                statementUsuario.setLong(1, id);
                int rows = statementUsuario.executeUpdate();

                if (rows > 0) {
                    connection.prepareStatement("COMMIT").execute();
                    return true;
                } else {
                    logger.warn("No se ha borrado ningún cliente");
                    return false;
                }
            }

        } catch (SQLException e) {
            logger.error("Error al borrar cliente", e);
        }

        return false;
    }
}