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
