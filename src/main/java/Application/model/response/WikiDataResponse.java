package Application.model.response;


import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Map;

public class WikiDataResponse {

    private String wikipediaId;
    public WikiDataResponse() {
    }

    @JsonSetter("entities")
    public void getWikipediaId(Map<String, Object> entities) {
        if (entities != null) {
            for (Map.Entry<String, Object> outerEntry : entities.entrySet()) {
                Object outerValue = outerEntry.getValue();
                Map<String, Object> innerMap = (Map<String, Object>) outerValue;
                Map<String, Object> sitelinksMap = (Map<String, Object>) innerMap.get("sitelinks");

                if (sitelinksMap != null) {
                    Map<String, Object> enwiki = (Map<String, Object>) sitelinksMap.get("enwiki");
                    if (enwiki != null) {
                        this.wikipediaId = enwiki.get("title").toString();
                    }
                }
            }
        }
    }

    public String getWikipediaId() {
        return wikipediaId;
    }
}