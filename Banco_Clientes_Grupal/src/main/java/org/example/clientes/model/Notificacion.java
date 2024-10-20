package org.example.clientes.model;

/**
 * Clase genérica que representa una notificación de tipo {@link Tipo}, la cual
 * puede contener cualquier tipo de información como contenido.
 *
 * @param <T> El tipo de contenido que contiene la notificación.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public class Notificacion<T> {
    private Tipo tipo;
    private T contenido;

    /**
     * Constructor que inicializa una notificación con el tipo de notificación y el contenido proporcionados.
     *
     * @param tipo El tipo de notificación, ya sea NEW, UPDATED o DELETED.
     * @param contenido El contenido de la notificación.
     */
    public Notificacion(Tipo tipo, T contenido) {
        this.tipo = tipo;
        this.contenido = contenido;
    }

    /**
     * Obtiene el tipo de la notificación.
     *
     * @return El tipo de la notificación.
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de la notificación.
     *
     * @param tipo El tipo que se desea asignar a la notificación.
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el contenido de la notificación.
     *
     * @return El contenido de la notificación.
     */
    public T getContenido() {
        return contenido;
    }


    /**
     * Establece el contenido de la notificación.
     *
     * @param contenido El contenido que se desea asignar a la notificación.
     */
    public void setContenido(T contenido) {
        this.contenido = contenido;
    }

    /**
     * Devuelve una representación en cadena de la notificación.
     *
     * @return Una cadena con el tipo de la notificación y su contenido.
     */
    @Override
    public String toString() {
        return "Notificacion{" +
                "tipo=" + tipo +
                ", contenido=" + contenido +
                '}';
    }

    /**
     * Enumeración que define los posibles tipos de notificación.
     * Puede ser NEW, UPDATED o DELETED.
     */
    public enum Tipo {
        NEW, UPDATED, DELETED
    }
}