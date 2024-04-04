package Application.service;

import Application.service.Area.AreaInfoObj;
import Application.service.Artist.ArtistInfoObj;

import java.util.UUID;

public class MusicEntityObj {
    private String iD = UUID.randomUUID().toString();
    private ArtistInfoObj artistInfoObj;
    private AreaInfoObj areaInfoObj;

    public void setAreaInfoObj(AreaInfoObj areaInfoObj) {
        this.areaInfoObj = areaInfoObj;
    }

    public MusicEntityObj() {
        this.iD = UUID.randomUUID().toString();
    }

    public ArtistInfoObj getArtistInfoObj() {
        return artistInfoObj;
    }

    public void setArtistInfoObj(ArtistInfoObj artistInfoObj) {
        this.artistInfoObj = artistInfoObj;
    }

    public String getiD() {
        return iD;
    }
}
