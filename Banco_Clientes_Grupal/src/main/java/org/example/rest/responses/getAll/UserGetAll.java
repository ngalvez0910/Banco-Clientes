package org.example.rest.responses.getAll;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGetAll {

    @JsonProperty("id")
    private int id;

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

    public int getId() {  return id;  }

    public String getName() {  return name;  }

    public String getUsername() {  return username;  }

    public String getEmail() {  return email; }
}
