package Application.api;

import Application.service.WikiInfo;
import Application.utils.RestTemp;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;


@RestController
public class WikipediaSearchRoute {
    private Log log = LogFactory.getLog(MusicBrainzIDSearchRoute.class);
    private final String protocol = "https";
    private final String schemeDelimiter = "://";
    private final String host = "en.wikipedia.org";
    private final String pathPrefix = "/w";
    private final String api = "/api.php";
    private final String postPreFix = "?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles=";

    public void wikipediaService(WikiInfo wikiInfo) throws URISyntaxException {
        URI uri = buildWikiPediaUri(wikiInfo.getWikipedia());
        ResponseEntity<String> response = getResponse(uri);

        wikiInfo.setWikiPediaStatuscode(String.valueOf(response.getStatusCodeValue()));
        wikiInfo.setDescription(extractDescription(response));
        if(wikiInfo == null || wikiInfo.getDescription().isEmpty()){
            log.warn("No description was found for: " + wikiInfo.getWikipedia());
        }
    }

    private URI buildWikiPediaUri(String searchTerm) throws URISyntaxException {
       String fullpath = RestTemp.constructUri(searchTerm,  protocol,  schemeDelimiter,  host,
                pathPrefix,  api,  postPreFix);
       return new URI(fullpath);
    }

    private ResponseEntity<String> getResponse(URI uri) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        if (response.getBody() == null || response.getBody().isEmpty()) {
            log.warn("No results on provided uri: " + uri);
        }
        return response;
    }

    private String extractDescription(ResponseEntity response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));
            JsonNode extractNode = rootNode.path("query").path("pages").elements().next().get("extract");
            if (extractNode != null && !extractNode.isNull()) {
                return extractNode.asText();
            }
        } catch (Exception e) {
            log.error("Error occurred while extracting description from Wikipedia response", e);
            e.printStackTrace();
        }
        return null;
    }
}
