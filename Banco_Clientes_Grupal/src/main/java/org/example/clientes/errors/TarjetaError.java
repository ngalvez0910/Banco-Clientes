package org.example.clientes.errors;

import java.time.LocalDate;

/**
 * Clase abstracta para representar errores relacionados con la gestión de tarjetas.
 * Extiende la clase Exception para proporcionar un manejo de errores más específico.
 */
public abstract class TarjetaError extends Exception {

    /**
     * Constructor para crear una instancia de TarjetaError.
     *
     * @param message Mensaje descriptivo del error
     */
    public TarjetaError(String message) {
        super(message);
    }

    /**
     * Clase interna para representar el error cuando el ID de la tarjeta es inválido.
     */
    public static class TarjetaIdInvalido extends TarjetaError {
        /**
         * Constructor para crear una instancia de TarjetaIdInvalido.
         *
         * @param id ID de la tarjeta que no es válido
         */
        public TarjetaIdInvalido(String id) {
            super("El ID de la tarjeta '" + id + "' no es válido.");
        }
    }

    /**
     * Clase interna para representar el error cuando el número de la tarjeta es inválido.
     */
    public static class TarjetaNumeroInvalido extends TarjetaError {
        /**
         * Constructor para crear una instancia de TarjetaNumeroInvalido.
         *
         * @param numeroTarjeta Número de tarjeta que no es válido
         */
        public TarjetaNumeroInvalido(String numeroTarjeta) {
            super("El número de tarjeta '" + numeroTarjeta + "' no es válido.");
        }
    }

    /**
     * Clase interna para representar el error cuando la fecha de caducidad es inválida.
     */
    public static class FechaCaducidadInvalida extends TarjetaError {
        /**
         * Constructor para crear una instancia de FechaCaducidadInvalida.
         *
         * @param fechaCaducidad Fecha de caducidad que no es válida
         */
        public FechaCaducidadInvalida(LocalDate fechaCaducidad) {
            super("La fecha de caducidad '" + fechaCaducidad + "' no es válida.");
        }
    }

    /**
     * Clase interna para representar el error cuando el nombre del titular es inválido.
     */
    public static class NombreTitularInvalido extends TarjetaError {
        /**
         * Constructor para crear una instancia de NombreTitularInvalido.
         *
         * @param nombreTitular Nombre del titular que no es válido
         */
        public NombreTitularInvalido(String nombreTitular) {
            super("El nombre del titular '" + nombreTitular + "' no es válido.");
        }
    }

    /**
     * Clase interna para representar errores relacionados con el almacenamiento de tarjetas.
     */
    public static class StorageError extends TarjetaError {
        /**
         * Constructor para crear una instancia de StorageError.
         *
         * @param action Acción que se intentaba realizar (por ejemplo, "guardar")
         * @param nombreArchivo Nombre del archivo relacionado con el error
         */
        public StorageError(String action, String nombreArchivo) {
            super("Error al " + action + " el archivo: " + nombreArchivo);
        }
    }
}
