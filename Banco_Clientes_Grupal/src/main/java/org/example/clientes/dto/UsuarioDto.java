package org.example.clientes.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO para representar un usuario.
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 */
@Data
@Builder
public class UsuarioDto {
    private Long id;
    private String nombre;
    private String userName;
    private String email;
    private String createdAt;
    private String updatedAt;
}
