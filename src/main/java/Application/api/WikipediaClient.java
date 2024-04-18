package Application.api;

import Application.utils.LoggingUtility;
import Application.utils.RestTempUtil;
import Application.utils.RestTemplateConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URI;

/**
 * Wikipedia documentation:
 * There is no hard speed limit on read requests, but be considerate and try not to take a site down. Most system
 * administrators reserve the right to unceremoniously block you if you do endanger the stability of their site.
 * Making your requests in series rather than in parallel, by waiting for one request to finish before sending a new
 * request, should result in a safe request rate (https://www.mediawiki.org/wiki/API:Etiquette)
 */
@RestController
public class WikipediaClient {

    @Value("${wikipedia.protocol}")
    private String protocol;

    @Value("${wikipedia.host}")
    private String host;
    @Value("${wikipedia.pathPrefix}")
    private String pathPrefix;

    @Value("${wikipedia.api}")
    private String api;
    private RestTemplateConfig config;

    @PostConstruct
    public void init() {
        config = new RestTemplateConfig(protocol, host, null, api, null,
                null, pathPrefix, null, null);
        // Initialize any properties or perform setup logic here
        LoggingUtility.info("Initialized MusicBrainzClient with properties: " +
                        "protocol={}, host={}, pathPrefix={}, api={}",
                protocol, host, pathPrefix, api);
    }

    public ResponseEntity<String> doGetResponse(String searchTerm) {
        URI uri = getUri(searchTerm);
        return RestTempUtil.getResponse(uri);
    }

    public URI getUri(String searchTerm) {
        return RestTempUtil.getWikipediaUriconstructor(searchTerm, config);
    }


}
