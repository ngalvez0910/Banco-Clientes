package org.example.rest.responses.createUpdateDelete;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDelete {

    @JsonIgnore
    @JsonProperty("id")
    private int id;

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
