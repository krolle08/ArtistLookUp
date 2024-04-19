package Application.api;

import Application.model.response.WikiDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * WikidataService documentation:
 * There is no speed limit on read requests. Most system administrators reserve the right to unceremoniously block the
 * application if it endangers the stability of the site.
 * Making requests in series rather than in parallel is preferred.
 * (https://www.mediawiki.org/wiki/API:Etiquette)
 */
@Service
public class WikidataClientImpl {

    @Value("${wikidata.url}")
    private String wikiDataBaseUrl;
    @Value("${wikidata.param}")
    private String wikiDataParam;
    @Autowired
    private RestTemplate restTemplate;

    public String buildUrl(String typeId) {
        return wikiDataBaseUrl + typeId + wikiDataParam;
    }

    public ResponseEntity<WikiDataResponse> getForObject(String url) throws Exception {
        ResponseEntity<WikiDataResponse> response = restTemplate.getForEntity(url, WikiDataResponse.class);
        return response;
    }
}
