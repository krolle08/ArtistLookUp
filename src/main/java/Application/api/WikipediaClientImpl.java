package Application.api;

import Application.model.response.WikipediaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * WikipediaService documentation:
 * There is no hard speed limit on read requests, but be considerate and try not to take a site down. Most system
 * administrators reserve the right to unceremoniously block you if you do endanger the stability of their site.
 * Making your requests in series rather than in parallel, by waiting for one request to finish before sending a new
 * request, should result in a safe request rate (https://www.mediawiki.org/wiki/API:Etiquette)
 */
@Service
public class WikipediaClientImpl {

    @Value("${wikipedia.url}")
    private String wikipediaUrl;
    @Autowired
    private RestTemplate restTemplate;


    public String buildUrl(String path) {
        return wikipediaUrl + path;
    }

    public ResponseEntity<WikipediaResponse> getForObject(String url) throws Exception {
        ResponseEntity<WikipediaResponse> response = restTemplate.getForEntity(url, WikipediaResponse.class);
        return response;    }
}
