package Application.service.Artist;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArtistInfoObj {

    private String iD;
    private String name;
    private String mBID;
    private WikiInfoObj wikiInfoObj;

    private Integer mBStatusCode;
    private List<AlbumInfoObj> albums = new ArrayList<>();

    public ArtistInfoObj(String name, String mBID) {
        this(name, mBID, null, null);
    }

    public ArtistInfoObj(String name, String mBid, WikiInfoObj wikiInfoObj, List<AlbumInfoObj> albums) {
        this.iD = UUID.randomUUID().toString();
        this.name = name;
        this.mBID = mBid;
        this.wikiInfoObj = wikiInfoObj;
        this.albums = albums;
    }

    public List<AlbumInfoObj> getAlbums() {
        return albums;
    }

    public ArtistInfoObj(){
    }

    public void setmBStatusCode(Integer mBStatusCode) {
        this.mBStatusCode = mBStatusCode;
    }

    public void setAlbums(List<AlbumInfoObj> albums) {
        this.albums = albums;
    }

    public WikiInfoObj getWikiInfo() {
        return wikiInfoObj;
    }

    public Integer getmBStatusCode() {
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

    public void setWikiInfo(WikiInfoObj wikiInfoObj) {
        this.wikiInfoObj = wikiInfoObj;
    }


    public ArtistInfoObj(String id) {
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

    public boolean isEmpty() {
        return iD == null &&
                (name == null || name.isEmpty()) &&
                (mBID == null || mBID.isEmpty()) &&
                wikiInfoObj == null &&
                (albums == null || albums.isEmpty()) &&
                mBStatusCode == null;
    }
}
