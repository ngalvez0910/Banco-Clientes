package org.example.clientes.repositories;

import org.example.clientes.model.Cliente;
import org.example.clientes.model.Tarjeta;
import org.example.clientes.model.Usuario;
import org.example.database.LocalDataBaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClienteRepositoryImplTest {

    private ClienteRepositoryImpl clienteRepository;

    @BeforeAll
    static void setUpAll() throws SQLException {
        ConfigProperties properties = new ConfigProperties("application.properties");
        dataBaseManager = LocalDataBaseManager.getInstance();
        dataBaseManager.connect();
        dataBaseManager.initializeDatabase();
        clienteRepository =  ClienteRepositoryImpl.getInstance(LocalDataBaseManager.getInstance());

    }
    @BeforeEach
    void setUp() {
        usuarioRepository.saveUser(Usuario.builder()
                .id(1l)
                .name("Test")
                .username("TestUsername")
                .email("test@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        creditCardRepository.saveCreditCard(TarjetaCredito.builder()
                .id(UUID.fromString("3a62d823-e068-4560-a464-9daa369e03d6"))
                .numero("1234567890123456")
                .nombreTitular("Test")
                .clientID(1L)
                .fechaCaducidad("12/24")
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
    }

    @Test
    public void GetAll() {
        List<Cliente> clientes = clienteRepository.getAll();
        assertNotNull(clientes);
        assertEquals(1, clientes.size());

        Cliente cliente = clientes.get(0);
        assertEquals("Pedro pedro", cliente.getUsuario().getNombre());
        assertEquals("1234567890123456", cliente.getTarjeta().getNumeroTarjeta());
    }

    @Test
    public void GetById() {
        Optional<Cliente> clienteOpt = clienteRepository.getById(1);
        assertTrue(clienteOpt.isPresent());

        Cliente cliente = clienteOpt.get();
        assertEquals(1L, cliente.getUsuario().getId());
        assertEquals("Pedro", cliente.getUsuario().getNombre());
        assertEquals("1234567890123456", cliente.getTarjeta().getNumeroTarjeta());
    }

    @Test
    public void Create() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Pedro")
                .userName("pedro")
                .email("pedro@ejemplo.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Tarjeta tarjeta = Tarjeta.builder()
                .id(1L)
                .numeroTarjeta("9876543210987654")
                .nombreTitular("Pedro")
                .fechaCaducidad(LocalDate.of(2025, 12, 31))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Cliente cliente = Cliente.builder()
                .usuario(usuario)
                .tarjeta(tarjeta)
                .build();

        Cliente createdCliente = clienteRepository.create(cliente);

        assertNotNull(createdCliente);
        assertNotNull(createdCliente.getUsuario().getId());
        assertEquals("Pedro", createdCliente.getUsuario().getNombre());
        assertEquals("9876543210987654", createdCliente.getTarjeta().getNumeroTarjeta());


        Optional<Cliente> retrievedCliente = clienteRepository.getById(createdCliente.getId());
        assertTrue(retrievedCliente.isPresent());
        assertEquals(createdCliente.getId(), retrievedCliente.get().getId());
    }



    @Test
    public void Update() {

        Optional<Cliente> clienteOriginal = clienteRepository.getById(13145346L);
        assertTrue(clienteOriginal.isPresent());

        Usuario updatedUsuario = Usuario.builder()
                .id(13145346L)
                .nombre("Pedro")
                .userName("pedro")
                .email("pedro@ejemplo.com")
                .createdAt(clienteOriginal.get().getUsuario().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        Tarjeta updatedTarjeta = Tarjeta.builder()
                .id(1234L)
                .numeroTarjeta("6543210987654321")
                .nombreTitular("Pedro")
                .fechaCaducidad(LocalDate.of(2026, 12, 31))
                .createdAt(clienteOriginal.get().getTarjeta().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        Cliente updatedCliente = Cliente.builder()
                .usuario(updatedUsuario)
                .tarjeta(updatedTarjeta)
                .build();

        Cliente clienteResult = clienteRepository.update(1234L, updatedCliente);
        assertNotNull(clienteResult);
        assertEquals("Pedro", clienteResult.getUsuario().getNombre());
        assertEquals("6543210987654321", clienteResult.getTarjeta().getNumeroTarjeta());


        Optional<Cliente> retrievedCliente = clienteRepository.getById(1234L);
        assertTrue(retrievedCliente.isPresent());
        assertEquals("Pedro", retrievedCliente.get().getUsuario().getNombre());
        assertEquals("6543210987654321", retrievedCliente.get().getTarjeta().getNumeroTarjeta());
    }

    @Test
    public void Delete() {

        Optional<Cliente> clienteOpt = clienteRepository.getById(999999999999L);
        assertTrue(clienteOpt.isPresent());


        boolean deleted = clienteRepository.delete(999999999999L);
        assertTrue(deleted);


        Optional<Cliente> deletedCliente = clienteRepository.getById(999999999999L);
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