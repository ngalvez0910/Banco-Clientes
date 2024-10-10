package org.example.clientes.dto;

import lombok.Builder;
import lombok.Data;

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