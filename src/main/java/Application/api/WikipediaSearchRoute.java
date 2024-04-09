package Application.api;

import Application.utils.RestTempUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Wikipedia documentation:
 * There is no hard speed limit on read requests, but be considerate and try not to take a site down. Most system
 * administrators reserve the right to unceremoniously block you if you do endanger the stability of their site.
 * Making your requests in series rather than in parallel, by waiting for one request to finish before sending a new
 * request, should result in a safe request rate (https://www.mediawiki.org/wiki/API:Etiquette)
 */
@RestController
public class WikipediaSearchRoute {
    private static final Logger logger = LoggerFactory.getLogger(WikipediaSearchRoute.class.getName());
    private final String protocol = "https";
    private final String schemeDelimiter = "://";
    private final String host = "en.wikipedia.org";
    private final String pathPrefix = "/w";
    private final String api = "/api.php";
    private final String postPreFix = "?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles=";

    public URI getUri(String searchTerm) {
        try {
            return new URI(RestTempUtil.constructUri(searchTerm, protocol, schemeDelimiter, host,
                    pathPrefix, api, postPreFix));
        } catch (URISyntaxException e) {
            logger.error("Error constructing URI with param: " + searchTerm +
                    " " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public ResponseEntity<String> doGetResponse(String searchTerm) {
        URI uri = getUri(searchTerm);
        return RestTempUtil.getResponse(uri);
    }
}
