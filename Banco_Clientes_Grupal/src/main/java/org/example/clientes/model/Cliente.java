package org.example.clientes.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase modelo que representa a un cliente en el sistema.
 * Utiliza las anotaciones de Lombok para generar automáticamente los métodos
 * getter, setter, equals, hashCode, toString, y un patrón de construcción (builder).
 */
@Data
@Builder
public class Cliente {
    private Long id;                     // Identificador único del cliente
    private Usuario usuario;             // Usuario asociado al cliente
    private List<Tarjeta> tarjeta;       // Lista de tarjetas asociadas al cliente
    private LocalDateTime createdAt;     // Fecha y hora de creación del cliente
    private LocalDateTime updatedAt;     // Fecha y hora de la última actualización del cliente

    /**
     * Constructor completo para la clase Cliente.
     *
     * @param id Identificador único del cliente.
     * @param usuario Usuario asociado al cliente.
     * @param tarjeta Lista de tarjetas asociadas al cliente.
     * @param createdAt Fecha y hora de creación del cliente.
     * @param updatedAt Fecha y hora de la última actualización del cliente.
     */
    public Cliente(Long id, Usuario usuario, List<Tarjeta> tarjeta, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.usuario = usuario;
        this.tarjeta = tarjeta;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
