package org.example.clientes.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Cliente {
    private Long id;
    private Usuario usuario;
    private List<Tarjeta> tarjeta;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Cliente(Long id, Usuario usuario, List<Tarjeta> tarjeta, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.usuario = usuario;
        this.tarjeta = tarjeta;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
