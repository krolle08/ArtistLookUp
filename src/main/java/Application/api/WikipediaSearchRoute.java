package Application.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@RestController
public class WikipediaSearchRoute {
    private Log log = LogFactory.getLog(MusicBrainzIDSearchRoute.class);
    private final String protocol = "https";
    private final String schemeDelimiter = "://";
    private final String host = "en.wikipedia.org";
    private final String pathPrefix = "/w";
    private final String api = "/api.php";
    private final String pathPreFix = "?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles=";

    public Map<String, String> getWikipediadataFromArtist(String searchTerm) throws URISyntaxException {
        if (searchTerm.isEmpty()) {
            log.info("No search term was given:" + searchTerm);
            return null;
        }
        Map<String, String> result = new HashMap<>();
        String fullPath = constructUrl(searchTerm).toString();
        URI uri = new URI(fullPath);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        if (response.getBody().isEmpty()) {
            log.info("No results on provided searchterm");
            return null;
        }
        // Extract HTTP status code
        result.put("wikidatastatusCode", String.valueOf(response.getStatusCodeValue()));
        result.putAll(extractData(response, searchTerm));
        return result;
    }

    private StringBuffer constructUrl(String searchTerm) {
        StringBuffer url = new StringBuffer();
        url.append(protocol).append(schemeDelimiter).append(host);
        if (searchTerm.isEmpty()) {
            log.info("No search term was given for the search:" + searchTerm);
        }
        url.append(pathPrefix).append(api).append(pathPreFix);
        url.append(searchTerm);
        return url;
    }

    private Map<String, String> extractData(ResponseEntity response, String searchTerm) {
        Map<String, String> extractedData = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));
            extractedData.put("description", Objects.requireNonNull(rootNode.path("query").path("pages").elements().next().get("extract").asText()));
            JsonNode extractNode = rootNode.path("query").path("pages").elements().next().get("extract");
            if (extractNode != null && !extractNode.isNull()) {
                extractedData.put("description", extractNode.asText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extractedData;
    }
}
