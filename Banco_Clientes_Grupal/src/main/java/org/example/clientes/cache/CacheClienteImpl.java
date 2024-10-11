package org.example.clientes.cache;

import org.example.clientes.model.Cliente;
import org.example.common.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedHashMap;
import java.util.Map;

public class CacheClienteImpl implements Cache<Long, Cliente> {
    private static final Logger logger = LoggerFactory.getLogger(CacheClienteImpl.class);
    private final int cacheSize;
    private final Map<Long, Cliente> cache;

    public CacheClienteImpl() {
        this.cacheSize = 5;
        this.cache = new LinkedHashMap<>(cacheSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Cliente> eldest) {
                return size() > CacheClienteImpl.this.cacheSize;
            }
        };
    }

    @Override
    public Cliente get(Long key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        return null;
    }

    @Override
    public void put(Long key, Cliente value) {
        logger.debug("Añadiendo a cache el valor de la clave: {}", key);
        cache.put(key, value);
    }

    @Override
    public void remove(Long key) {
        logger.debug("Eliminando de cache el valor de la clave: {}", key);
        cache.remove(key);
    }

    @Override
    public void clear() {
        logger.debug("Limpiando la cache");
        cache.clear();
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean containsKey(Long key) {
        return cache.containsKey(key);
    }
}