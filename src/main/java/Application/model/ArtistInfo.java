package Application.model;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.List;


public class ArtistInfo {


    @NotBlank
    private final String mBId;
    private String name;
    private List<Album> albums;
    public ArtistInfo(String mBId) {
        this.mBId = mBId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public String getmBId() {
        return mBId;
    }
}
