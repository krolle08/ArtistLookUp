package Application.model;

import java.util.UUID;

public class Wikipedia {

    private final String iD;
    private final String wikidataSearchTerm;
    private String wikipediaSearchTerm;

    //HTTP code for handling the response
    private Integer wikiDataStatuccode;

    //HTTP code for handling the response
    private Integer wikiPediaStatuscode;

    private String description = "";

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

    public Wikipedia(String wikidataSearchTerm, String wikipediaSearchTerm) {
        this.iD = UUID.randomUUID().toString();
        this.wikidataSearchTerm = wikidataSearchTerm;
        this.wikipediaSearchTerm = wikipediaSearchTerm;
    }
    public String getiD() {
        return iD;
    }

    public void setWikipediaSearchTerm(String wikipediaSearchTerm) {
        this.wikipediaSearchTerm = wikipediaSearchTerm;
    }

    public String getWikidataSearchTerm() {
        return wikidataSearchTerm;
    }

    public String getWikipediaSearchTerm() {
        return wikipediaSearchTerm;
    }
}
