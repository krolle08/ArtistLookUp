package Application.model.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public class WikipediaResponse {

    @JsonProperty("extract")
    private String description;

    public String getDescription() {
        return description;
    }

}
