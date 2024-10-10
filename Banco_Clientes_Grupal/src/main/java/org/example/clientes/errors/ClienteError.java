package org.example.clientes.errors;

public abstract class ClienteError extends Exception {

    public ClienteError(String message) {
        super(message);
    }

    public static class StorageError extends ClienteError {
        public StorageError(String action, String nombreArchivo) {
            super("Error al " + action + " el archivo: " + nombreArchivo);
        }
    }

}
