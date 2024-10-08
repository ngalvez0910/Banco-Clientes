package org.example.common;

import java.util.Collection;

public interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);
    void remove(K key);
    void clear();

    int size();

    Collection<V> values();

    boolean isEmpty();
    default boolean isNotEmpty() {
        return !isEmpty();
    }

    boolean containsKey(K id);
}