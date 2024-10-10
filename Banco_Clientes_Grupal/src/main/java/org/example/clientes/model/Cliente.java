package org.example.clientes.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Cliente {
    private Long id;
    private List<Tarjeta> tarjeta;
    private Usuario usuario;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
