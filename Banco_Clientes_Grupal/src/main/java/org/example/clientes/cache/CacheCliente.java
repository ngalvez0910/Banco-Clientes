package org.example.clientes.cache;

import org.example.clientes.model.Cliente;
import org.example.clientes.model.Tarjeta;
import org.example.common.Cache;

public interface CacheCliente extends Cache<Long, Cliente> {
    int CLIENTE_CACHE_SIZE = 5;
}
