package Application.service;

import java.util.UUID;

public class WikiInfo {

    private String iD;
    private String wikidata;
    private String wikipedia;

    //HTTP code for response
    private String wikiDataStatuccode;

    //HTTP code for response
    private String wikiPediaStatuccode;

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWikiDataStatuccode() {
        return wikiDataStatuccode;
    }

    public void setWikiDataStatuccode(String wikiDataStatuccode) {
        this.wikiDataStatuccode = wikiDataStatuccode;
    }

    public String getWikiPediaStatuccode() {
        return wikiPediaStatuccode;
    }

    public void setWikiPediaStatuccode(String wikiPediaStatuccode) {
        this.wikiPediaStatuccode = wikiPediaStatuccode;
    }

    public WikiInfo(String wikidata, String wikipedia) {
        this.iD = UUID.randomUUID().toString();
        this.wikidata = wikidata;
        this.wikipedia = wikipedia;
    }

    public String getiD() {
        return iD;
    }

    public void setWikidata(String wikidata) {
        this.wikidata = wikidata;
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
}
