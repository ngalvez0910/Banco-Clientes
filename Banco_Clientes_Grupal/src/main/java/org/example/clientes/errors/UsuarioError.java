package org.example.clientes.errors;

/**
 * Clase abstracta para representar errores relacionados con la gestión de usuarios.
 * Extiende la clase Exception para proporcionar un manejo de errores específico.
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 */
public abstract class UsuarioError extends Exception {

    /**
     * Constructor para crear una instancia de UsuarioError.
     *
     * @param message Mensaje descriptivo del error
     */
    public UsuarioError(String message) {
        super(message);
    }

    /**
     * Clase interna para representar un error cuando el usuario es inválido.
     */
    public static class UsuarioInvaido extends UsuarioError {
        /**
         * Constructor para crear una instancia de UsuarioInvaido.
         *
         * @param mensaje Mensaje descriptivo del error
         */
        public UsuarioInvaido(String mensaje) {
            super(mensaje);
        }
    }

    /**
     * Clase interna para representar un error cuando el nombre es inválido.
     */
    public static class NombreInvalido extends UsuarioError {
        /**
         * Constructor para crear una instancia de NombreInvalido.
         *
         * @param nombre Nombre que no es válido
         */
        public NombreInvalido(String nombre) {
            super("El nombre '" + nombre + "' no es válido. Debe tener entre 4 y 80 caracteres y no debe contener números ni caracteres especiales.");
        }
    }

    /**
     * Clase interna para representar un error cuando el nombre de usuario es inválido.
     */
    public static class UserNameInvalido extends UsuarioError {
        /**
         * Constructor para crear una instancia de UserNameInvalido.
         *
         * @param userName Nombre de usuario que no es válido
         */
        public UserNameInvalido(String userName) {
            super("El nombre de usuario '" + userName + "' no es válido. Debe tener entre 4 y 30 caracteres.");
        }
    }

    /**
     * Clase interna para representar un error cuando el correo electrónico es inválido.
     */
    public static class EmailInvalido extends UsuarioError {
        /**
         * Constructor para crear una instancia de EmailInvalido.
         *
         * @param email Correo electrónico que no es válido
         */
        public EmailInvalido(String email) {
            super("El correo electrónico '" + email + "' no es válido.");
        }
    }

    /**
     * Clase interna para representar errores relacionados con el almacenamiento de usuarios.
     */
    public static class StorageError extends UsuarioError {
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
