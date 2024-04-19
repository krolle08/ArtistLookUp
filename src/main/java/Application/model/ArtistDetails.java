package Application.model;

import Application.model.response.AlbumResponse;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.List;
public class ArtistDetails {

    @JsonProperty("mbid")
    private String mbid;
    @JsonProperty("description")
    private String description;
    @JsonProperty("albums")
    private List<AlbumResponse> albums;

    public ArtistDetails(String mbid, String description, List<AlbumResponse> albums) {
        this.mbid = mbid;
        this.description = description;
        this.albums = albums;
    }


}
