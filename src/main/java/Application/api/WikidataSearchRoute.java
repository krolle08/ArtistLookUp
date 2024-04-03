package Application.api;

import Application.utils.CustomRetryTemplate;
import Application.service.ArtistContainer.WikiInfoObj;
import Application.utils.RestTempUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.util.logging.Logger;

/**
 * Wikidata documentation:
 * There is no hard speed limit on read requests, but be considerate and try not to take a site down. Most system
 * administrators reserve the right to unceremoniously block you if you do endanger the stability of their site.
 * Making your requests in series rather than in parallel, by waiting for one request to finish before sending a new
 * request, should result in a safe request rate (https://www.mediawiki.org/wiki/API:Etiquette)
 */
@RestController
public class WikidataSearchRoute {
    private static final Logger logger = Logger.getLogger(WikidataSearchRoute.class.getName());
    private final String protocol = "https";
    private final String schemeDelimiter = "://";
    private final String host = "wikidata.org";
    private final String pathPrefix = "/w";
    private final String api = "/api.php";

    public void getWikidataForArtist(WikiInfoObj wikiInfoObj) throws URISyntaxException, JsonProcessingException {
        String apiUrl = buildWikiDataUri(wikiInfoObj.getWikidata());
        ResponseEntity<String> response = getResponse(apiUrl);
        extractWikiPediaData(response, wikiInfoObj);
    }

    private String buildWikiDataUri(String wikiDataSearchTerm) {
        return UriComponentsBuilder.fromUriString(protocol + schemeDelimiter + host + pathPrefix + api)
                .queryParam("action", "wbgetentities")
                .queryParam("format", "json")
                .queryParam("ids", wikiDataSearchTerm)
                .queryParam("props", "sitelinks")
                .build()
                .toUriString();
    }

    private ResponseEntity<String> getResponse(String apiUrl) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        handleResponse(response, apiUrl);
        return response;
    }

    public static ResponseEntity<String> handleResponse(ResponseEntity<String> response, String apiUrl) throws URISyntaxException {
        if (response.getBody().contains("ratelimits")) {
            return handleRateLimitations(apiUrl);
        } else if (response.getBody().contains("no-such-entity")) {
            logger.info("The requested uri:" + apiUrl + " did not match any data on Wikidata");
        }
        return response;
    }

    private static ResponseEntity<String> handleRateLimitations(String apiUrl) throws URISyntaxException {
        logger.warning("Rate limits detected. Retrying...");
        // Construct RetryTemplate
        CustomRetryTemplate retryTemplate = new CustomRetryTemplate();
        RetryCallback<ResponseEntity<String>, URISyntaxException> retryCallback = retryContext -> {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(apiUrl, String.class);
        };
        // Execute with RetryTemplate
        ResponseEntity<String> newResponse = retryTemplate.execute(retryCallback);
        if (newResponse.getBody().isEmpty()) {
            logger.info("The request on uri:" + apiUrl + " did not match any data in Wikidata");
        }
        return newResponse;
    }

    private void extractWikiPediaData(ResponseEntity response, WikiInfoObj wikiInfoObj) throws JsonProcessingException {
        wikiInfoObj.setWikiDataStatuccode(response.getStatusCodeValue());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody().toString());
            String wikipediaSearchTerm = rootNode.path("entities")
                    .path(wikiInfoObj.getWikidata().toUpperCase())
                    .path("sitelinks")
                    .path("enwiki")
                    .get("title")
                    .asText();
            if (wikipediaSearchTerm.isEmpty()) {
                logger.info("No word for searching on wikipedia was found in the wikidata response for:" + wikiInfoObj.getWikidata());
                return;
            }
            wikiInfoObj.setWikipediaSearchTerm(RestTempUtil.encodeString(wikipediaSearchTerm));
    }
}
