package org.example;

import io.reactivex.rxjava3.core.Observable;
import org.example.clientes.cache.CacheClienteImpl;
import org.example.clientes.model.Cliente;
import org.example.clientes.model.Tarjeta;
import org.example.clientes.model.Usuario;
import org.example.clientes.repositories.ClienteRepositoryImpl;
import org.example.clientes.repositories.TarjetaRemoteRepositoryImpl;
import org.example.clientes.services.ClienteNotificacionImpl;
import org.example.clientes.services.ClienteServiceImpl;
import org.example.clientes.storage.csv.StorageTarjetaCsvImpl;
import org.example.clientes.storage.csv.StorageUsuarioCsvImpl;
import org.example.clientes.storage.json.StorageJsonImpl;
import org.example.config.ConfigProperties;
import org.example.database.LocalDataBaseManager;
import org.example.database.RemoteDataBaseManager;
import org.example.rest.RetrofitClient;
import org.example.rest.UserApiRest;
import org.example.rest.repository.UserRemoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Hello world!");
        logger.debug("userApiRest: " + UserApiRest.API_USERS_URL);

        var retrofit = RetrofitClient.getClient(UserApiRest.API_USERS_URL);
        var userApiRest = retrofit.create(UserApiRest.class);
        UserRemoteRepository userRepository = new UserRemoteRepository(userApiRest);
        var remoteDataBaseManager = new RemoteDataBaseManager();
        TarjetaRemoteRepositoryImpl tarjetaRepository = new TarjetaRemoteRepositoryImpl(remoteDataBaseManager);
        var configProperties = new ConfigProperties();
        var localDataBaseManager = new LocalDataBaseManager(configProperties);
        ClienteRepositoryImpl clienteRepository = new ClienteRepositoryImpl(localDataBaseManager);
        CacheClienteImpl cacheCliente = new CacheClienteImpl();
        ClienteNotificacionImpl notification = new ClienteNotificacionImpl();

        var service = new ClienteServiceImpl(userRepository, tarjetaRepository, clienteRepository, cacheCliente, notification);

        tarjetaRepository.resetIds();

        logger.info("Sistema de obtención de notificaciones en Tiempo Real");
        service.getNotifications().subscribe(
                notificacion -> logger.info("🟢 Notificación: {}", notificacion),
                error -> logger.info("Se ha producido un error: {}", error.getMessage()),
                () -> logger.info("Completado")
        );

        var usuario = new Usuario(1L, "name1", "username1", "email1@email.com", LocalDateTime.now(), LocalDateTime.now());
        var usuario2 = new Usuario(2L, "name2", "username2", "email2@email.com", LocalDateTime.now(), LocalDateTime.now());

        List<Tarjeta> tarjetas1 = new ArrayList<>();
        List<Tarjeta> tarjetas2 = new ArrayList<>();
        var tarjeta1 = new Tarjeta(1L, "name1", "1111222233334444", LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        var tarjeta2 = new Tarjeta(2L, "name1", "2222333344445555", LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        var tarjeta3 = new Tarjeta(3L, "name2", "3333444455556666", LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        tarjetas1.add(tarjeta1);
        tarjetas1.add(tarjeta2);
        tarjetas2.add(tarjeta3);

        var cliente = new Cliente(usuario.getId(), usuario, tarjetas1, LocalDateTime.now(), LocalDateTime.now());
        var cliente2 = new Cliente(usuario2.getId(), usuario2, tarjetas2, LocalDateTime.now(), LocalDateTime.now());

        // Probando create
        service.create(cliente)
                .peek(client -> logger.info("Cliente creado: {}", client))
                .peekLeft(error -> logger.info("Error: {}", error.getMessage()));

        service.create(cliente2)
                .peek(client -> logger.info("Cliente creado: {}", client))
                .peekLeft(error -> logger.info("Error: {}", error.getMessage()));

        // Probando getAll
        service.getAll()
                .peek(list -> logger.info("Listado de clientes: {}", list))
                .peekLeft(error -> logger.info("Error: {}", error.getMessage()));

        // Probando getById
        service.getById(1L)
                .peek(client -> logger.info("Cliente con id 1: {}", client))
                .peekLeft(error -> logger.info("Error: {}", error.getMessage()));

        // Probando update
        var usuarioUpdate = new Usuario(3L, "update", "updateUsername", "emailUpdate@email.com", LocalDateTime.now(), LocalDateTime.now());
        List<Tarjeta> tarjetas3 = new ArrayList<>();
        var tarjeta4 = new Tarjeta(4L, "update", "4444555566667777", LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        tarjetas3.add(tarjeta4);
        var clienteUpdate = new Cliente(usuarioUpdate.getId(), usuarioUpdate, tarjetas3, LocalDateTime.now(), LocalDateTime.now());
        service.update(1L, clienteUpdate)
                .peek(client -> logger.info("Cliente actualizado: {}", client))
                .peekLeft(error -> logger.info("Error: {}", error.getMessage()));

        // Probando delete
        service.delete(2L)
                .peek(client -> logger.info("Cliente eliminado: {}", client))
                .peekLeft(error -> logger.info("Error: {}", error.getMessage()));

        // Exportando datos
        var csvStorage = new StorageTarjetaCsvImpl();
        var jsonStorage = new StorageJsonImpl(new ObjectMapper(), logger);
        var usuarioCsvStorage = new StorageUsuarioCsvImpl();

        // Crear una lista de usuarios
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario);
        usuarios.add(usuario2);

        // Crear una lista de tarjetas
        List<Tarjeta> tarjetas = new ArrayList<>(tarjetas1);
        tarjetas.addAll(tarjetas2);

        exportarDatos(csvStorage, jsonStorage, usuarioCsvStorage, usuarios, tarjetas);

        System.exit(0);
    }

    private static void exportarDatos(StorageTarjetaCsvImpl tarjetaStorage, StorageJsonImpl jsonStorage,
                                      StorageUsuarioCsvImpl usuarioCsvStorage, List<Usuario> usuarios, List<Tarjeta> tarjetas) {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        try {
            // Archivos a exportar
            File csvFile = new File(dataDir, "tarjetas.csv");
            File jsonFileClientes = new File(dataDir, "clientes.json");
            File csvFileUsuarios = new File(dataDir, "usuarios.csv");

            // Eliminar archivos existentes
            eliminarArchivo(csvFile);
            eliminarArchivo(jsonFileClientes);
            eliminarArchivo(csvFileUsuarios);

            // Exportando tarjetas a CSV
            tarjetaStorage.exportFile(csvFile, Observable.fromIterable(tarjetas));
            logger.info("Tarjetas exportadas a CSV: {}", csvFile.getAbsolutePath());

            // Exportando clientes a JSON
            var cliente = new Cliente(usuarios.get(0).getId(), usuarios.get(0), tarjetas, LocalDateTime.now(), LocalDateTime.now());
            jsonStorage.exportFile(jsonFileClientes, Observable.just(cliente));
            logger.info("Clientes exportados a JSON: {}", jsonFileClientes.getAbsolutePath());

            // Exportando usuarios a CSV
            usuarioCsvStorage.exportFile(csvFileUsuarios, Observable.fromIterable(usuarios));
            logger.info("Usuarios exportados a CSV: {}", csvFileUsuarios.getAbsolutePath());

        } catch (Exception e) {
            logger.error("Error al exportar datos: ", e);
        }
    }

    private static void eliminarArchivo(File file) {
        if (file.exists() && !file.delete()) {
            logger.warn("No se pudo eliminar el archivo: {}", file.getAbsolutePath());
        } else {
            logger.info("Archivo eliminado: {}", file.getAbsolutePath());
        }
    }
}