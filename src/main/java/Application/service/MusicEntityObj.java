package Application.service;

import Application.service.ArtistContainer.ArtistInfoObj;
import Application.service.ArtistContainer.WikiInfoObj;

import java.util.UUID;

public class MusicEntityObj {
    private String iD;
    private ArtistInfoObj artistInfoObj;

    public MusicEntityObj(WikiInfoObj wikiInfoObj, ArtistInfoObj artistInfoObj) {
        this.iD = UUID.randomUUID().toString();
        this.artistInfoObj = artistInfoObj;
    }

    public MusicEntityObj() {
        this.iD = UUID.randomUUID().toString();
    }

    public ArtistInfoObj getArtistInfo() {
        return artistInfoObj;
    }

    public void setArtistInfo(ArtistInfoObj artistInfoObj) {
        this.artistInfoObj = artistInfoObj;
    }

    public String getiD() {
        return iD;
    }
}
