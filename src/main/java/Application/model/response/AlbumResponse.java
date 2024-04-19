package Application.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlbumResponse {
    @JsonProperty("title")
    private String title;
    @JsonProperty("id")
    private String albumId;
    @JsonProperty("image")
    private String imageURL;

    public AlbumResponse(String title, String albumId, String imageURL) {
        this.title = title;
        this.albumId = albumId;
        this.imageURL = imageURL;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }
}
