package org.example.clientes.errors;

public class UsuarioError {
    public static class NombreInvalido extends Exception {
        public NombreInvalido(String nombre) {
            super("El nombre '" + nombre + "' no es válido. Debe tener entre 4 y 80 caracteres y no debe contener números ni caracteres especiales.");
        }
    }

    public static class UserNameInvalido extends Exception {
        public UserNameInvalido(String userName) {
            super("El nombre de usuario '" + userName + "' no es válido. Debe tener entre 4 y 30 caracteres.");
        }
    }

    public static class EmailInvalido extends Exception {
        public EmailInvalido(String email) {
            super("El correo electrónico '" + email + "' no es válido.");
        }
    }
}
