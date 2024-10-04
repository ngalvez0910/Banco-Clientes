package org.example.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Clase que representa un cliente
 */
@Data
@Builder
public class Cliente {
    //private Long id;
    private Usuario usuario;
    private List<Tarjeta> tarjetas;
}
