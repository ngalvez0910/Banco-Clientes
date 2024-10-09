package org.example.clientes.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cliente {
    private Tarjeta tarjeta;
    private Usuario usuario;
}
