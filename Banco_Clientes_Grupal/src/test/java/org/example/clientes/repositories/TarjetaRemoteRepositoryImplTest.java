package org.example.clientes.repositories;

import org.example.clientes.model.Tarjeta;
import org.example.database.RemoteDataBaseManager;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TarjetaRemoteRepositoryImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreContainer = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("tarjetas_db")
            .withUsername("admin")
            .withPassword("admin123")
            .withInitScript("init.sql");

    private static RemoteDataBaseManager dataBaseManager;
    private static TarjetaRemoteRepositoryImpl tarjetaRemoteRepository;

    @BeforeAll
    public static void setUp() throws SQLException {
        dataBaseManager = new RemoteDataBaseManager(){
            @Override
                public Connection getConnection() throws SQLException {
                    return DriverManager.getConnection(
                            postgreContainer.getJdbcUrl(),
                            postgreContainer.getUsername(),
                            postgreContainer.getPassword()
                    );
                }
        };

    tarjetaRemoteRepository = new TarjetaRemoteRepositoryImpl(dataBaseManager);
    }

    @AfterAll
    public static void tearDown() {
        if (postgreContainer != null) {
            postgreContainer.stop();
        }
    }

    @Test
    @Order(1)
    void getAll() {
        List<Tarjeta> tarjetaTest = tarjetaRemoteRepository.getAll();

        assertAll(() -> {
            assertNotNull(tarjetaTest);
            assertFalse(tarjetaTest.isEmpty(), "La lista de tarjetas no debe estar vacía");
            assertEquals("Ana", tarjetaTest.get(0).getNombreTitular());
        });
    }

    @Test
    @Order(2)
    public void GetById() {
        var tarjeta = tarjetaRemoteRepository.getById(1L);

        assertAll(() -> {
            assertTrue(tarjeta.isPresent());
            assertEquals("Ana", tarjeta.get().getNombreTitular());
        });
    }

    @Test
    @Order(3)
    public void GetByIdNotFound() {
        var tarjeta = tarjetaRemoteRepository.getById(999L);

        assertAll(() -> {
            assertFalse(tarjeta.isPresent());
        });
    }

    @Test
    @Order(4)
    public void Create() {
        Tarjeta tarjeta = Tarjeta.builder()
                .nombreTitular("Juan")
                .numeroTarjeta("0987654321098765")
                .fechaCaducidad(LocalDate.of(2022, 12, 31))
                .build();

        var tarjetaCreada = tarjetaRemoteRepository.create(tarjeta);

        assertAll(() -> {
            assertNotNull(tarjetaCreada);
            assertEquals("Juan", tarjetaCreada.getNombreTitular());
        });
    }

    @Test
    @Order(5)
    public void Update() {
        Tarjeta tarjeta = Tarjeta.builder()
                .id(1L)
                .nombreTitular("Ana Updated")
                .numeroTarjeta("1234567890123456")
                .fechaCaducidad(LocalDate.of(2022, 12, 31))
                .build();

        var tarjetaActualizada = tarjetaRemoteRepository.update(1L, tarjeta);

        assertAll(() -> {
            assertNotNull(tarjetaActualizada);
            assertEquals("Ana Updated", tarjetaActualizada.getNombreTitular());
        });
    }

    @Test
    @Order(6)
    public void UpdateNotFound() {
        Tarjeta tarjeta = Tarjeta.builder()
                .id(999L)
                .nombreTitular("Ana Updated")
                .numeroTarjeta("1234567890123456")
                .fechaCaducidad(LocalDate.of(2022, 12, 31))
                .build();

        var tarjetaActualizada = tarjetaRemoteRepository.update(999L, tarjeta);

        assertAll(() -> {
            assertNull(tarjetaActualizada);
        });
    }

    @Test
    @Order(7)
    public void Delete() {
        var tarjetaEliminada = tarjetaRemoteRepository.delete(99L);

        assertAll(() -> {
            assertFalse(tarjetaEliminada, "La tarjeta no debería existir");
        });
    }
}