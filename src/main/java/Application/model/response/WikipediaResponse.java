package Application.model.response;


import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Map;

public class WikipediaResponse {

    private String description;

    @JsonSetter("query")
    public void setDescription(Map<String, Object> query) {
        if (query != null && query.containsKey("pages")) {
            Map<String, Object> pages = (Map<String, Object>) query.get("pages");
            for (Map.Entry<String, Object> outerEntry : pages.entrySet()) {
                Object outerValue = outerEntry.getValue();
                Map<String, Object> innerMap = (Map<String, Object>) outerValue;
                if (innerMap != null && innerMap.containsKey("extract")) { // Assuming page id is always "21231"
                    this.description = innerMap.get("extract").toString();
                }
            }
        }
    }

public String getDescription() {
    return description;
}
}
