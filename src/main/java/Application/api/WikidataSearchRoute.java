package Application.api;

import Application.utils.CustomRetryTemplate;
import Application.utils.LoggingUtility;
import Application.utils.RestTempUtil;
import Application.utils.RestTemplateConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Wikidata documentation:
 * There is no speed limit on read requests. Most system administrators reserve the right to unceremoniously block the
 * application if it endangers the stability of the site.
 * Making requests in series rather than in parallel is preferred.
 * (https://www.mediawiki.org/wiki/API:Etiquette)
 */
@RestController
public class WikidataSearchRoute {
    @Value("${wikiData.protocol}")
    private String protocol;

    @Value("${wikiData.host}")
    private String host;
    @Value("${wikiData.pathPrefix}")
    private String pathPrefix;

    @Value("${wikiData.api}")
    private String api;
    private RestTemplateConfig config;

    @PostConstruct
    public void init() {
        config = new RestTemplateConfig(protocol, host, null, api, null,
                null, pathPrefix, null, null);
        // Initialize any properties or perform setup logic here
        LoggingUtility.info("Initialized MusicBrainzIDSearchRoute with properties: " +
                        "protocol={}, host={}, pathPrefix={}, api={}",
                protocol, host, pathPrefix, api);
    }

    public ResponseEntity<String> doGetResponse(String wikidataSearchTerm) {
        URI uri = getUri(wikidataSearchTerm);
        ResponseEntity<String> response = RestTempUtil.getResponse(uri);
        handleResponse(response, uri.toString());
        return response;
    }

    public URI getUri(String wikidataSearchTerm) {
        return RestTempUtil.getWikiDataUriconstructor(wikidataSearchTerm, config);
    }

    private static ResponseEntity<String> handleRateLimitations(String uri) {
        LoggingUtility.warn("Rate limits detected. Retrying...");
        // Construct RetryTemplate
        CustomRetryTemplate retryTemplate = new CustomRetryTemplate();
        RetryCallback<ResponseEntity<String>, URISyntaxException> retryCallback = retryContext -> {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(uri, String.class);
        };
        ResponseEntity<String> newResponse = null;
        try {
            // Execute with RetryTemplate
            newResponse = retryTemplate.execute(retryCallback);
            if (newResponse.getBody().isEmpty()) {
                LoggingUtility.info("The response on uri:" + uri + " did not match any data on Wikidata");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            LoggingUtility.error("Error occured while creating the uri. " + e.getMessage());
        }
        return newResponse;
    }

    public ResponseEntity<String> handleResponse(ResponseEntity<String> response, String uri) {
        if (response.getBody() == null || response.getBody().isEmpty() || response.getBody().contains("no-such-entity")) {
            LoggingUtility.info("The requested uri:" + uri + " did not match any data on Wikidata");
        } else if (response.getBody().contains("ratelimits")) {
            return handleRateLimitations(uri);
        }
        return response;
    }

    public RestTemplateConfig getRestConfig() {
        return config;
    }
}
