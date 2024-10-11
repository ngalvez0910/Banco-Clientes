package org.example.clientes.services;

import io.vavr.control.Either;
import org.example.clientes.cache.CacheClienteImpl;
import org.example.clientes.errors.ClienteError;
import org.example.clientes.model.Cliente;
import org.example.clientes.model.Tarjeta;
import org.example.clientes.model.Usuario;
import org.example.clientes.repositories.ClienteRepository;
import org.example.clientes.repositories.TarjetaRemoteRepositoryImpl;
import org.example.rest.repository.UserRemoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ClienteServiceImpl implements ClienteService {
    private final UserRemoteRepository userRepository;
    private final TarjetaRemoteRepositoryImpl tarjetaRepository;
    private final ClienteRepository clienteRepository;
    private final CacheClienteImpl cacheCliente;
    private final Logger logger = LoggerFactory.getLogger(ClienteServiceImpl.class);

    public ClienteServiceImpl(UserRemoteRepository userRepository, TarjetaRemoteRepositoryImpl tarjetaRemoteRepository, ClienteRepository clienteRepository, CacheClienteImpl cacheCliente) {
        this.userRepository = userRepository;
        this.tarjetaRepository = tarjetaRemoteRepository;
        this.clienteRepository = clienteRepository;
        this.cacheCliente = cacheCliente;
    }

    @Override
    public Either<ClienteError, List<Cliente>> getAll() {
        logger.info("Obteniendo clientes...");
        try {
            List<Cliente> clientes = CompletableFuture.supplyAsync(clienteRepository::getAll).get(10000, MILLISECONDS);
            if (clientes.isEmpty()) {
                List<Usuario> usuarios = CompletableFuture.supplyAsync(userRepository::getAllSync).get(10000, MILLISECONDS);
                List<Tarjeta> tarjetas = CompletableFuture.supplyAsync(tarjetaRepository::getAll).get(10000, MILLISECONDS);

                for (Usuario usuario : usuarios) {
                    List<Tarjeta> tarjetasUser = new ArrayList<>();
                    for (Tarjeta tarjeta : tarjetas) {
                        if (tarjeta.getNombreTitular().equals(usuario.getNombre())) {
                            tarjetasUser.add(tarjeta);
                        }
                    }
                    Cliente cliente = new Cliente(usuario.getId(), usuario, tarjetasUser, LocalDateTime.now(), LocalDateTime.now());
                    clientes.add(cliente);

                    CompletableFuture.runAsync(() -> clienteRepository.create(cliente));
                }
            }
            if (clientes.isEmpty()) {
                return Either.left(new ClienteError.ClienteNotFound());
            } else {
                return Either.right(clientes);
            }
        } catch (Exception e) {
            return Either.left(new ClienteError.ClienteNotFound());
        }
    }

    @Override
    public Either<ClienteError, Cliente> getById(long id) {
        logger.info("Obteniendo cliente por id: {}", id);
        try {
            Cliente cliente = CompletableFuture.supplyAsync(() -> cacheCliente.get(id)).get(10000, MILLISECONDS);
            if (cliente == null) {
                Optional<Cliente> clienteRepo = CompletableFuture.supplyAsync(() -> clienteRepository.getById(id)).get(10000, MILLISECONDS);

                if (clienteRepo.isEmpty()) {
                    Usuario usuarioRemoto = CompletableFuture.supplyAsync(() -> userRepository.getByIdSync(id)).get(10000, MILLISECONDS);

                    if (usuarioRemoto == null) {
                        return Either.left(new ClienteError.ClienteNotFound());
                    } else {
                        List<Tarjeta> tarjetasRemotas = CompletableFuture.supplyAsync(tarjetaRepository::getAll).get(10000, MILLISECONDS);
                        List<Tarjeta> tarjetasUser = new ArrayList<>();
                        for (Tarjeta tarjeta : tarjetasRemotas) {
                            if (tarjeta.getNombreTitular().equals(usuarioRemoto.getNombre())) {
                                tarjetasUser.add(tarjeta);
                            }
                        }
                        cliente = new Cliente(usuarioRemoto.getId(), usuarioRemoto, tarjetasUser, LocalDateTime.now(), LocalDateTime.now());
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

    @Override
    public Either<ClienteError, Cliente> create(Cliente cliente) {
        logger.info("Creando cliente...");
        Usuario usuario = cliente.getUsuario();
        List<Tarjeta> tarjetas = cliente.getTarjeta();
        try {
            Usuario usuarioRemoto = CompletableFuture.supplyAsync(() -> userRepository.createUserSync(usuario)).get(10000, MILLISECONDS);
            for (Tarjeta tarjeta : tarjetas) {
                CompletableFuture.runAsync(() -> tarjetaRepository.create(tarjeta));
            }
            Cliente clienteRemoto = new Cliente(usuarioRemoto.getId(), usuarioRemoto, tarjetas, usuarioRemoto.getCreatedAt(), usuarioRemoto.getUpdatedAt());
            return Either.right(clienteRemoto);
        } catch (Exception e) {
            return Either.left(new ClienteError.ClienteNotCreated());
        }
    }

    @Override
    public Either<ClienteError, Cliente> update(long id, Cliente cliente) {
        logger.info("Actualizando cliente con id: {}", id);
        try {
            CompletableFuture.runAsync(() -> userRepository.getByIdSync(id)); //si no existe salta una excepción
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
                CompletableFuture.runAsync(() -> {
                    if (cacheCliente.get(id) != null) {
                        cacheCliente.remove(id);
                    }
                    cacheCliente.put(id, cliente);
                });
                return Either.right(clienteUpdated);
            }
        } catch (Exception e) {
            return Either.left(new ClienteError.ClienteNotUpdated());
        }
    }

    @Override
    public Either<ClienteError, Cliente> delete(long id) {
        logger.info("Borrando cliente con id: {}", id);
        try {
            CompletableFuture.runAsync(() -> userRepository.deleteUserSync(id)); //si falla salta excepción
            Optional<Cliente> clienteLocal = CompletableFuture.supplyAsync(() -> clienteRepository.getById(id)).get(10000, MILLISECONDS);
            if (clienteLocal.isPresent()) {
                CompletableFuture.runAsync(() -> clienteRepository.delete(id));
                for (Tarjeta tarjeta : clienteLocal.get().getTarjeta()) {
                    CompletableFuture.runAsync(() -> tarjetaRepository.delete(tarjeta.getId()));
                }
                CompletableFuture.runAsync(() -> {
                    if (cacheCliente.get(id) != null) {
                        cacheCliente.remove(id);
                    }
                });
            }
            return Either.right(clienteLocal.get());
        } catch (Exception e) {
            return Either.left(new ClienteError.ClienteNotDeleted());
        }
    }
}
