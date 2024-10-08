package org.example.clientes.repositories;

import org.example.clientes.model.Tarjeta;
import org.example.database.RemoteDataBaseManager;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TarjetaRemoteRepositoryImpl implements TarjetaRemoteRepository {
    private final RemoteDataBaseManager remoteDbManager;
    private final Logger logger = LoggerFactory.getLogger(TarjetaRemoteRepositoryImpl.class);

    public TarjetaRemoteRepositoryImpl(RemoteDataBaseManager remoteDbManager) {
        this.remoteDbManager = remoteDbManager;
    }

    public List<Tarjeta> getAll() {
        logger.info("Obteniendo todas las tarjetas...");
        List<Tarjeta> tarjetas = new ArrayList<>();
        String query = "SELECT * FROM tarjetas";

        try (Connection connection = remoteDbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                Tarjeta tarjeta = Tarjeta.builder()
                        .id(resultSet.getLong("id"))
                        .nombreTitular(resultSet.getString("nombreTitular"))
                        .numeroTarjeta(resultSet.getString("numeroTarjeta"))
                        .fechaCaducidad(LocalDate.from(resultSet.getObject("fechaCaducidad", LocalDateTime.class)))
                        .build();
                tarjetas.add(tarjeta);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener todas las tarjetas", e);
        }

        return tarjetas;
    }

    public Optional<Tarjeta> getById(Long id) {
        logger.info("Obteniendo tarjeta con id: " + id);
        String query = "SELECT * FROM tarjetas WHERE id = ?";

        try (Connection connection = remoteDbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return Optional.of(Tarjeta.builder()
                            .id(resultSet.getLong("id"))
                            .nombreTitular(resultSet.getString("nombreTitular"))
                            .numeroTarjeta(resultSet.getString("numeroTarjeta"))
                            .fechaCaducidad(LocalDate.from(resultSet.getObject("fechaCaducidad", LocalDateTime.class)))
                            .build());
                }
        } catch (SQLException e) {
            logger.error("Error al obtener la tarjeta con id: " + id, e);
        }
        return Optional.empty();
    }

    public Tarjeta create(Tarjeta tarjeta) {
        logger.info("Creando tarjeta...");
        String query = "INSERT INTO tarjetas (nombreTitular, numeroTarjeta, fechaCaducidad) VALUES (?, ?, ?)";
        try (Connection connection = remoteDbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, tarjeta.getNombreTitular());
            statement.setString(2, tarjeta.getNumeroTarjeta());
            statement.setObject(3, tarjeta.getFechaCaducidad());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    tarjeta.setId(id);
                    return tarjeta;
                } else {
                    throw new SQLException("No se pudo obtener la clave generada.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error al crear la tarjeta", e);
        }
        return tarjeta;
    }

    public Tarjeta update(Long id, Tarjeta tarjeta) {
        logger.info("Actualizando tarjeta...");
        String query = "UPDATE tarjetas SET nombreTitular = ?, numeroTarjeta = ?, fechaCaducidad = ? WHERE id = ?";
        try (Connection connection = remoteDbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, tarjeta.getNombreTitular());
                statement.setString(2, tarjeta.getNumeroTarjeta());
                statement.setObject(3, tarjeta.getFechaCaducidad());
                statement.setLong(4, id);
                statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al actualizar la tarjeta", e);
        }
        return null;
    }

    public Boolean delete(Long id) {
        logger.info("Borrando tarjeta...");
        String query = "DELETE FROM tarjetas WHERE id =?";
        try (Connection connection = remoteDbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, id);
                statement.executeUpdate();

                if (statement.executeUpdate() > 0) {
                    return true;
                } else {
                    logger.warn("No se ha borrado la tarjeta");
                    return false;
                }
        } catch (SQLException e) {
            logger.error("Error al borrar la tarjeta", e);
        }
        return false;
    }
}