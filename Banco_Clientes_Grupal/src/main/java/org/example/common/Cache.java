package org.example.common;

/**
 * Interfaz genérica para una caché que permite almacenar, recuperar y eliminar
 * pares clave-valor.
 *
 * @param <K> El tipo de las claves utilizadas en la caché
 * @param <V> El tipo de los valores almacenados en la caché
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 */
public interface Cache<K, V> {

    /**
     * Recupera el valor asociado a la clave especificada.
     *
     * @param key La clave cuya valor asociado se desea obtener
     * @return El valor asociado a la clave, o null si no existe
     */
    V get(K key);

    /**
     * Almacena un valor en la caché asociado a la clave especificada.
     *
     * @param key La clave con la que se asociará el valor
     * @param value El valor a almacenar en la caché
     */
    void put(K key, V value);

    /**
     * Elimina de la caché el valor asociado a la clave especificada.
     *
     * @param key La clave cuyo valor se desea eliminar
     */
    void remove(K key);

    /**
     * Limpia la caché eliminando todos los pares clave-valor.
     */
    void clear();

    /**
     * Devuelve el número de elementos actualmente almacenados en la caché.
     *
     * @return El tamaño de la caché
     */
    int size();

    /**
     * Verifica si la caché contiene un valor asociado a la clave especificada.
     *
     * @param key La clave a verificar
     * @return true si la caché contiene la clave, false en caso contrario
     */
    boolean containsKey(K key);
}
