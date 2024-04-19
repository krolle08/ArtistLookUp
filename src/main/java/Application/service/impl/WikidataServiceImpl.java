package Application.service.impl;

import Application.api.WikidataClientImpl;
import Application.model.response.WikiDataResponse;
import Application.service.WikidataService;
import Application.utils.CustomRetryTemplate;
import Application.utils.LoggingUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;

@Service
public class WikidataServiceImpl implements WikidataService {

    @Autowired
    private WikidataClientImpl wikidataClient;
    @Override
    public WikiDataResponse getWikipediaId(String wikidataId) throws Exception {
        String url =  wikidataClient.buildUrl(wikidataId);
        WikiDataResponse wikiDataResponse = wikidataClient.getForObject(url).getBody();
        return wikiDataResponse;
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
                LoggingUtility.info("The response on uri:" + uri + " did not match any data on WikidataService");
            }
        } catch (URISyntaxException e) {
            String errorMessage = "Error occured while creating the uri. " + e.getMessage();
            LoggingUtility.error(errorMessage);
            e.printStackTrace();
            throw new RuntimeException(errorMessage, e);
        }
        return newResponse;
    }

    public ResponseEntity<String> handleResponse(ResponseEntity<String> response, String uri) {
        if (response.getBody() == null || response.getBody().isEmpty() || response.getBody().contains("no-such-entity")) {
            LoggingUtility.info("The requested uri:" + uri + " did not match any data on WikidataService");
        } else if (response.getBody().contains("ratelimits")) {
            return handleRateLimitations(uri);
        }
        return response;
    }

}
