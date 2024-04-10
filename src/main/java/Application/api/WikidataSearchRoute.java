package Application.api;

import Application.utils.CustomRetryTemplate;
import Application.utils.RestTempUtil;
import Application.utils.RestTemplateConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * There is no hard speed limit on read requests, but be considerate and try not to take a site down. Most system
 * administrators reserve the right to unceremoniously block you if you do endanger the stability of their site.
 * Making your requests in series rather than in parallel, by waiting for one request to finish before sending a new
 * request, should result in a safe request rate (https://www.mediawiki.org/wiki/API:Etiquette)
 */
@RestController
public class WikidataSearchRoute {
    private static final Logger logger = LoggerFactory.getLogger(WikidataSearchRoute.class.getName());
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
                null, pathPrefix, null);
        // Initialize any properties or perform setup logic here
        logger.info("Initialized MusicBrainzIDSearchRoute with properties: " +
                        "protocol={}, host={}, pathPrefix={}, api={}",
                protocol, host, pathPrefix, api);
    }

    public ResponseEntity<String> doGetResponse(String wikidataSearchTerm) throws URISyntaxException {
        URI uri = getUri(wikidataSearchTerm);
        ResponseEntity<String> response = RestTempUtil.getResponse(uri);
        handleResponse(response, uri.toString());
        return response;
    }

    public URI getUri(String wikidataSearchTerm) {
        return RestTempUtil.constructUriWikiData(wikidataSearchTerm, config);
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

    public ResponseEntity<String> handleResponse(ResponseEntity<String> response, String url) throws URISyntaxException {
        if (response.getBody() == null || response.getBody().isEmpty() || response.getBody().contains("no-such-entity")) {
            logger.info("The requested uri:" + url + " did not match any data on Wikidata");
        } else if (response.getBody().contains("ratelimits")) {
            return handleRateLimitations(url);
        }
        return response;
    }
    public RestTemplateConfig getRestConfig(){
        return config;
    }
}
