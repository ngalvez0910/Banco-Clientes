package rest.responses.getAll;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ResponseGetAll {

    @JsonProperty("data")
    private List<UserGetAll> data;

    public List<UserGetAll> getData() {
        return data;
    }

}
