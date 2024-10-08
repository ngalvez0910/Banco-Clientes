package org.example.clientes.repositories;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;

import static javafx.beans.binding.Bindings.when;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class TarjetaRemoteRepositoryImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreContainer = new PostgreSQLContainer<>("postgres:12-alpine")
            .withDatabaseName("tarjetas_db")
            .withUsername("admin")
            .withPassword("admin123")
            .withInitScript("init.sql");

    @Mock
    private UserService userService;

    @InjectMocks
    private TarjetaRemoteRepositoryImpl tarjetaRemoteRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        tarjetaRemoteRepository = new TarjetaRemoteRepositoryImpl(userService);
        when(userService.getAll()).thenReturn();
        when(userService.getById(anyLong())).thenReturn();
    }

    @AfterAll
    public static void tearDown() {
        if (postgreContainer != null) {
            postgreContainer.stop();
        }
    }

    @Test
    void getAll() throws SQLException {
        var result = tarjetaRemoteRepository.getAll();
        assertTrue(result.isRight());
    }

    @Test
    public void getById() throws SQLException {
        var result = tarjetaRemoteRepository.getById(1L);
        assertTrue(result.isRight());
    }
}