package org.example.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.config.ConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase que gestiona las conexiones a la base de datos local utilizando HikariCP como pool de conexiones.
 * Implementa la interfaz AutoCloseable para asegurar el cierre de recursos.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 */
public class LocalDataBaseManager implements AutoCloseable {
    private static LocalDataBaseManager instance = null;
    private HikariDataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(LocalDataBaseManager.class);

    // Parámetros de configuración por defecto
    private String DB_URL = "jdbc:sqlite:clients.db";
    private String DB_USER = "admin";
    private String DB_PASSWORD = "admin123";
    private String DB_Timeout = "10000";
    private Connection connection = null;

    /**
     * Constructor por defecto.
     */
    public LocalDataBaseManager() {
    }

    /**
     * Constructor que inicializa el gestor de la base de datos con las configuraciones proporcionadas.
     *
     * @param config Configuración de propiedades que contiene los detalles de la conexión a la base de datos.
     */
    private LocalDataBaseManager(ConfigProperties config) {
        HikariConfig hikariConfig = new HikariConfig();

        // Configurar Hikari con las propiedades obtenidas del archivo de configuración
        hikariConfig.setJdbcUrl(config.getProperty("local.database.url", DB_URL));
        hikariConfig.setUsername(config.getProperty("database.username", DB_USER));
        hikariConfig.setPassword(config.getProperty("database.password", DB_PASSWORD));
        hikariConfig.setConnectionTimeout(Long.parseLong(config.getProperty("local.database.timeout", DB_Timeout)));

        this.dataSource = new HikariDataSource(hikariConfig);
        logger.info("Hikari configurado correctamente");
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
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Error al conectar a la base de datos local", e);
            throw new RuntimeException("Error al conectar a la base de datos local", e);
        }
    }

    /**
     * Desconecta el pool de conexiones cerrando todas las conexiones activas.
     */
    public void disconnect() {
        if (dataSource != null) {
            dataSource.close();
            logger.info("Desconectado del pool de conexiones...");
        }
    }

    /**
     * Método que se llama cuando el objeto se cierra, asegurando que las conexiones se liberen.
     *
     * @throws Exception Si ocurre un error al cerrar la conexión.
     */
    @Override
    public void close() throws Exception {
        disconnect();
    }
}
