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

/**
 * Implementación del repositorio remoto para gestionar entidades de tipo Tarjeta.
 * Proporciona métodos para crear, obtener, actualizar y eliminar tarjetas en la base de datos remota.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public class TarjetaRemoteRepositoryImpl implements TarjetaRemoteRepository {
    private final RemoteDataBaseManager remoteDbManager;
    private final Logger logger = LoggerFactory.getLogger(TarjetaRemoteRepositoryImpl.class);

    /**
     * Constructor que inicializa el {@link RemoteDataBaseManager} para la conexión a la base de datos.
     *
     * @param remoteDbManager el gestor de la base de datos remota
     */
    public TarjetaRemoteRepositoryImpl(RemoteDataBaseManager remoteDbManager) {
        this.remoteDbManager = remoteDbManager;
    }

    /**
     * Obtiene todas las tarjetas de la base de datos.
     *
     * @return una lista de todas las tarjetas
     */
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
                        .fechaCaducidad(resultSet.getObject("fechaCaducidad", LocalDate.class))
                        .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
                        .updatedAt(resultSet.getObject("updatedAt", LocalDateTime.class))
                        .build();
                tarjetas.add(tarjeta);
            }
            logger.info("Tarjetas obtenidas: {}", tarjetas);
        } catch (SQLException e) {
            logger.error("Error al obtener todas las tarjetas", e);
        }

        return tarjetas;
    }

    /**
     * Obtiene una tarjeta específica por su identificador.
     *
     * @param id el identificador de la tarjeta a obtener
     * @return un {@link Optional} que contiene la tarjeta si se encuentra, o vacío si no
     */
    public Optional<Tarjeta> getById(Long id) {
        logger.info("Obteniendo tarjeta con id: " + id);
        String query = "SELECT * FROM tarjetas WHERE id = ?";

        try (Connection connection = remoteDbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    Tarjeta tarjeta = Tarjeta.builder()
                            .id(resultSet.getLong("id"))
                            .nombreTitular(resultSet.getString("nombreTitular"))
                            .numeroTarjeta(resultSet.getString("numeroTarjeta"))
                            .fechaCaducidad(resultSet.getObject("fechaCaducidad", LocalDate.class))
                            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
                            .updatedAt(resultSet.getObject("updatedAt", LocalDateTime.class))
                            .build();
                    logger.info("Tarjeta encontrada: {}", tarjeta);
                    return Optional.of(tarjeta);
                }
        } catch (SQLException e) {
            logger.error("Error al obtener la tarjeta con id: " + id, e);
        }
        return Optional.empty();
    }

    /**
     * Crea una nueva tarjeta en la base de datos.
     *
     * @param tarjeta la tarjeta a crear
     * @return la tarjeta creada, con el identificador generado
     */
    public Tarjeta create(Tarjeta tarjeta) {
        logger.info("Creando tarjeta...");
        String query = "INSERT INTO tarjetas (nombreTitular, numeroTarjeta, fechaCaducidad, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?)";
        var timeStamp = LocalDateTime.now();
        try (Connection connection = remoteDbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, tarjeta.getNombreTitular());
            statement.setString(2, tarjeta.getNumeroTarjeta());
            statement.setObject(3, tarjeta.getFechaCaducidad());
            statement.setObject(4, timeStamp);
            statement.setObject(5, timeStamp);

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    tarjeta.setId(id);
                    tarjeta.setCreatedAt(timeStamp);
                    tarjeta.setUpdatedAt(timeStamp);
                    logger.info("Tarjeta creada: {}", tarjeta);
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

    /**
     * Actualiza una tarjeta existente en la base de datos.
     *
     * @param id el identificador de la tarjeta a actualizar
     * @param tarjeta la tarjeta con los nuevos datos
     * @return la tarjeta actualizada, o null si no se encontró
     */
    public Tarjeta update(Long id, Tarjeta tarjeta) {
        logger.info("Actualizando tarjeta...");
        String query = "UPDATE tarjetas SET nombreTitular = ?, numeroTarjeta = ?, fechaCaducidad = ?, updatedAt = ? WHERE id = ?";
        var timeStamp = LocalDateTime.now();
        try (Connection connection = remoteDbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, tarjeta.getNombreTitular());
                statement.setString(2, tarjeta.getNumeroTarjeta());
                statement.setObject(3, tarjeta.getFechaCaducidad());
                statement.setObject(4, timeStamp);
                statement.setLong(5, id);
                statement.executeUpdate();

                int rows = statement.executeUpdate();
                if (rows > 0) {
                    tarjeta.setUpdatedAt(timeStamp);
                    logger.info("Tarjeta actualizada: {}", tarjeta);
                    return tarjeta;
                }

            return getById(id).orElse(null);
        } catch (SQLException e) {
            logger.error("Error al actualizar la tarjeta", e);
        }
        return null;
    }

    /**
     * Elimina una tarjeta de la base de datos por su identificador.
     *
     * @param id el identificador de la tarjeta a eliminar
     * @return true si la tarjeta fue eliminada, false si no se encontró
     */
    public Boolean delete(Long id) {
        logger.info("Borrando tarjeta...");
        String query = "DELETE FROM tarjetas WHERE id =?";
        try (Connection connection = remoteDbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, id);
                int rows = statement.executeUpdate();

                if (rows > 0) {
                    logger.info("Tarjeta con ID {} borrada exitosamente", id);
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