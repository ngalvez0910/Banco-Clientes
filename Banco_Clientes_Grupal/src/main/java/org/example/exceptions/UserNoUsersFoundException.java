package org.example.exceptions;

/**
 * Excepción que se lanza cuando no se encuentran usuarios en el sistema.
 * Extiende de UserException, indicando un error específico relacionado con la ausencia de usuarios.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 */
public class UserNoUsersFoundException extends UserException {

    /**
     * Constructor que recibe un mensaje de error específico.
     *
     * @param message Mensaje de error que se mostrará cuando no se encuentren usuarios.
     */
    public UserNoUsersFoundException(String message) {
        super(message);
    }
}
