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
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TarjetaRemoteRepositoryImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreContainer = new PostgreSQLContainer<>("postgres:12-alpine")
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
    void getAll()  {
        List<Tarjeta> tarjeta =tarjetaRemoteRepository.getAll();

        assertAll(() -> {
            assertNotNull (tarjeta);
            assertEquals("Ana", tarjeta.getFirst().getNombreTitular());
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
            assertTrue(tarjeta.isEmpty());
        });
    }

    @Test
    @Order(4)
    public void Create() {
        Tarjeta tarjeta = Tarjeta.builder()
                .nombreTitular("Juan")
                .numeroTarjeta("098765432123456")
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
                .nombreTitular("Ana Actualizada")
                .numeroTarjeta("1234567890123456")
                .fechaCaducidad(LocalDate.parse("01/25"))
                .build();

        var tarjetaActualizada = tarjetaRemoteRepository.update(1L, tarjeta);

        assertAll(() -> {
            assertNotNull(tarjetaActualizada);
            assertEquals("Ana Actualizada", tarjetaActualizada.getNombreTitular());
        });
    }

    @Test
    @Order(6)
    public void UpdateNotFound() {
        Tarjeta tarjeta = Tarjeta.builder()
                .id(999L)
                .nombreTitular("Ana Actualizada")
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
        var tarjeta= tarjetaRemoteRepository.delete(99L);

        assertAll(() -> {
            assertFalse(tarjeta);
        });
    }
}