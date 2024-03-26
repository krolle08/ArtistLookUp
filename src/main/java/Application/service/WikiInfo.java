package Application.service;

import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.UUID;

public class WikiInfo {

    private String iD;
    private final String wikidata;
    private final String wikipedia;

    public WikiInfo(String wikidata, String wikipedia) {
        this.iD = UUID.randomUUID().toString();
        this.wikidata = wikidata;
        this.wikipedia = wikipedia;
    }

    public String getiD() {
        return iD;
    }

    public String getWikidata() {
        return wikidata;
    }

    public String getWikipedia() {
        return wikipedia;
    }
}
