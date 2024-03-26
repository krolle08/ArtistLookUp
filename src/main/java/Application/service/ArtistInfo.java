package Application.service;

import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ArtistInfo {

    private String iD;
    private String name;
    private String mBID;
    private URI uri;
    private WikiInfo wikiInfo;

    private String mBStatusCode;
    private List<AlbumInfo> albums;

    public ArtistInfo(String name, String mBID, URI uri) {
        this.iD = UUID.randomUUID().toString();
        this.name = name;
        this.mBID = mBID;
        this.uri = uri;
    }

    public ArtistInfo(){
    }

    public void setmBStatusCode(String mBStatusCode) {
        this.mBStatusCode = mBStatusCode;
    }

    public void setAlbums(List<AlbumInfo> albums) {
        this.albums = albums;
    }

    public WikiInfo getWikiInfo() {
        return wikiInfo;
    }

    public String getmBStatusCode() {
        return mBStatusCode;
    }

    public Map<String, AlbumInfo> getAlbums() {
        return albums;
    }

    private Map<String, AlbumInfo> albums = new HashMap<>();

    public void setiD(String iD) {
        this.iD = iD;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setmBID(String mBID) {
        this.mBID = mBID;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setWikiInfo(WikiInfo wikiInfo) {
        this.wikiInfo = wikiInfo;
    }


    public ArtistInfo(String id) {
        this.iD = id;
    }

    public String getiD() {
        return iD;
    }

    public String getName() {
        return name;
    }

    public String getmBID() {
        return mBID;
    }

    public URI getUri() {
        return uri;
    }

    public String getWikidata() {
        return wikidata;
    }

    public String getWikipedia() {
        return wikipedia;
    }
}
