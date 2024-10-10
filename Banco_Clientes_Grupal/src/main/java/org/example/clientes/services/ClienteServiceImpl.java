package org.example.clientes.services;

import io.vavr.control.Either;
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

public class ClienteServiceImpl implements ClienteService {
    private final UserRemoteRepository userRepository;
    private final TarjetaRemoteRepositoryImpl tarjetaRepository;
    private final ClienteRepository clienteRepository;
    private final Logger logger = LoggerFactory.getLogger(ClienteServiceImpl.class);

    public ClienteServiceImpl(UserRemoteRepository userRepository, TarjetaRemoteRepositoryImpl tarjetaRemoteRepository, ClienteRepository clienteRepository) {
        this.userRepository = userRepository;
        this.tarjetaRepository = tarjetaRemoteRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Either<ClienteError, List<Cliente>> getAll() {
        logger.info("Obteniendo clientes...");
        List<Cliente> clientes = clienteRepository.getAll();
        if (clientes.isEmpty()) {
            List<Usuario> usuarios = userRepository.getAllSync();
            List<Tarjeta> tarjetas = tarjetaRepository.getAll();

            for (Usuario usuario : usuarios) {
                List<Tarjeta> tarjetasUser = new ArrayList<>();
                for (Tarjeta tarjeta : tarjetas) {
                    if (tarjeta.getNombreTitular().equals(usuario.getNombre())) {
                        tarjetasUser.add(tarjeta);
                    }
                }
                Cliente cliente = new Cliente(1L, usuario, tarjetasUser, LocalDateTime.now(), LocalDateTime.now());
                clientes.add(cliente);

                clienteRepository.create(cliente);
            }
        }
        if (clientes.isEmpty()) {
            return Either.left(new ClienteError.ClienteNotFound());
        } else {
            return Either.right(clientes);
        }
    }

    @Override
    public Either<ClienteError, Cliente> getById(int id) {
        return null;
    }

    @Override
    public Either<ClienteError, Cliente> create(Cliente cliente) {
        return null;
    }

    @Override
    public Either<ClienteError, Cliente> update(long id, Cliente cliente) {
        return null;
    }

    @Override
    public Either<ClienteError, Cliente> delete(long id) {
        return null;
    }
}
