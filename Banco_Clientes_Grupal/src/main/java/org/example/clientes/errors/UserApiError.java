package org.example.clientes.errors;

public class UserApiError {
    private final String message;
    private final int code;

    public UserApiError(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public static class UserApiNotCreated extends UserApiError {
        public UserApiNotCreated(long id) {
            super("No se pudo crear el usuario con id: " + id, 404);
        }
    }

    public static class UserNotFoundApiError extends UserApiError {
        public UserNotFoundApiError(long id) {
            super("Usuario no encontrado con id: " + id, 404);
        }
    }

    public static class UserApiNoUsersFound extends UserApiError {
        public UserApiNoUsersFound() {
            super("No se encontró ningún usuario", 404);
        }
    }

    public static class UserApiNotUpdatedError extends UserApiError {
        public UserApiNotUpdatedError(long id) {
            super("Usuario no actualizado con id: " + id, 404);
        }
    }

    public static class UserApiNotDeletedError extends UserApiError {
        public UserApiNotDeletedError(long id) {
            super("Usuario no eliminado con id: " + id, 404);
        }
    }
}
