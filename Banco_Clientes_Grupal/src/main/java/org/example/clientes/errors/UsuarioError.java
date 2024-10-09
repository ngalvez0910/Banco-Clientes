package org.example.clientes.errors;

import org.example.clientes.model.Usuario;

public abstract class UsuarioError extends Exception{
    public UsuarioError(String message) {super(message);}

    public static class NombreInvalido extends UsuarioError {
        public NombreInvalido(String nombre) {
            super("El nombre '" + nombre + "' no es válido. Debe tener entre 4 y 80 caracteres y no debe contener números ni caracteres especiales.");
        }
    }

    public static class UserNameInvalido extends UsuarioError {
        public UserNameInvalido(String userName) {
            super("El nombre de usuario '" + userName + "' no es válido. Debe tener entre 4 y 30 caracteres.");
        }
    }

    public static class EmailInvalido extends UsuarioError {
        public EmailInvalido(String email) {
            super("El correo electrónico '" + email + "' no es válido.");
        }
    }
}
