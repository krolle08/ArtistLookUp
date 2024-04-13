package Application.service.Wiki;

import Application.api.WikidataSearchRoute;
import Application.service.Artist.WikiInfoObj;
import Application.utils.LoggingUtility;
import Application.utils.RestTempUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WikiDataService {

    @Autowired
    WikidataSearchRoute wikidataSearchRoute;

    public void getWikidata(WikiInfoObj wikiInfoObj){
        ResponseEntity<String> response = wikidataSearchRoute.doGetResponse(wikiInfoObj.getWikidataSearchTerm());
        extractWikiPediaData(response, wikiInfoObj);
    }

    public void extractWikiPediaData(ResponseEntity response, WikiInfoObj wikiInfoObj) {
        wikiInfoObj.setWikiDataStatuccode(response.getStatusCodeValue());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(response.getBody().toString());
        } catch (JsonProcessingException e) {
            LoggingUtility.error("Error occurred when mapping the response in WikiAndCoverArtExecutorService.extractWikidata.32. " +
                    e.getMessage());
            e.printStackTrace();
            return;
        }
        String wikipediaSearchTerm;
        Optional<JsonNode> entitiesNode = Optional.ofNullable(rootNode.path("entities"));
        Optional<JsonNode> wikiNode = entitiesNode.flatMap(node -> Optional.ofNullable(node.get(wikiInfoObj.getWikidataSearchTerm().toUpperCase())));
        Optional<JsonNode> sitelinksNode = wikiNode.flatMap(node -> Optional.ofNullable(node.get("sitelinks")));
        Optional<JsonNode> enwikiNode = sitelinksNode.flatMap(node -> Optional.ofNullable(node.get("enwiki")));
        if (enwikiNode.isPresent() && enwikiNode.get().has("title")) {
            wikipediaSearchTerm = enwikiNode.get().get("title").asText();
        } else {
            LoggingUtility.info("No word for searching on wikipedia was found in the wikidata response for:" + wikiInfoObj.getWikidataSearchTerm());
            return;
        }
        wikiInfoObj.setWikipediaSearchTerm(RestTempUtil.encodeIfNeeded(wikipediaSearchTerm));
    }
}
