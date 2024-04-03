package Application.api;

import Application.YourApplication;
import Application.service.ArtistContainer.WikiInfoObj;
import Application.utils.RestTempUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

/**
 * Wikipedia documentation:
 * There is no hard speed limit on read requests, but be considerate and try not to take a site down. Most system
 * administrators reserve the right to unceremoniously block you if you do endanger the stability of their site.
 * Making your requests in series rather than in parallel, by waiting for one request to finish before sending a new
 * request, should result in a safe request rate (https://www.mediawiki.org/wiki/API:Etiquette)
 */
@RestController
public class WikipediaSearchRoute {
    private static final Logger logger = Logger.getLogger(YourApplication.class.getName());
    private final String protocol = "https";
    private final String schemeDelimiter = "://";
    private final String host = "en.wikipedia.org";
    private final String pathPrefix = "/w";
    private final String api = "/api.php";
    private final String postPreFix = "?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles=";

    public void wikipediaService(WikiInfoObj wikiInfoObj) throws URISyntaxException, JsonProcessingException {
        URI uri = buildWikiPediaUri(wikiInfoObj.getWikipedia());
        ResponseEntity<String> response = getResponse(uri);

        wikiInfoObj.setWikiPediaStatuscode(response.getStatusCodeValue());
        wikiInfoObj.setDescription(extractDescription(response));
        if(wikiInfoObj.getDescription() == null || wikiInfoObj.getDescription().isEmpty()){
            logger.warning("No description was found for: " + wikiInfoObj.getWikipedia());
        }
    }

    private URI buildWikiPediaUri(String searchTerm) throws URISyntaxException {
       String fullpath = RestTempUtil.constructUri(searchTerm,  protocol,  schemeDelimiter,  host,
                pathPrefix,  api,  postPreFix);
       return new URI(fullpath);
    }

    private ResponseEntity<String> getResponse(URI uri) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        if (response.getBody() == null || response.getBody().isEmpty()) {
            logger.warning("No results on provided uri: " + uri);
        }
        return response;
    }

    private String extractDescription(ResponseEntity response) throws JsonProcessingException {
        String description = "";
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));
            JsonNode extractNode = rootNode.path("query").path("pages").elements().next().get("extract");
            if (extractNode != null && !extractNode.isNull()) {
                description = extractNode.asText();}
        return description;
    }
}
