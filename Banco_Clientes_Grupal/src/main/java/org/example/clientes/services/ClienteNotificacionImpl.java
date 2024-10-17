package org.example.clientes.services;

import org.example.clientes.model.Cliente;
import org.example.clientes.model.Notificacion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * Implementación de la interfaz {@link ClienteNotificacion} para gestionar notificaciones de clientes.
 * Esta clase sigue el patrón Singleton y utiliza un flujo reactivo para manejar las notificaciones.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public class ClienteNotificacionImpl implements ClienteNotificacion {
    private static ClienteNotificacionImpl INSTANCE;
    private final Flux<Notificacion<Cliente>> clientesNotificationFlux;
    private FluxSink<Notificacion<Cliente>> clientesNotification;

    /**
     * Constructor privado que inicializa el flujo de notificaciones de clientes.
     */
    public ClienteNotificacionImpl() {
        this.clientesNotificationFlux = Flux.<Notificacion<Cliente>>create(emitter -> {
            this.clientesNotification = emitter;
        }).share();
    }

    /**
     * Obtiene la instancia única de {@link ClienteNotificacionImpl}.
     *
     * @return la instancia única de ClienteNotificacionImpl
     */
    public static synchronized ClienteNotificacionImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClienteNotificacionImpl();
        }
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Notificacion<Cliente>> getNotificationAsFlux() {
        return clientesNotificationFlux;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notify(Notificacion<Cliente> notificacion) {
        if (this.clientesNotification != null) {
            this.clientesNotification.next(notificacion);
        } else {
            throw new IllegalStateException("FluxSink not initialized");
        }
    }
}