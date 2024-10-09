package org.example.clientes.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Cliente {
    private Long id;
    private Tarjeta tarjeta;
    private Usuario usuario;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
