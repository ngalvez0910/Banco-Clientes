package org.example.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase que gestiona las conexiones a una base de datos remota utilizando JDBC.
 * Implementa el patrón Singleton para asegurar que solo exista una instancia de la clase.
 * También implementa AutoCloseable para facilitar el cierre de la conexión.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public class RemoteDataBaseManager implements AutoCloseable {
    private static RemoteDataBaseManager instance = null;
    private final Logger logger = LoggerFactory.getLogger(RemoteDataBaseManager.class);

    // Variables para los parámetros de conexión a la base de datos
    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;
    private Connection connection = null;

    /**
     * Constructor privado que carga las propiedades necesarias para la conexión a la base de datos.
     */
    public RemoteDataBaseManager() {
        loadProperties();
    }

    /**
     * Carga las propiedades de conexión desde el archivo "application.properties".
     */
    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.error("No se ha podido encontrar el fichero: application.properties");
                return;
            }

            Properties prop = new Properties();
            prop.load(input);

            DB_URL = prop.getProperty("database.url");
            DB_USER = prop.getProperty("database.user");
            DB_PASSWORD = prop.getProperty("database.password");

        } catch (IOException ex) {
            logger.error("No se ha podido cargar el fichero de properties", ex);
        }
    }

    /**
     * Método para obtener la instancia singleton de RemoteDataBaseManager.
     *
     * @return La instancia única de RemoteDataBaseManager.
     */
    public static RemoteDataBaseManager getInstance() {
        if (instance == null) {
            instance = new RemoteDataBaseManager();
        }
        return instance;
    }

    /**
     * Obtiene una conexión a la base de datos. Si la conexión no existe o está cerrada, se crea una nueva.
     *
     * @return Un objeto Connection que representa la conexión a la base de datos.
     * @throws SQLException Si ocurre un error al intentar conectar.
     */
    public Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            logger.error("No se pudo conectar con la base de datos", e);
            throw new RuntimeException("No se pudo conectar con la base de datos", e);
        }
    }

    /**
     * Desconecta la conexión de la base de datos cerrando la conexión activa.
     */
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                logger.info("Desconectando de la base de datos...");
            } catch (SQLException e) {
                logger.error("No se pudo desconectar de la base de datos", e);
                throw new RuntimeException("No se pudo desconectar de la base de datos", e);
            }
        }
    }

    /**
     * Cierra la conexión a la base de datos llamando a `disconnect()`.
     *
     * @throws Exception Si ocurre un error al cerrar la conexión.
     */
    @Override
    public void close() throws Exception {
        disconnect();
    }
}
