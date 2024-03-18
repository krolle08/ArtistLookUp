package Application.api;

import Application.features.CustomRetryTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Wikidata dokumentation and requirements:
 * There is no hard speed limit on read requests, but be considerate and try not to take a site down. Most system
 * administrators reserve the right to unceremoniously block you if you do endanger the stability of their site.
 * Making your requests in series rather than in parallel, by waiting for one request to finish before sending a new
 * request, should result in a safe request rate (https://www.mediawiki.org/wiki/API:Etiquette)
 */
@RestController
public class WikidataSearchRoute {
    private Log log = LogFactory.getLog(MusicBrainzIDSearchRoute.class);
    private final String protocol = "https";
    private final String schemeDelimiter = "://";
    private final String host = "wikidata.org";
    private final String pathPrefix = "/w";
    private final String api = "/api.php";
    private final String enwiki = "enwiki";
    public Map<String, String> getWikidataFromArtist(String searchTerm) throws URISyntaxException {
        Map<String, String> result = new HashMap<>();
        if (searchTerm.isEmpty()) {
            log.info("No searchterm was provided:" + searchTerm);
            return result;
        }
        String apiUrl = UriComponentsBuilder.fromHttpUrl(protocol + schemeDelimiter + host + pathPrefix + api)
                .queryParam("action", "wbgetentities")
                .queryParam("format", "json")
                .queryParam("ids", searchTerm)
                .queryParam("props", "sitelinks")
                .build()
                .toUriString();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        if (response.getBody().contains("ratelimited")) {
            handleRateLimitations(apiUrl);
        } else if (response.getBody().contains("no-such-entity")) { //CREATE ANOTHER FIX FOR THIS!!!
            log.info("The request on uri:" + apiUrl + "did not match any data on Wikidata");
            return result;
        }
        // Handle the original response
        // Extract HTTP status code
        result.put("wikidataStatusCode", String.valueOf(response.getStatusCodeValue()));
        result.putAll(extractData(response, searchTerm));
        return result;
    }
    private void processResponse(ResponseEntity<String> response, String uri) {
        if (response.getBody().isEmpty()) {
            log.info("The request on uri:" + uri + "did not match any data in Wikidata");
        }
    }
    private Map<String, String> extractData(ResponseEntity response, String searchTerm) {
        Map<String, String> extractedData = new HashMap<>();
        try {
            // Extract HTTP status code
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody().toString());
            String wikipediaSearchTerm = rootNode.path("entities").path(searchTerm.toUpperCase()).path("sitelinks").path(enwiki).get("title").asText();
            if (wikipediaSearchTerm.isEmpty()) {
                log.info("No description in the response for:" + searchTerm);
                return extractedData;
            }
            extractedData.put("wikipediaSearchTerm", encodeString(wikipediaSearchTerm));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extractedData;
    }
    private void handleRateLimitations(String apiUrl) {
        log.warn("Rate limits detected. Retrying...");
        // Construct RetryTemplate
        CustomRetryTemplate retryTemplate = new CustomRetryTemplate();
        RetryCallback<ResponseEntity<String>, URISyntaxException> retryCallback = retryContext -> {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(apiUrl, String.class);
        };
        // Execute with RetryTemplate
        ResponseEntity<String> newResponse = null;
        try {
            newResponse = retryTemplate.execute(retryCallback);
        } catch (URISyntaxException e) {
            log.error("Error occurred while executing request", e);
            return;
        }
        // Handle the response after retry
        processResponse(newResponse, apiUrl);
    }
    public static String encodeString(String input) {
        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Handle encoding exception
            e.printStackTrace();
            return null;
        }
    }
}
