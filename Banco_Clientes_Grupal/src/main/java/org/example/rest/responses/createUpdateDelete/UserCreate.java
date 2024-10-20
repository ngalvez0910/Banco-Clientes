package org.example.rest.responses.createUpdateDelete;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Clase UserCreate
 *
 * Esta clase representa la respuesta de creación de un usuario en la API REST.
 * Contiene los campos principales como ID, nombre, nombre de usuario y correo electrónico,
 * además de otros atributos opcionales como dirección, teléfono, sitio web y compañía,
 * los cuales están anotados con @JsonIgnore para ser ignorados en el proceso de
 * serialización/deserialización.
 *
 * Utiliza las anotaciones @JsonProperty de Jackson para mapear los nombres de los
 * campos JSON con los atributos de la clase y las anotaciones de Lombok @Builder,
 * @AllArgsConstructor y @NoArgsConstructor para facilitar la construcción y manejo
 * de instancias de esta clase.
 *
 * Métodos accesores están disponibles para obtener los valores de los campos principales.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreate {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("username")
    private String username;

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

    public long getId() { return id; }

    public String getName() {  return name;  }

    public String getUsername() {  return username;  }

    public String getEmail() {  return email; }

}
