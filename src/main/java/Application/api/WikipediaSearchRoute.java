package Application.api;

import Application.service.WikiInfo;
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
        String fullPath = constructUrl(wikiInfo.getWikipedia());
        URI uri = new URI(fullPath);
        ResponseEntity<String> response = getResponse(String.valueOf(uri));

        wikiInfo.setWikiPediaStatuccode(String.valueOf(response.getStatusCodeValue()));
        wikiInfo.setDescription(extractDescription(response));
        if(wikiInfo.getDescription().isEmpty()){
            log.warn("No description was found for: " + wikiInfo.getWikipedia());
        }
    }

    private String constructUrl(String searchTerm){
       return RestTemp.constructUrl(searchTerm,  protocol,  schemeDelimiter,  host,
                pathPrefix,  api,  postPreFix);
    }

    private ResponseEntity getResponse(String uri) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        if (response.getBody().isEmpty()) {
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
                return rootNode.path("query").path("pages").elements().next().get("extract").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
