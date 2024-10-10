package org.example.clientes.cache;

import org.example.clientes.model.Cliente;

public class CacheClienteImpl extends CacheImpl<Long, Cliente> implements CacheCliente {
    public CacheClienteImpl(int cacheSize) {
        super(cacheSize);
    }
}
