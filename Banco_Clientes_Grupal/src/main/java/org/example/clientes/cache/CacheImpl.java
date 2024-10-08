package org.example.clientes.cache;

import org.example.common.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class CacheImpl<K, V> implements Cache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(CacheImpl.class);
    private final int cacheSize;
    private final LinkedHashMap<K, V> cache;

    public CacheImpl(int cacheSize) {
        this.cacheSize = cacheSize;
        this.cache = new LinkedHashMap<>(cacheSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > CacheImpl.this.cacheSize;
            }
        };
    }

    @Override
    public V get(K key) {
        logger.debug("Obteniendo el valor de la clave: {}", key);
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        logger.debug("Añadiendo a cache el valor de la clave: {}", key);
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
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
        logger.debug("Obteniendo el tamaño de la cache");
        return cache.size();
    }

    @Override
    public Collection<V> values() {
        logger.debug("Obteniendo los valores de la cache");
        return cache.values();
    }

    @Override
    public boolean isEmpty() {
        logger.debug("Comprobando si la cache está vacía");
        return cache.isEmpty();
    }

    @Override
    public boolean isNotEmpty() {
        logger.debug("Comprobando si la cache no está vacía");
        return !isEmpty();
    }

    @Override
    public boolean containsKey(K key) {
        logger.debug("Comprobando si existe la clave en la cache: {}", key);
        return cache.containsKey(key);
    }
}
