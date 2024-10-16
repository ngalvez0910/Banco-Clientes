package org.example.exceptions;

/**
 * Excepción que se lanza cuando un usuario no es encontrado.
 * Hereda de UserException, lo que indica que es un tipo específico de error relacionado con usuarios.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public class UserNotFoundException extends UserException {

    /**
     * Constructor que recibe un mensaje de error específico.
     *
     * @param message Mensaje de error que será mostrado cuando ocurra la excepción de usuario no encontrado.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
