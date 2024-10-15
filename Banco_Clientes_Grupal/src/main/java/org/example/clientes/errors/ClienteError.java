package org.example.clientes.errors;

/**
 * Clase para representar errores relacionados con la gestión de clientes.
 * Proporciona mensajes de error y códigos de estado HTTP para diferentes situaciones.
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 */
public class ClienteError {
    private final String message;
    private final int code;

    /**
     * Constructor para crear una instancia de ClienteError.
     *
     * @param message Mensaje descriptivo del error
     * @param code    Código de estado HTTP asociado al error
     */
    public ClienteError(String message, int code) {
        this.message = message;
        this.code = code;
    }

    /**
     * Obtiene el mensaje del error.
     *
     * @return Mensaje del error
     */
    public String getMessage() {
        return message;
    }

    /**
     * Obtiene el código de estado HTTP del error.
     *
     * @return Código de estado HTTP
     */
    public int getCode() {
        return code;
    }

    /**
     * Clase interna para representar el error cuando un cliente no es encontrado.
     */
    public static class ClienteNotFound extends ClienteError {
        public ClienteNotFound() {
            super("No se pudo encontrar ningún cliente: ", 404);
        }
    }

    /**
     * Clase interna para representar el error cuando un cliente no es creado.
     */
    public static class ClienteNotCreated extends ClienteError {
        public ClienteNotCreated() {
            super("No se pudo crear el cliente: ", 400);
        }
    }

    /**
     * Clase interna para representar el error cuando un cliente no es actualizado.
     */
    public static class ClienteNotUpdated extends ClienteError {
        public ClienteNotUpdated() {
            super("No se pudo actualizar el cliente: ", 400);
        }
    }

    /**
     * Clase interna para representar el error cuando un cliente no es borrado.
     */
    public static class ClienteNotDeleted extends ClienteError {
        public ClienteNotDeleted() {
            super("No se pudo borrar el cliente: ", 400);
        }
    }
}
