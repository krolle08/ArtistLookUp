package Application.service;

import java.net.URI;
import java.util.List;
import java.util.UUID;

public class ArtistInfo {

    private String iD;
    private String name;
    private String mBID;
    private WikiInfo wikiInfo;

    private String mBStatusCode;
    private List<AlbumInfo> albums;

    public ArtistInfo(String name, String mBID) {
        this(name, mBID, null, null);
    }

    public ArtistInfo(String name, String mBid, WikiInfo wikiInfo, List<AlbumInfo> albums) {
        this.iD = UUID.randomUUID().toString();
        this.name = name;
        this.mBID = mBid;
        this.wikiInfo = wikiInfo;
        this.albums = albums;
    }

    public List<AlbumInfo> getAlbums() {
        return albums;
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
    public void setiD(String iD) {
        this.iD = iD;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setmBID(String mBID) {
        this.mBID = mBID;
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
}
