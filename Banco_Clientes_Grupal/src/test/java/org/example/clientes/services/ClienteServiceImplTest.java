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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    ClienteNotificacion notificacion;

    @Mock
    UserRemoteRepository userRepository;

    @Mock
    TarjetaRemoteRepositoryImpl tarjetaRepository;

    @Mock
    CacheClienteImpl cacheCliente;

    @InjectMocks
    ClienteServiceImpl clienteService;

    @BeforeEach
    void setUp() {
        clienteService = new ClienteServiceImpl(userRepository, tarjetaRepository, clienteRepository, cacheCliente, notificacion);
    }

    @Test
    void GetAll() throws Exception {

        Usuario usuario = Usuario.builder().id(1L).nombre("John Doe").build();
        List<Tarjeta> tarjetas = Arrays.asList(Tarjeta.builder().numeroTarjeta("1234567890123456").nombreTitular("John Doe").build());
        Cliente cliente = Cliente.builder()
                .id(1L)
                .usuario(usuario)
                .tarjeta(tarjetas)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(clienteRepository.getAll()).thenReturn(Arrays.asList(cliente));

        Either<ClienteError, List<Cliente>> result = clienteService.getAll();

        assertTrue(result.isRight());
        assertEquals(1, result.get().size());
    }



    @Test
    void GetById() throws Exception {
        long clienteId = 1L;
        Usuario usuario = Usuario.builder().id(clienteId).nombre("John Doe").build();
        List<Tarjeta> tarjetas = Arrays.asList(Tarjeta.builder().numeroTarjeta("1234567890123456").nombreTitular("John Doe").build());
        Cliente cliente = Cliente.builder()
                .id(clienteId)
                .usuario(usuario)
                .tarjeta(tarjetas)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(cacheCliente.get(clienteId)).thenReturn(cliente);

        Either<ClienteError, Cliente> result = clienteService.getById(clienteId);

        assertTrue(result.isRight());
        assertEquals(cliente, result.get());
    }

    @Test
    void findByIdNotExist() throws Exception {
        long clienteId = 999L;
        when(cacheCliente.get(clienteId)).thenReturn(null);
        when(clienteRepository.getById(clienteId)).thenReturn(Optional.empty());


        Either<ClienteError, Cliente> result = clienteService.getById(clienteId);


        assertTrue(result.isLeft());
        assertTrue(result.getLeft() instanceof ClienteError.ClienteNotFound);

    }


        @Test
    void Create() throws Exception {

        Usuario usuario = Usuario.builder().id(1L).nombre("Jane Doe").build();
        List<Tarjeta> tarjetas = Arrays.asList(Tarjeta.builder().numeroTarjeta("9876543210987654").nombreTitular("Jane Doe").build());
        Cliente cliente = Cliente.builder()
                .usuario(usuario)
                .tarjeta(tarjetas)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.createUserSync(usuario)).thenReturn(Optional.of(usuario));
        doNothing().when(notificacion).notify(any());


        Either<ClienteError, Cliente> result = clienteService.create(cliente);


        assertTrue(result.isRight());
    }

    @Test
    void Update() throws Exception {

        long clienteId = 1L;
        Usuario usuario = Usuario.builder().id(clienteId).nombre("John Smith").build();
        List<Tarjeta> tarjetas = Arrays.asList(Tarjeta.builder().numeroTarjeta("6543210987654321").nombreTitular("John Smith").build());
        Cliente cliente = Cliente.builder()
                .id(clienteId)
                .usuario(usuario)
                .tarjeta(tarjetas)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.getByIdSync(clienteId)).thenReturn(Optional.of(usuario));
        when(clienteRepository.update(clienteId, cliente)).thenReturn(cliente);


        Either<ClienteError, Cliente> result = clienteService.update(clienteId, cliente);


        assertTrue(result.isRight());
        assertEquals(cliente, result.get());
    }

    @Test
    void updateNotExist() throws Exception {
        long clienteId = 999L;
        Cliente cliente = Cliente.builder()
                .id(clienteId)
                .usuario(Usuario.builder().id(clienteId).nombre("Jane Doe").build())
                .tarjeta(Arrays.asList(Tarjeta.builder().numeroTarjeta("9876543210987654").nombreTitular("Jane Doe").build()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.getByIdSync(clienteId)).thenReturn(Optional.empty());


        Either<ClienteError, Cliente> result = clienteService.update(clienteId, cliente);


        assertTrue(result.isLeft());
        assertTrue(result.getLeft() instanceof ClienteError.ClienteNotUpdated);
    }

    @Test
    void Delete() throws Exception {

        long clienteId = 1L;
        Usuario usuario = Usuario.builder().id(clienteId).nombre("John Doe").build();
        List<Tarjeta> tarjetas = Arrays.asList(Tarjeta.builder().numeroTarjeta("1234567890123456").nombreTitular("John Doe").build());
        Cliente cliente = Cliente.builder()
                .id(clienteId)
                .usuario(usuario)
                .tarjeta(tarjetas)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.getByIdSync(clienteId)).thenReturn(Optional.of(usuario));
        when(clienteRepository.getById(clienteId)).thenReturn(Optional.of(cliente));


        Either<ClienteError, Cliente> result = clienteService.delete(clienteId);


        assertTrue(result.isRight());
    }

    @Test
    void deleteNotExist() throws Exception {
        long clienteId = 999L;
        when(userRepository.getByIdSync(clienteId)).thenReturn(Optional.empty());


        Either<ClienteError, Cliente> result = clienteService.delete(clienteId);


        assertTrue(result.isLeft());
        assertTrue(result.getLeft() instanceof ClienteError.ClienteNotDeleted);
    }
}