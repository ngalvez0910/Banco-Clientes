package org.example.clientes.cache;

import org.example.clientes.model.Cliente;
import org.example.common.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementación de un caché para almacenar objetos de tipo Cliente.
 * Esta clase utiliza un LinkedHashMap para mantener el orden de acceso y limitar el tamaño del caché.
 * Cuando se supera el tamaño máximo del caché, se elimina el elemento más antiguo.
 * Proporciona métodos para obtener, añadir, eliminar y limpiar elementos del caché.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public class CacheClienteImpl implements Cache<Long, Cliente> {
    private static final Logger logger = LoggerFactory.getLogger(CacheClienteImpl.class);
    private final int cacheSize;
    private final Map<Long, Cliente> cache;

    /**
     * Constructor que inicializa el caché con un tamaño predeterminado.
     * El tamaño predeterminado del caché es 5.
     */
    public CacheClienteImpl() {
        this.cacheSize = 5;
        this.cache = new LinkedHashMap<>(cacheSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Cliente> eldest) {
                return size() > CacheClienteImpl.this.cacheSize;
            }
        };
    }

    /**
     * Obtiene un Cliente del caché utilizando su clave.
     *
     * @param key La clave del Cliente a obtener.
     * @return El Cliente asociado a la clave, o null si no existe en el caché.
     */
    @Override
    public Cliente get(Long key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        return null;
    }

    /**
     * Añade un Cliente al caché con la clave especificada.
     *
     * @param key La clave para asociar con el Cliente.
     * @param value El Cliente a almacenar en el caché.
     */
    @Override
    public void put(Long key, Cliente value) {
        logger.debug("Añadiendo a cache el valor de la clave: {}", key);
        cache.put(key, value);
    }

    /**
     * Elimina un Cliente del caché utilizando su clave.
     *
     * @param key La clave del Cliente a eliminar.
     */
    @Override
    public void remove(Long key) {
        logger.debug("Eliminando de cache el valor de la clave: {}", key);
        cache.remove(key);
    }

    /**
     * Limpia el caché eliminando todos los elementos.
     */
    @Override
    public void clear() {
        logger.debug("Limpiando la cache");
        cache.clear();
    }

    /**
     * Devuelve el número de elementos en el caché.
     *
     * @return El tamaño actual del caché.
     */
    @Override
    public int size() {
        return cache.size();
    }

    /**
     * Verifica si una clave existe en el caché.
     *
     * @param key La clave a buscar en el caché.
     * @return true si la clave existe, false en caso contrario.
     */
    @Override
    public boolean containsKey(Long key) {
        return cache.containsKey(key);
    }
}
