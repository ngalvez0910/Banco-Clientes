package org.example.clientes.cache;

import org.example.clientes.model.Tarjeta;

public class CacheTarjetaImpl extends CacheImpl<Long, Tarjeta> implements CacheTarjeta{
    public CacheTarjetaImpl(int cacheSize) {
        super(cacheSize);
    }
}
