package org.example.clientes.services;

import org.example.clientes.model.Cliente;
import org.example.clientes.model.Notificacion;
import reactor.core.publisher.Flux;

public interface ClienteNotificacion {
    Flux<Notificacion<Cliente>> getNotificationAsFlux();

    void notify(Notificacion<Cliente> notificacion);
}
