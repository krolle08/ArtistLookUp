package Application.model.response;

public class AlbumResponse {

    private String albumId;
    private String title;
    private String imageURL;

    public AlbumResponse(String albumId, String title, String imageURL){
        this.albumId = albumId;
        this.title = title;
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
