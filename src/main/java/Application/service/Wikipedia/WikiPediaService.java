package Application.service.Wikipedia;

import Application.api.WikipediaSearchRoute;
import Application.service.Artist.SearchArtistService;
import Application.service.Artist.WikiInfoObj;
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

@Service
public class WikiPediaService {
    private static final Logger logger = LoggerFactory.getLogger(WikiPediaService.class.getName());

    @Autowired
    WikipediaSearchRoute wikipediaSearchRoute;

    public void getWikidataForArtist(WikiInfoObj wikiInfoObj) throws URISyntaxException, JsonProcessingException {
        URI uri = wikipediaSearchRoute.getUrl(wikiInfoObj.getWikipediaSearchTerm());
        ResponseEntity<String> response = wikipediaSearchRoute.getResponse(uri);
        if (response.getBody() == null || response.getBody().isEmpty()) {
            logger.warn("No results on provided uri: " + uri);
        } else {
            wikiInfoObj.setDescription(extractDescription(response));
        }
        wikiInfoObj.setWikiPediaStatuscode(response.getStatusCodeValue());
    }

    private String extractDescription(ResponseEntity response) throws JsonProcessingException {
        String description = "";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));
        JsonNode extractNode = rootNode.path("query").path("pages").elements().next().get("extract");
        if (extractNode != null && !extractNode.isNull()) {
            description = extractNode.asText();
        }
        return description;
    }
}
