package org.example.clientes.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * La clase Usuario representa los detalles de un usuario
 */
@Data
@Builder
public class Usuario {

    private Long id;                     // Identificador único del usuario
    private String nombre;               // Nombre completo del usuario
    private String userName;             // Nombre de usuario (username) único
    private String email;                // Dirección de correo electrónico del usuario
    private LocalDateTime createdAt;     // Fecha y hora de creación del usuario
    private LocalDateTime updatedAt;     // Fecha y hora de la última actualización del usuario

    /**
     * Constructor completo para crear una instancia de la clase Usuario.
     *
     * @param id Identificador único del usuario
     * @param nombre Nombre completo del usuario
     * @param userName Nombre de usuario (username) único
     * @param email Dirección de correo electrónico del usuario
     * @param createdAt Fecha y hora de creación del usuario
     * @param updatedAt Fecha y hora de la última actualización del usuario
     */
    public Usuario(Long id, String nombre, String userName, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.userName = userName;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
