package Application.model.response;

import Application.model.Album;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;


public class ArtistResponse {

    @NotBlank
    @JsonProperty("sort-name")
    private String name;

    @NotBlank
    @JsonProperty("id")
    private String mBID;

    private List<Album> albums;

    private String wikidataId;

    public ArtistResponse() {
    }
    public void setAlbums(List<Album> albums) {
        if (albums != null) {
            this.albums = albums;
        }
    }
    public void setWikidataId(String wikidataId) {
        this.wikidataId = wikidataId;
    }

    private void parseWikidataIdFromNode(Map<String, Object> node) {
        if (node != null) {
            Object urlMapObject = node.get("url");
            if (urlMapObject instanceof Map) {
                Map<String, Object> urlMap = (Map<String, Object>) node.get("url");
                if (urlMap != null && urlMap.containsKey("resource")) {
                    String resource = (String) urlMap.get("resource");
                    if (resource != null) {
                        this.wikidataId = resource.substring(resource.indexOf("Q"));
                    }
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getmBID() {
        return mBID;
    }

    public void setmBID(String mBID) {
        this.mBID = mBID;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public String getWikidataId() {
        return wikidataId;
    }
}
