package Application.model.response;

import Application.model.Album;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.ArrayList;
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

    @JsonSetter("release-groups")
    public void setAlbums(List<Map<String, Object>> albums) {
        if (albums != null) {
            this.albums = parseAlbums(albums);
        }
    }

    public List<Album> parseAlbums(List<Map<String, Object>> albums) {
        List<Album> parsedAlbums = new ArrayList<>();
        for (Map<String, Object> albumData : albums) {
            if (albumData.get("primary-type").equals("Album")) {
                String albumTitle = (String) albumData.get("title");
                String albumId = (String) albumData.get("id");
                Album album = new Album(albumTitle, albumId);
                parsedAlbums.add(album);
            }
        }
        return parsedAlbums;
    }

    @JsonSetter("relations")
    public void setWikidataId(List<Map<String, Object>> relations) {
        for (Map<String, Object> node : relations) {
            if (node.get("type") != null && node.get("type").equals("wikidata")) {
                parseWikidataIdFromNode(node);
                break;
            }
        }
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
