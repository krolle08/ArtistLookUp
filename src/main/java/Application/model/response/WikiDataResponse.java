package Application.model.response;


import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Map;

public class WikiDataResponse {

    private String wikipediaId;

    private Map<String, Object> entities;

    public WikiDataResponse() {
    }
    @JsonSetter("entities")
    public void getWikipediaId() {
        if (entities != null) {
            for (Map.Entry<String, Object> entry : entities.entrySet()) {
                Map<String, Object> node = (Map<String, Object>) entry.getValue();
                if (node.containsKey("sitelinks")) {
                    String wikipediaId = parseWikipediaIdFromNode(node);
                    if (wikipediaId != null) {
                        this.wikipediaId = wikipediaId;
                    }
                }
            }
        }
    }

    private String parseWikipediaIdFromNode(Map<String, Object> node) {
        if (node != null) {
            Map<String, Object> urlMap = (Map<String, Object>) node.get("sitelinks");
            if (urlMap != null && urlMap.containsKey("enwiki")) {
                Map<String, Object> enwikiMap = (Map<String, Object>) urlMap.get("enwiki");
                if (enwikiMap != null && enwikiMap.containsKey("resource")) {
                    String resource = (String) enwikiMap.get("resource");
                    if (resource != null) {
                        return resource.substring(resource.indexOf("Q"));
                    }
                }
            }
        }
        return null;
    }
    public String getId(){
        return wikipediaId;
    }
}




/*


    @JsonSetter("entities")
    public void setWikidataId(List<Map<String, Object>> entities) {
        for (Map<String, Object> node : entities) {
            if (node.get("sitelinks") != null) {
                parseWikiPediaIdFromNode(node);
                break;
            }
        }
    }

    private void parseWikiPediaIdFromNode(Map<String, Object> node) {
        if (node != null) {
            Object urlMapObject = node.get("enwiki");
            if (urlMapObject instanceof Map) {
                Map<String, Object> urlMap = (Map<String, Object>) node.get("enwiki");
                if (urlMap != null && urlMap.containsKey("resource")) {
                    String resource = (String) urlMap.get("resource");
                    if (resource != null) {
                        this.wikipediaId = resource.substring(resource.indexOf("Q"));
                    }
                }
            }
        }
    }
}
 */
