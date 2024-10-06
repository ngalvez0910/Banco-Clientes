package org.example.errors;

public class UserNotFoundError extends UserError {
    public UserNotFoundError(long id) {
        super("Usuario no encontrado con id:" + id, 404);
    }
}
