package org.example.rest.responses.createUpdateDelete;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Clase UserDelete
 *
 * Esta clase representa la respuesta de eliminación de un usuario en la API REST.
 * Aunque incluye varios campos como ID, nombre, nombre de usuario, correo electrónico,
 * dirección, teléfono, sitio web y compañía, todos ellos están anotados con @JsonIgnore,
 * lo que significa que no serán incluidos en la serialización o deserialización
 * cuando se trabaje con esta clase en operaciones de eliminación.
 *
 * La clase utiliza las anotaciones de Lombok @Builder, @AllArgsConstructor y @NoArgsConstructor
 * para facilitar la creación y manejo de instancias de esta clase, permitiendo el uso de patrones
 * de diseño como el constructor completo o vacío.
 *
 * Esta clase es útil en el contexto de la API cuando se realiza una operación de eliminación
 * de un usuario, pero los detalles del mismo no son necesarios en la respuesta.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDelete {

    @JsonIgnore
    @JsonProperty("id")
    private long id;

    @JsonIgnore
    @JsonProperty("name")
    private String name;

    @JsonIgnore
    @JsonProperty("username")
    private String username;

    @JsonIgnore
    @JsonProperty("email")
    private String email;

    @JsonIgnore
    @JsonProperty("address")
    private String address;

    @JsonIgnore
    @JsonProperty("phone")
    private String phone;

    @JsonIgnore
    @JsonProperty("website")
    private String website;

    @JsonIgnore
    @JsonProperty("company")
    private String company;


}
