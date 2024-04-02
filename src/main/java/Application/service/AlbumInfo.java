package Application.service;

import java.util.UUID;

public class AlbumInfo {

    private String iD;
    private String albumId;
    private String title;
    private String imageURL;

    public AlbumInfo(String albumId, String title, String imageURL){
        this.iD = UUID.randomUUID().toString();
        this.albumId = albumId;
        this.title = title;
        this.imageURL = imageURL;
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
