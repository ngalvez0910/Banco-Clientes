package org.example.database;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.example.config.ConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase que gestiona las conexiones a la base de datos local.
 * Implementa la interfaz AutoCloseable para asegurar el cierre de recursos.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public class LocalDataBaseManager implements AutoCloseable {

    private static LocalDataBaseManager instance = null;
    private final Logger logger = LoggerFactory.getLogger(LocalDataBaseManager.class);
    private Connection connection = null;

    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;
    private InputStream DB_INIT;

    /**
     * Constructor privado que inicializa los parámetros de conexión.
     *
     * @param config Configuración de propiedades para la conexión.
     */
    public LocalDataBaseManager(ConfigProperties config) {
        this.DB_URL = config.getProperty("local.database.url", "jdbc:sqlite:clients.db");
        this.DB_USER = config.getProperty("database.user", "");
        this.DB_PASSWORD = config.getProperty("database.password", "");
        this.DB_INIT = getClass().getClassLoader().getResourceAsStream("init.sql");
    }

    /**
     * Método estático para obtener la instancia singleton del gestor de la base de datos.
     *
     * @param config Configuración de propiedades para la conexión.
     * @return La instancia única de LocalDataBaseManager.
     */
    public static LocalDataBaseManager getInstance(ConfigProperties config) {
        if (instance == null) {
            instance = new LocalDataBaseManager(config);
        }
        return instance;
    }

    /**
     * Establece una conexión con la base de datos.
     *
     * @return Un objeto Connection que representa la conexión a la base de datos.
     * @throws SQLException Si ocurre un error al intentar conectar.
     */
    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                logger.info("Conexión a la base de datos establecida correctamente.");

                if (DB_INIT != null) {
                    runInitScript(connection, DB_INIT);
                }
            } catch (SQLException e) {
                logger.error("Error al conectar a la base de datos local", e);
                throw new RuntimeException("Error al conectar a la base de datos local", e);
            }
        }
        return connection;
    }

    /**
     * Desconecta la base de datos cerrando la conexión activa.
     */
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Conexión a la base de datos cerrada.");
            } catch (SQLException e) {
                logger.error("Error al cerrar la conexión a la base de datos", e);
            }
        }
    }

    /**
     * Método que se llama cuando el objeto se cierra, asegurando que la conexión se libere.
     *
     * @throws Exception Si ocurre un error al cerrar la conexión.
     */
    @Override
    public void close() throws Exception {
        disconnect();
    }

    private void runInitScript(Connection connection, InputStream initScript) {
        try {
            ScriptRunner scriptRunner = new ScriptRunner(connection);

            scriptRunner.runScript(new InputStreamReader(initScript));

            logger.info("Script de inicialización ejecutado correctamente.");
        } catch (Exception e) {
            logger.error("Error al ejecutar el script init.sql", e);
            throw new RuntimeException("Error al ejecutar el script init.sql", e);
        }
    }
}