
package org.example.clientes.errors;

import org.example.rest.errors.UserApiError;

public class ClienteError {
    private final String message;
    private final int code;

    public ClienteError(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public static class ClienteNotFound extends ClienteError {
        public ClienteNotFound() {
            super("No se pudo encontrar ningún cliente: ", 404);
        }
    }

    public static class ClienteNotCreated extends ClienteError {
        public ClienteNotCreated() {
            super("No se pudo crear el cliente: ", 400);
        }
    }

    public static class ClienteNotUpdated extends ClienteError {
        public ClienteNotUpdated() {
            super("No se pudo actualizar el cliente: ", 400);
        }
    }

    public static class ClienteNotDeleted extends ClienteError {
        public ClienteNotDeleted() {
            super("No se pudo borrar el cliente: ", 400);
        }
    }
}
