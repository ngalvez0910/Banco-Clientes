package org.example.clientes.services;

import org.example.clientes.model.Cliente;
import org.example.clientes.model.Notificacion;
import reactor.core.publisher.Flux;

/**
 * Interfaz para manejar notificaciones relacionadas con clientes.
 * Proporciona métodos para obtener notificaciones como un flujo reactivo y para enviar nuevas notificaciones.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public interface ClienteNotificacion {

    /**
     * Obtiene un flujo reactivo de notificaciones de clientes.
     *
     * @return un {@link Flux} que emite notificaciones de tipo {@link Notificacion} para clientes
     */
    Flux<Notificacion<Cliente>> getNotificationAsFlux();

    /**
     * Envía una notificación para un cliente específico.
     *
     * @param notificacion la notificación que se enviará
     */
    void notify(Notificacion<Cliente> notificacion);
}
