package Application.service.Artist;

import java.util.UUID;

public class AlbumInfoObj {

    private String iD;
    private String albumId;
    private String title;
    private String imageURL;

    public AlbumInfoObj(String albumId, String title){
        this.iD = UUID.randomUUID().toString();
        this.albumId = albumId;
        this.title = title;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getiD() {
        return iD;
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
