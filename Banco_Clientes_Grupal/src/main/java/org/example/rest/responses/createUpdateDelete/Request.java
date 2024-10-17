package org.example.rest.responses.createUpdateDelete;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Clase Request
 *
 * Esta clase representa una solicitud que se envía en las operaciones de creación,
 * actualización o eliminación de usuarios en la API REST. Contiene los campos
 * necesarios para representar los datos de un usuario: nombre, nombre de usuario
 * y correo electrónico.
 *
 * La clase utiliza la anotación @JsonProperty de Jackson para mapear los campos
 * de JSON con los atributos de la clase y @Builder de Lombok para facilitar la
 * construcción de objetos de tipo Request.
 *
 * Métodos accesores están disponibles para recuperar los valores de cada campo.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
@Builder
public class Request {

    @JsonProperty("name")
    private String name;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    /**
     * Devuelve el nombre del usuario.
     *
     * @return El nombre del usuario.
     */
    public String getName() { return name; }

    /**
     * Devuelve el nombre de usuario.
     *
     * @return El nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Devuelve el correo electrónico del usuario.
     *
     * @return El correo electrónico del usuario.
     */
    public String getEmail() {
        return email;
    }
}
