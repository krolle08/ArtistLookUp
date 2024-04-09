package Application.service.Wikidata;

import Application.api.WikidataSearchRoute;
import Application.service.Artist.WikiInfoObj;
import Application.utils.RestTempUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
public class WikidataService {
    private static final Logger logger = LoggerFactory.getLogger(WikidataService.class.getName());

    @Autowired
    WikidataSearchRoute wikidataSearchRoute;

    public void getWikidata(WikiInfoObj wikiInfoObj) throws URISyntaxException, JsonProcessingException {
        ResponseEntity<String> response = wikidataSearchRoute.doGetResponse(wikiInfoObj.getWikidataSearchTerm());
        extractWikiPediaData(response, wikiInfoObj);
    }

    public void extractWikiPediaData(ResponseEntity response, WikiInfoObj wikiInfoObj) throws JsonProcessingException {
        wikiInfoObj.setWikiDataStatuccode(response.getStatusCodeValue());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.getBody().toString());
        String wikipediaSearchTerm;
        Optional<JsonNode> entitiesNode = Optional.ofNullable(rootNode.path("entities"));
        Optional<JsonNode> wikiNode = entitiesNode.flatMap(node -> Optional.ofNullable(node.get(wikiInfoObj.getWikidataSearchTerm().toUpperCase())));
        Optional<JsonNode> sitelinksNode = wikiNode.flatMap(node -> Optional.ofNullable(node.get("sitelinks")));
        Optional<JsonNode> enwikiNode = sitelinksNode.flatMap(node -> Optional.ofNullable(node.get("enwiki")));
        if (enwikiNode.isPresent() && enwikiNode.get().has("title")) {
            wikipediaSearchTerm = enwikiNode.get().get("title").asText();
        } else{
            logger.info("No word for searching on wikipedia was found in the wikidata response for:" + wikiInfoObj.getWikidataSearchTerm());
            return;
        }
        wikiInfoObj.setWikipediaSearchTerm(RestTempUtil.encodeString(wikipediaSearchTerm));
    }
}
