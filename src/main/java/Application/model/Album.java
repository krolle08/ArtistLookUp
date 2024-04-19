package Application.model;

public class Album {

    private String albumId;
    private String title;
    public Album(String albumId, String title){
        this.albumId = albumId;
        this.title = title;
    }
    public String getAlbumId() {
        return albumId;
    }

    public String getTitle() {
        return title;
    }


}
