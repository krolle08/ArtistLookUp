package Application.service;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MusicEntity {
    private String iD;
    private ArtistInfo artistInfo;

    public MusicEntity(WikiInfo wikiInfo, ArtistInfo artistInfo) {
        this.iD = UUID.randomUUID().toString();
        this.artistInfo = artistInfo;
    }

    public MusicEntity() {
        this.iD = UUID.randomUUID().toString();
    }

    public ArtistInfo getArtistInfo() {
        return artistInfo;
    }

    public void setArtistInfo(ArtistInfo artistInfo) {
        this.artistInfo = artistInfo;
    }

    public String getiD() {
        return iD;
    }
}
