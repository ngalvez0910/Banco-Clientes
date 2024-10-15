package org.example.clientes.services;

import org.example.clientes.model.Cliente;
import org.example.clientes.model.Notificacion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

public class ClienteNotificacionImpl implements ClienteNotificacion {
    private static ClienteNotificacionImpl INSTANCE = new ClienteNotificacionImpl();

    private final Flux<Notificacion<Cliente>> clientesNotificationFlux;
    private FluxSink<Notificacion<Cliente>> clientesNotification;

    public ClienteNotificacionImpl() {
        this.clientesNotificationFlux = Flux.<Notificacion<Cliente>>create(emitter -> this.clientesNotification = emitter).share();
    }

    public static ClienteNotificacionImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClienteNotificacionImpl();
        }
        return INSTANCE;
    }

    @Override
    public Flux<Notificacion<Cliente>> getNotificationAsFlux() {
        return clientesNotificationFlux;
    }

    @Override
    public void notify(Notificacion<Cliente> notificacion) {
        clientesNotification.next(notificacion);
    }
}
