package org.example.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class RemoteDataBaseManager implements AutoCloseable {
    private static RemoteDataBaseManager instance = null;
    private final Logger logger = LoggerFactory.getLogger(RemoteDataBaseManager.class);
    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;
    private Connection connection = null;

    public RemoteDataBaseManager() {
        loadProperties();
    }

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

    public static RemoteDataBaseManager getInstance() {
        if (instance == null) {
            instance = new RemoteDataBaseManager();
        }
        return instance;
    }

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

    @Override
    public void close() throws Exception {
        disconnect();
    }
}
