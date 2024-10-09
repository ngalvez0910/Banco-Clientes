package org.example.errors;

public class UserNotCreated extends UserError {
    public UserNotCreated(long id) {
        super("No se pudo crear el usuario con id: " + id, 404);
    }
}
