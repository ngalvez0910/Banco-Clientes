package org.example.clientes.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Usuario {
    private Long id;
    private String nombre;
    private String userName;
    private String email;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
