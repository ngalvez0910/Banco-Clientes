package org.example.clientes.repositories;

import org.example.clientes.model.Cliente;
import org.example.clientes.model.Tarjeta;
import org.example.clientes.model.Usuario;
import org.example.config.ConfigProperties;
import org.example.database.LocalDataBaseManager;
import org.example.rest.repository.UserRemoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class ClienteRepositoryImplTest {
    private static ClienteRepositoryImpl clienteRepository;
    private static LocalDataBaseManager dataBaseManager;

    @BeforeAll
    static void setUpAll() throws SQLException {
        ConfigProperties properties = new ConfigProperties();
        dataBaseManager = LocalDataBaseManager.getInstance(properties);
        dataBaseManager.connect();
        clienteRepository = new ClienteRepositoryImpl(dataBaseManager);
    }

    @BeforeEach
    void setUp() {

        var usuario = new Usuario(null, "Test", "TestUsername", "test@example.com", LocalDateTime.now(), LocalDateTime.now());
        var tarjeta = new Tarjeta(null, "1234567890123456", "Test", LocalDate.of(2025, 12, 31), LocalDateTime.now(), LocalDateTime.now());
        var cliente = new Cliente(null, usuario, List.of(tarjeta), LocalDateTime.now(), LocalDateTime.now());

        clienteRepository.create(cliente);


        Optional<Cliente> clienteVerificado = clienteRepository.getById(cliente.getId());
        assertTrue(clienteVerificado.isPresent(), "El cliente no fue creado correctamente.");
    }

    @AfterEach
    void tearDown() {
        // Limpia los datos después de cada prueba
       clienteRepository.deleteAll();
    }

    @Test
    public void testGetAll() {
        List<Cliente> clientes = clienteRepository.getAll();
        assertNotNull(clientes);
        assertEquals(1, clientes.size());
        Cliente cliente = clientes.get(0);
        assertEquals("Test", cliente.getUsuario().getNombre());
        assertNotNull(cliente.getTarjeta());
        assertEquals(1, cliente.getTarjeta().size());
        Tarjeta tarjeta = cliente.getTarjeta().get(0);
        assertEquals("1234567890123456", tarjeta.getNumeroTarjeta());
    }

    @Test
    public void testGetById() {
        Optional<Cliente> clienteOpt = clienteRepository.getById(1L);
        assertTrue(clienteOpt.isPresent());
        Cliente cliente = clienteOpt.get();
        assertEquals(1L, cliente.getUsuario().getId());
        assertEquals("Test", cliente.getUsuario().getNombre());
        assertNotNull(cliente.getTarjeta());
        assertFalse(cliente.getTarjeta().isEmpty());
        Tarjeta tarjeta = cliente.getTarjeta().get(0);
        assertEquals("1234567890123456", tarjeta.getNumeroTarjeta());
    }

    @Test
    public void testCreate() {
        Usuario usuario = Usuario.builder()
                .id(2L)
                .nombre("Pedro")
                .userName("pedro")
                .email("pedro@ejemplo.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Tarjeta tarjeta1 = Tarjeta.builder()
                .id(2L)
                .numeroTarjeta("9876543210987654")
                .nombreTitular("Pedro")
                .fechaCaducidad(LocalDate.of(2025, 12, 31))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Tarjeta tarjeta2 = Tarjeta.builder()
                .id(3L)
                .numeroTarjeta("1234567890123456")
                .nombreTitular("Pedro")
                .fechaCaducidad(LocalDate.of(2026, 12, 31))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        List<Tarjeta> tarjetas = Arrays.asList(tarjeta1, tarjeta2);
        Cliente cliente = Cliente.builder()
                .id(2L)
                .usuario(usuario)
                .tarjeta(tarjetas)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Cliente createdCliente = clienteRepository.create(cliente);
        assertNotNull(createdCliente);
        assertNotNull(createdCliente.getUsuario().getId());
        assertEquals("Pedro", createdCliente.getUsuario().getNombre());
        assertNotNull(createdCliente.getTarjeta());
        assertEquals(2, createdCliente.getTarjeta().size());
        Optional<Cliente> retrievedCliente = clienteRepository.getById(createdCliente.getId());
        assertTrue(retrievedCliente.isPresent());
        assertEquals(createdCliente.getId(), retrievedCliente.get().getId());
        assertEquals("Pedro", retrievedCliente.get().getUsuario().getNombre());
        assertNotNull(retrievedCliente.get().getTarjeta());
        assertEquals(2, retrievedCliente.get().getTarjeta().size());
        Tarjeta retrievedTarjeta1 = retrievedCliente.get().getTarjeta().get(0);
        Tarjeta retrievedTarjeta2 = retrievedCliente.get().getTarjeta().get(1);
        assertEquals("9876543210987654", retrievedTarjeta1.getNumeroTarjeta());
        assertEquals("1234567890123456", retrievedTarjeta2.getNumeroTarjeta());
    }

    @Test
    public void testUpdate() {

        Optional<Cliente> clienteOriginal = clienteRepository.getById(1L);
        assertTrue(clienteOriginal.isPresent());
        Usuario updatedUsuario = Usuario.builder()
                .id(1L)
                .nombre("Pedro")
                .userName("pedro")
                .email("pedro@ejemplo.com")
                .createdAt(clienteOriginal.get().getUsuario().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        Tarjeta updatedTarjeta1 = Tarjeta.builder()
                .id(1L)
                .numeroTarjeta("6543210987654321")
                .nombreTitular("Pedro")
                .fechaCaducidad(LocalDate.of(2026, 12, 31))
                .createdAt(clienteOriginal.get().getTarjeta().get(0).getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        Tarjeta updatedTarjeta2 = Tarjeta.builder()
                .id(2L)
                .numeroTarjeta("1234567890123456")
                .nombreTitular("Pedro")
                .fechaCaducidad(LocalDate.of(2027, 12, 31))
                .createdAt(clienteOriginal.get().getTarjeta().get(0).getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        List<Tarjeta> updatedTarjetas = Arrays.asList(updatedTarjeta1, updatedTarjeta2);
        Cliente updatedCliente = Cliente.builder()
                .id(1L)
                .usuario(updatedUsuario)
                .tarjeta(updatedTarjetas)
                .createdAt(clienteOriginal.get().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
        Cliente clienteResult = clienteRepository.update(1L, updatedCliente);
        assertNotNull(clienteResult);
        assertEquals("Pedro", clienteResult.getUsuario().getNombre());
        Optional<Cliente> retrievedCliente = clienteRepository.getById(1L);
        assertTrue(retrievedCliente.isPresent());
        assertEquals("Pedro", retrievedCliente.get().getUsuario().getNombre());
        assertNotNull(retrievedCliente.get().getTarjeta());
        assertEquals(2, retrievedCliente.get().getTarjeta().size());
        Tarjeta retrievedTarjeta1 = retrievedCliente.get().getTarjeta().get(0);
        Tarjeta retrievedTarjeta2 = retrievedCliente.get().getTarjeta().get(1);
        assertEquals("6543210987654321", retrievedTarjeta1.getNumeroTarjeta());
        assertEquals("1234567890123456", retrievedTarjeta2.getNumeroTarjeta());
    }

    @Test
    public void testDelete() {

        Optional<Cliente> clienteOpt = clienteRepository.getById(1L);
        assertTrue(clienteOpt.isPresent());
        boolean deleted = clienteRepository.delete(1L);
        assertTrue(deleted);
        Optional<Cliente> deletedCliente = clienteRepository.getById(1L);
        assertFalse(deletedCliente.isPresent());
    }

    @Test
    public void testDeleteAll() {
        List<Cliente> clientesAntes = clienteRepository.getAll();
        assertFalse(clientesAntes.isEmpty());
        boolean deletedAll = clienteRepository.deleteAll();
        assertTrue(deletedAll);
        List<Cliente> clientesDespues = clienteRepository.getAll();
        assertTrue(clientesDespues.isEmpty());
    }
}