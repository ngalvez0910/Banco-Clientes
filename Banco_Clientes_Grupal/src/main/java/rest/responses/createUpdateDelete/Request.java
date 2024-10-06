package rest.responses.createUpdateDelete;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class Request {

    @JsonProperty("name")
    private String name;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    public String getName() { return name; }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
