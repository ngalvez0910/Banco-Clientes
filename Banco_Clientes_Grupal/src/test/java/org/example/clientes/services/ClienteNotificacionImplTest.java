package org.example.clientes.services;

import org.example.clientes.model.Cliente;
import org.example.clientes.model.Notificacion;
import org.example.clientes.model.Tarjeta;
import org.example.clientes.model.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteNotificacionImplTest {
    private ClienteNotificacionImpl clienteNotificacion;

    @BeforeEach
    void setUp() {
        clienteNotificacion = ClienteNotificacionImpl.getInstance();
    }

    @Test
    void testGetNotificationAsFlux() {
        Usuario usuario = new Usuario(1L, "Test", "TestUsername", "test@example.com", LocalDateTime.now(), LocalDateTime.now());
        List<Tarjeta> tarjetas = new ArrayList<>();
        Cliente cliente = Cliente.builder()
                .id(1L)
                .usuario(usuario)
                .tarjeta(tarjetas)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Notificacion<Cliente> notificacion1 = new Notificacion<>(Notificacion.Tipo.NEW, cliente);
        Notificacion<Cliente> notificacion2 = new Notificacion<>(Notificacion.Tipo.UPDATED, cliente);

        // Subscribing to the flux before sending notifications
        Flux<Notificacion<Cliente>> flux = clienteNotificacion.getNotificationAsFlux();

        StepVerifier.create(flux)
                .then(() -> {
                    clienteNotificacion.notify(notificacion1);
                    clienteNotificacion.notify(notificacion2);
                })
                .expectNext(notificacion1)
                .expectNext(notificacion2)
                .thenCancel()
                .verify();
    }

    @Test
    void testNotify() {
        Usuario usuario = new Usuario(1L, "Test", "TestUsername", "test@example.com", LocalDateTime.now(), LocalDateTime.now());
        List<Tarjeta> tarjetas = new ArrayList<>();
        Cliente cliente = Cliente.builder()
                .id(1L)
                .usuario(usuario)
                .tarjeta(tarjetas)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Notificacion<Cliente> notificacion = new Notificacion<>(Notificacion.Tipo.NEW, cliente);

        // Subscribing to the flux before sending notifications
        Flux<Notificacion<Cliente>> flux = clienteNotificacion.getNotificationAsFlux();

        StepVerifier.create(flux)
                .then(() -> clienteNotificacion.notify(notificacion))
                .expectNext(notificacion)
                .thenCancel()
                .verify();
    }
}