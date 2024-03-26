package Application.service;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MusicEntity {
    private String iD;
    private WikiInfo wikiInfo;
    private ArtistInfo artistInfo;
    private Map<String, AlbumInfo> albums;

    public MusicEntity(WikiInfo wikiInfo, ArtistInfo artistInfo) {
        this.iD = UUID.randomUUID().toString();
        this.wikiInfo = wikiInfo;
        this.artistInfo = artistInfo;
        this.albums = new HashMap<>();
    }

    public MusicEntity() {
        this.iD = UUID.randomUUID().toString();
        this.albums = new HashMap<>();
    }

    public void setAlbum(String albumId, String title, String imageUrl) {
        AlbumInfo newAlbum = new AlbumInfo(albumId, title, imageUrl);
        setAlbum(newAlbum);
    }

    public void setAlbum(AlbumInfo album) {
        albums.put(UUID.randomUUID().toString(), album);
    }

    public WikiInfo getWikiInfo() {
        return wikiInfo;
    }

    public void setWikiInfo(String wikidata, String wikipedia) {
        WikiInfo newWikiInfo = new WikiInfo(wikidata, wikipedia);
        this.wikiInfo = newWikiInfo;
    }

    public void setWikiInfo(WikiInfo wikiInfo) {
        this.wikiInfo = wikiInfo;
    }

    public ArtistInfo getArtistInfo() {
        return artistInfo;
    }

    public void setArtistInfo(ArtistInfo artistInfo) {
        this.artistInfo = artistInfo;
    }

    public void setArtistInfo(String name, String mBID, URI url) {
        this.artistInfo = new ArtistInfo(name, mBID, url);
    }

    public Map<String, AlbumInfo> getAlbums() {
        return albums;
    }
    public void setAlbums(Map<String, AlbumInfo> albums) {
        for (Map.Entry<String, AlbumInfo> album : albums.entrySet()) {
            if (album instanceof AlbumInfo) {
                setAlbum((AlbumInfo) album);
            }
        }
    }
    public String getiD() {
        return iD;
    }
}
