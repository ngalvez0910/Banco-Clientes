package org.example.clientes.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Usuario {
    private Long id;
    private String nombre;
    private String userName;
    private String email;
}
