package Application.service.ArtistContainer;

import java.util.UUID;

public class WikiInfoObj {

    private String iD;
    private String wikidata;
    private String wikipedia;

    //HTTP code for response
    private Integer wikiDataStatuccode;

    //HTTP code for response
    private Integer wikiPediaStatuscode;

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWikiDataStatuccode() {
        return wikiDataStatuccode;
    }
    public Integer getWikiPediaStatuccode() {
        return wikiPediaStatuscode;
    }

    public void setWikiDataStatuccode(Integer wikiDataStatuccode) {
        this.wikiDataStatuccode = wikiDataStatuccode;
    }

    public void setWikiPediaStatuscode(Integer wikiPediaStatuscode) {
        this.wikiPediaStatuscode = wikiPediaStatuscode;
    }

    public WikiInfoObj(String wikidata, String wikipedia) {
        this.iD = UUID.randomUUID().toString();
        this.wikidata = wikidata;
        this.wikipedia = wikipedia;
    }
    public String getiD() {
        return iD;
    }

    public void setWikipediaSearchTerm(String wikipedia) {
        this.wikipedia = wikipedia;
    }

    public String getWikidata() {
        return wikidata;
    }

    public String getWikipedia() {
        return wikipedia;
    }


    public boolean isEmpty() {
        return  wikidata == null &&
                wikipedia == null &&
                description == null;
    }
}
