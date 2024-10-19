package org.example;

import org.example.clientes.cache.CacheClienteImpl;
import org.example.clientes.model.Cliente;
import org.example.clientes.model.Tarjeta;
import org.example.clientes.model.Usuario;
import org.example.clientes.repositories.ClienteRepositoryImpl;
import org.example.clientes.repositories.TarjetaRemoteRepositoryImpl;
import org.example.clientes.services.ClienteNotificacionImpl;
import org.example.clientes.services.ClienteServiceImpl;
import org.example.config.ConfigProperties;
import org.example.database.LocalDataBaseManager;
import org.example.database.RemoteDataBaseManager;
import org.example.rest.RetrofitClient;
import org.example.rest.UserApiRest;
import org.example.rest.repository.UserRemoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        System.out.println("Hello world!");
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

        System.out.println("Sistema de obtención de notificaciones en Tiempo Real");
        service.getNotifications().subscribe(
                notificacion -> System.out.println("🟢 Notificación: " + notificacion),
                error -> System.err.println("Se ha producido un error: " + error),
                () -> System.out.println("Completado")
        );

        //TODO PRUEBAS:

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

        //probando create
        service.create(cliente)
                .peek(client -> System.out.println("Cliente creado: " + client))
                .peekLeft(error -> System.out.println("Error: " + error.getMessage()));

        service.create(cliente2)
                .peek(client -> System.out.println("Cliente creado: " + client))
                .peekLeft(error -> System.out.println("Error: " + error.getMessage()));

        //probando getAll
        service.getAll()
                .peek(list -> System.out.println("Listado de clientes: " + list))
                .peekLeft(error -> System.out.println("Error: " + error.getMessage()));

        //probando getById
        service.getById(1L)
                .peek(client -> System.out.println("Cliente con id 1: " + client))
                .peekLeft(error -> System.out.println("Error: " + error.getMessage()));

        //probando update
        var usuarioUpdate = new Usuario(3L, "update", "updateUsername", "emailUpdate@email.com", LocalDateTime.now(), LocalDateTime.now());
        List<Tarjeta> tarjetas3 = new ArrayList<>();
        var tarjeta4 = new Tarjeta(4L, "update", "4444555566667777", LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        tarjetas3.add(tarjeta4);
        var clienteUpdate = new Cliente(usuarioUpdate.getId(), usuarioUpdate, tarjetas3, LocalDateTime.now(), LocalDateTime.now());
        service.update(1L, clienteUpdate)
                .peek(client -> System.out.println("Cliente actualizado: " + client))
                .peekLeft(error -> System.out.println("Error: " + error.getMessage()));

        //probando delete
        service.delete(2L)
                .peek(client -> System.out.println("Cliente eliminado: " + client))
                .peekLeft(error -> System.out.println("Error: " + error.getMessage()));

        System.exit(0);
    }
}