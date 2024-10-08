package org.example.errors;

public class UserNoUsersFound extends UserError {
    public UserNoUsersFound() {
        super("No se encontró ningún usuario", 404);
    }
}