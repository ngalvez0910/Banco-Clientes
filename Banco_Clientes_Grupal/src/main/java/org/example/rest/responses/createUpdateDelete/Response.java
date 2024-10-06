package org.example.rest.responses.createUpdateDelete;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("updatedAt")
    private String updatedAt;

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    public String getCreatedAt() {
        return createdAt;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }
    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public String getUserName() {
        return username;
    }
    public String getEmail() {
        return email;
    }


}
