package org.example.clientes.services;

import io.vavr.control.Either;
import org.example.clientes.cache.CacheClienteImpl;
import org.example.clientes.errors.ClienteError;
import org.example.clientes.model.Cliente;
import org.example.clientes.model.Notificacion;
import org.example.clientes.model.Tarjeta;
import org.example.clientes.model.Usuario;
import org.example.clientes.repositories.ClienteRepository;
import org.example.clientes.repositories.TarjetaRemoteRepositoryImpl;
import org.example.rest.repository.UserRemoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Implementación del servicio de gestión de clientes.
 * Esta clase proporciona métodos para realizar operaciones CRUD sobre los clientes,
 * utilizando repositorios y cache para obtener, crear, actualizar y eliminar clientes.
 * También maneja notificaciones relacionadas con los cambios en los clientes.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public class ClienteServiceImpl implements ClienteService {
    private final ClienteNotificacion notification;
    private final UserRemoteRepository userRepository;
    private final TarjetaRemoteRepositoryImpl tarjetaRepository;
    private final ClienteRepository clienteRepository;
    private final CacheClienteImpl cacheCliente;
    private final Logger logger = LoggerFactory.getLogger(ClienteServiceImpl.class);

    /**
     * Constructor de la clase ClienteServiceImpl.
     *
     * @param userRepository el repositorio para acceder a los usuarios remotos.
     * @param tarjetaRemoteRepository el repositorio para acceder a las tarjetas remotas.
     * @param clienteRepository el repositorio para acceder a los clientes.
     * @param cacheCliente el sistema de cache para los clientes.
     * @param notification el objeto de notificación para gestionar las notificaciones de clientes.
     */
    public ClienteServiceImpl(UserRemoteRepository userRepository, TarjetaRemoteRepositoryImpl tarjetaRemoteRepository, ClienteRepository clienteRepository, CacheClienteImpl cacheCliente, ClienteNotificacion notification) {
        this.userRepository = userRepository;
        this.tarjetaRepository = tarjetaRemoteRepository;
        this.clienteRepository = clienteRepository;
        this.cacheCliente = cacheCliente;
        this.notification = notification;
    }

    /**
     * Obtiene todos los clientes.
     *
     * @return un {@link Either} que contiene un {@link ClienteError} si ocurre un error, o una lista de {@link Cliente} si la operación es exitosa.
     */
    @Override
    public Either<ClienteError, List<Cliente>> getAll() {
        logger.info("Obteniendo clientes...");
        try {
            List<Cliente> clientes = CompletableFuture.supplyAsync(clienteRepository::getAll).get(10000, MILLISECONDS);
            if (clientes.isEmpty()) {
                Optional<List<Usuario>> usuarios = CompletableFuture.supplyAsync(userRepository::getAllSync).get(10000, MILLISECONDS);
                if (usuarios.isPresent()) {
                    List<Tarjeta> tarjetas = CompletableFuture.supplyAsync(tarjetaRepository::getAll).get(10000, MILLISECONDS);
                    for (Usuario usuario : usuarios.get()) {
                        List<Tarjeta> tarjetasUser = new ArrayList<>();
                        for (Tarjeta tarjeta : tarjetas) {
                            if (tarjeta.getNombreTitular().equals(usuario.getNombre())) {
                                tarjetasUser.add(tarjeta);
                            }
                        }
                        Cliente cliente = new Cliente(usuario.getId(), usuario, tarjetasUser, LocalDateTime.now(), LocalDateTime.now());
                        clientes.add(cliente);

                        CompletableFuture.runAsync(() -> clienteRepository.create(cliente));
                        return Either.right(clientes);
                    }
                } else {
                    return Either.left(new ClienteError.ClienteNotFound());
                }
            }
            return Either.right(clientes);
        } catch (Exception e) {
            return Either.left(new ClienteError.ClienteNotFound());
        }
    }

    /**
     * Obtiene un cliente por su identificador.
     *
     * @param id el identificador del cliente.
     * @return un {@link Either} que contiene un {@link ClienteError} si ocurre un error, o el {@link Cliente} correspondiente si la operación es exitosa.
     */
    @Override
    public Either<ClienteError, Cliente> getById(long id) {
        logger.info("Obteniendo cliente por id: {}", id);
        try {
            Cliente cliente = CompletableFuture.supplyAsync(() -> cacheCliente.get(id)).get(10000, MILLISECONDS);
            if (cliente == null) {
                Optional<Cliente> clienteRepo = CompletableFuture.supplyAsync(() -> clienteRepository.getById(id)).get(10000, MILLISECONDS);

                if (clienteRepo.isEmpty()) {
                    Optional<Usuario> usuarioRemoto = CompletableFuture.supplyAsync(() -> userRepository.getByIdSync(id)).get(10000, MILLISECONDS);

                    if (usuarioRemoto.isEmpty()) {
                        return Either.left(new ClienteError.ClienteNotFound());
                    } else {
                        List<Tarjeta> tarjetasRemotas = CompletableFuture.supplyAsync(tarjetaRepository::getAll).get(10000, MILLISECONDS);
                        List<Tarjeta> tarjetasUser = new ArrayList<>();
                        for (Tarjeta tarjeta : tarjetasRemotas) {
                            if (tarjeta.getNombreTitular().equals(usuarioRemoto.get().getNombre())) {
                                tarjetasUser.add(tarjeta);
                            }
                        }
                        cliente = new Cliente(usuarioRemoto.get().getId(), usuarioRemoto.get(), tarjetasUser, LocalDateTime.now(), LocalDateTime.now());
                        Cliente finalCliente = cliente;
                        CompletableFuture.runAsync(() -> cacheCliente.put(finalCliente.getId(), finalCliente));
                        CompletableFuture.runAsync(() -> clienteRepository.create(finalCliente));
                        return Either.right(cliente);
                    }
                } else {
                    cliente = clienteRepo.get();
                    Cliente finalClient = cliente;
                    CompletableFuture.runAsync(() -> cacheCliente.put(finalClient.getId(), finalClient));
                    return Either.right(cliente);
                }
            }
            return Either.right(cliente);
        } catch (Exception e) {
            return Either.left(new ClienteError.ClienteNotFound());
        }
    }

    /**
     * Crea un nuevo cliente.
     *
     * @param cliente el cliente a crear.
     * @return un {@link Either} que contiene un {@link ClienteError} si ocurre un error, o el {@link Cliente} creado si la operación es exitosa.
     */
    @Override
    public Either<ClienteError, Cliente> create(Cliente cliente) {
        logger.info("Creando cliente...");
        Usuario usuario = cliente.getUsuario();
        List<Tarjeta> tarjetas = cliente.getTarjeta();
        try {
            Optional<Usuario> usuarioRemoto = CompletableFuture.supplyAsync(() -> userRepository.createUserSync(usuario)).get(10000, MILLISECONDS);
            if (usuarioRemoto.isPresent()) {
                for (Tarjeta tarjeta : tarjetas) {
                    CompletableFuture.runAsync(() -> tarjetaRepository.create(tarjeta));
                }
                Cliente client = new Cliente(usuarioRemoto.get().getId(), usuarioRemoto.get(), tarjetas, usuarioRemoto.get().getCreatedAt(), usuarioRemoto.get().getUpdatedAt());
                clienteRepository.create(client);
                notification.notify(new Notificacion<>(Notificacion.Tipo.NEW, client));
                return Either.right(client);
            } else {
                return Either.left(new ClienteError.ClienteNotCreated());
            }
        } catch (Exception e) {
            return Either.left(new ClienteError.ClienteNotCreated());
        }
    }

    /**
     * Actualiza un cliente existente.
     *
     * @param id el identificador del cliente a actualizar.
     * @param cliente el cliente con los nuevos datos.
     * @return un {@link Either} que contiene un {@link ClienteError} si ocurre un error, o el {@link Cliente} actualizado si la operación es exitosa.
     */
    @Override
    public Either<ClienteError, Cliente> update(long id, Cliente cliente) {
        logger.info("Actualizando cliente con id: {}", id);
        try {
            Optional<Usuario> usuarioRemoto = CompletableFuture.supplyAsync(() -> userRepository.getByIdSync(id)).get(10000, MILLISECONDS); //si no existe salta una excepción
            if (usuarioRemoto.isPresent()) {
                CompletableFuture.runAsync(() -> userRepository.updateUserSync(id, cliente.getUsuario()));

                List<Tarjeta> tarjetas = new ArrayList<>();
                for (Tarjeta tarjeta : cliente.getTarjeta()) {
                    CompletableFuture<Tarjeta> tarjetaFuture = CompletableFuture.supplyAsync(() -> tarjetaRepository.update(tarjeta.getId(), tarjeta));
                    tarjetas.add(tarjetaFuture.get(10000, MILLISECONDS));
                }

                Cliente clienteUpdated = CompletableFuture.supplyAsync(() -> clienteRepository.update(id, cliente)).get(10000, MILLISECONDS);
                if (clienteUpdated == null) {
                    return Either.left(new ClienteError.ClienteNotUpdated());
                } else {
                    notification.notify(new Notificacion<>(Notificacion.Tipo.NEW, clienteUpdated));
                    CompletableFuture.runAsync(() -> {
                        if (cacheCliente.get(id) != null) {
                            cacheCliente.remove(id);
                        }
                        cacheCliente.put(id, clienteUpdated);
                    });
                    return Either.right(clienteUpdated);
                }
            } else {
                return Either.left(new ClienteError.ClienteNotUpdated());
            }
        } catch (Exception e) {
            return Either.left(new ClienteError.ClienteNotUpdated());
        }
    }

    /**
     * Elimina un cliente por su identificador.
     *
     * @param id el identificador del cliente a eliminar.
     * @return un {@link Either} que contiene un {@link ClienteError} si ocurre un error, o el {@link Cliente} eliminado si la operación es exitosa.
     */
    @Override
    public Either<ClienteError, Cliente> delete(long id) {
        logger.info("Borrando cliente con id: {}", id);
        try {
            Optional<Usuario> usuarioRemoto = CompletableFuture.supplyAsync(() -> userRepository.getByIdSync(id)).get(10000, MILLISECONDS);
            if (usuarioRemoto.isPresent()) {
                CompletableFuture.runAsync(() -> userRepository.deleteUserSync(id));
                Optional<Cliente> clienteLocal = CompletableFuture.supplyAsync(() -> clienteRepository.getById(id)).get(10000, MILLISECONDS);
                if (clienteLocal.isPresent()) {
                    for (Tarjeta tarjeta : clienteLocal.get().getTarjeta()) {
                        CompletableFuture.runAsync(() -> tarjetaRepository.delete(tarjeta.getId()));
                    }
                    CompletableFuture.runAsync(() -> clienteRepository.delete(id));
                    notification.notify(new Notificacion<>(Notificacion.Tipo.DELETED, clienteLocal.get()));
                    CompletableFuture.runAsync(() -> {
                        if (cacheCliente.get(id) != null) {
                            cacheCliente.remove(id);
                        }
                    });
                }
                return Either.right(clienteLocal.get());
            } else {
                return Either.left(new ClienteError.ClienteNotDeleted());
            }
        } catch (Exception e) {
            return Either.left(new ClienteError.ClienteNotDeleted());
        }
    }

    /**
     * Obtiene las notificaciones de clientes como un flujo.
     *
     * @return un {@link Flux} de {@link Notificacion} que contiene las notificaciones de clientes.
     */
    public Flux<Notificacion<Cliente>> getNotifications() {
        return notification.getNotificationAsFlux();
    }
}
