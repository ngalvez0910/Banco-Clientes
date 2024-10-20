package org.example.clientes.repositories;

import org.example.clientes.model.Cliente;
import org.example.clientes.model.Tarjeta;
import org.example.clientes.model.Usuario;
import org.example.config.ConfigProperties;
import org.example.database.LocalDataBaseManager;
import org.example.rest.repository.UserRemoteRepository;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    @Order(1)
    @Test
    public void testGetAll() {
        List<Cliente> clientes = clienteRepository.getAll();
        assertNotNull(clientes);
        assertEquals(2, clientes.size());
        Cliente cliente = clientes.getFirst();
        assertEquals("Ana", cliente.getUsuario().getNombre());
        assertNotNull(cliente.getTarjeta());
        assertEquals(1, cliente.getTarjeta().size());
        Tarjeta tarjeta = cliente.getTarjeta().getFirst();
        assertEquals("1234567890123456", tarjeta.getNumeroTarjeta());
    }

    @Order(2)
    @Test
    public void testGetById() {
        Optional<Cliente> clienteOpt = clienteRepository.getById(1L);
        assertTrue(clienteOpt.isPresent());
        Cliente cliente = clienteOpt.get();
        assertEquals(1L, cliente.getUsuario().getId());
        assertEquals("Ana", cliente.getUsuario().getNombre());
        assertNotNull(cliente.getTarjeta());
        assertFalse(cliente.getTarjeta().isEmpty());
        Tarjeta tarjeta = cliente.getTarjeta().getFirst();
        assertEquals("1234567890123456", tarjeta.getNumeroTarjeta());
    }

    @Order(3)
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

        Optional<Cliente> cliente2 = clienteRepository.getById(createdCliente.getId());

        assertTrue(cliente2.isPresent());
        assertEquals(createdCliente.getId(), cliente2.get().getId());
        assertEquals("Pedro", cliente2.get().getUsuario().getNombre());
        assertNotNull(cliente2.get().getTarjeta());
        assertEquals(1, cliente2.get().getTarjeta().size());

        Tarjeta tarjetaRecibida = cliente2.get().getTarjeta().get(0);

        assertEquals("9876543210987654", tarjetaRecibida.getNumeroTarjeta());
    }

    @Order(4)
    @Test
    public void testUpdate() {

        Optional<Cliente> clienteOriginal = clienteRepository.getById(1L);
        assertTrue(clienteOriginal.isPresent());
        Usuario updatedUsuario = Usuario.builder()
                .nombre("Fran")
                .userName("fran")
                .email("fran@ejemplo.com")
                .createdAt(clienteOriginal.get().getUsuario().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        Tarjeta updatedTarjeta1 = Tarjeta.builder()
                .numeroTarjeta("6543210987654324")
                .nombreTitular("Fran")
                .fechaCaducidad(LocalDate.of(2026, 12, 31))
                .createdAt(clienteOriginal.get().getTarjeta().getFirst().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Tarjeta> tarjetasUpdate = new ArrayList<Tarjeta>();
        tarjetasUpdate.add(updatedTarjeta1);

        Cliente updatedCliente = Cliente.builder()
                .id(1L)
                .usuario(updatedUsuario)
                .tarjeta(tarjetasUpdate)
                .createdAt(clienteOriginal.get().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        Cliente clienteResult = clienteRepository.update(1L, updatedCliente);

        assertNotNull(clienteResult);
        assertEquals("Fran", clienteResult.getUsuario().getNombre());
        assertNotNull(clienteResult.getTarjeta());
        assertEquals(1, clienteResult.getTarjeta().size());

        Tarjeta tarjetaRecibida = clienteResult.getTarjeta().getFirst();

        assertEquals("6543210987654324", tarjetaRecibida.getNumeroTarjeta());
    }

    @Order(5)
    @Test
    public void testDelete() {

        Optional<Cliente> clienteOpt = clienteRepository.getById(1L);
        assertTrue(clienteOpt.isPresent());
        boolean deleted = clienteRepository.delete(1L);
        assertTrue(deleted);
        Optional<Cliente> deletedCliente = clienteRepository.getById(1L);
        assertFalse(deletedCliente.isPresent());
    }

    @Order(6)
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