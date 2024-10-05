package org.example.clientes.model;

import lombok.Builder;
import lombok.Data;
import org.example.tarjetas.model.Tarjeta;
import org.example.usuarios.models.Usuario;

import java.util.List;

@Data
@Builder
public class Cliente {
    //private Long id;
    private Usuario usuario;
    private List<Tarjeta> tarjetas;
}
