package org.example.clientes.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * La clase Usuario representa los detalles de un usuario
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 */
@Data
@Builder
public class Usuario {

    private Long id;
    private String nombre;
    private String userName;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
