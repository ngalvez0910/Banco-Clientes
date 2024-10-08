package org.example.rest.responses.getById;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGetById {

    @JsonProperty("data")
    private UserGetById data;

    public UserGetById getData(){ return data; }

}
