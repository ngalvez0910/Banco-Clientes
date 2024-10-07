package org.example.clientes.cache;

import org.example.clientes.model.Tarjeta;
import org.example.clientes.model.Usuario;
import org.example.common.Cache;

public interface CacheTarjeta extends Cache<Long, Tarjeta> {
    int TARJETA_CACHE_SIZE = 5;
}
