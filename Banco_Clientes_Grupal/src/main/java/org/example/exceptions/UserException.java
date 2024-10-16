package org.example.exceptions;

/**
 * Clase abstracta para manejar excepciones relacionadas con usuarios.
 * Extiende de RuntimeException, lo que permite que estas excepciones no sean comprobadas (unchecked exceptions).
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
abstract class UserException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje de error específico.
     *
     * @param message Mensaje de error que será mostrado cuando ocurra la excepción.
     */
    public UserException(String message) {
        super(message);
    }
}
