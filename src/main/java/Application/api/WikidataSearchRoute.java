package Application.api;

import Application.utils.CustomRetryTemplate;
import Application.utils.RestTempUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Wikidata documentation:
 * There is no hard speed limit on read requests, but be considerate and try not to take a site down. Most system
 * administrators reserve the right to unceremoniously block you if you do endanger the stability of their site.
 * Making your requests in series rather than in parallel, by waiting for one request to finish before sending a new
 * request, should result in a safe request rate (https://www.mediawiki.org/wiki/API:Etiquette)
 */
@RestController
public class WikidataSearchRoute {
    private static final Logger logger = LoggerFactory.getLogger(WikidataSearchRoute.class.getName());
    private final String protocol = "https";
    private final String schemeDelimiter = "://";
    private final String host = "wikidata.org";
    private final String pathPrefix = "/w";
    private final String api = "/api.php";

    private static String buildWikiDataUri(String wikiDataSearchTerm, String protocol, String schemeDelimiter,
                                           String host, String pathPrefix, String api) {
        return UriComponentsBuilder.fromUriString(protocol + schemeDelimiter + host + pathPrefix + api)
                .queryParam("action", "wbgetentities")
                .queryParam("format", "json")
                .queryParam("ids", wikiDataSearchTerm)
                .queryParam("props", "sitelinks")
                .build()
                .toUriString();
    }

    private static ResponseEntity<String> handleRateLimitations(String url) throws URISyntaxException {
        logger.warn("Rate limits detected. Retrying...");
        // Construct RetryTemplate
        CustomRetryTemplate retryTemplate = new CustomRetryTemplate();
        RetryCallback<ResponseEntity<String>, URISyntaxException> retryCallback = retryContext -> {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(url, String.class);
        };
        // Execute with RetryTemplate
        ResponseEntity<String> newResponse = retryTemplate.execute(retryCallback);
        if (newResponse.getBody().isEmpty()) {
            logger.info("The request on uri:" + url + " did not match any data in Wikidata");
        }
        return newResponse;
    }

    public URI getUri(String wikidataSearchTerm) {
        try {
            return new URI(buildWikiDataUri(wikidataSearchTerm, protocol, schemeDelimiter, host, pathPrefix, api));
        } catch (URISyntaxException e) {
            logger.error("Error constructing URI with param: " + wikidataSearchTerm +
                    " " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public ResponseEntity<String> doGetResponse(URI uri) throws URISyntaxException {
        ResponseEntity<String> response = RestTempUtil.getResponse(uri);
        handleResponse(response, uri.toString());
        return response;
    }

    public ResponseEntity<String> handleResponse(ResponseEntity<String> response, String url) throws URISyntaxException {
        if (response.getBody().contains("ratelimits")) {
            return handleRateLimitations(url);
        } else if (response.getBody().contains("no-such-entity")) {
            logger.info("The requested uri:" + url + " did not match any data on Wikidata");
        }
        return response;
    }
}
