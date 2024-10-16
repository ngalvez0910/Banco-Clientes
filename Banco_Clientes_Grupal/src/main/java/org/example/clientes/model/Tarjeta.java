package org.example.clientes.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * La clase Tarjeta representa los detalles de una tarjeta de crédito
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
@Data
@Builder
public class Tarjeta {

    private Long id;
    private String nombreTitular;
    private String numeroTarjeta;
    private LocalDate fechaCaducidad;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Constructor completo para crear una instancia de la clase Tarjeta.
     *
     * @param id Identificador único de la tarjeta
     * @param nombreTitular Nombre del titular de la tarjeta
     * @param numeroTarjeta Número de la tarjeta
     * @param fechaCaducidad Fecha de caducidad de la tarjeta
     * @param createdAt Fecha y hora de creación de la tarjeta
     * @param updatedAt Fecha y hora de la última actualización de la tarjeta
     */
    public Tarjeta(Long id, String nombreTitular, String numeroTarjeta, LocalDate fechaCaducidad, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nombreTitular = nombreTitular;
        this.numeroTarjeta = numeroTarjeta;
        this.fechaCaducidad = fechaCaducidad;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
