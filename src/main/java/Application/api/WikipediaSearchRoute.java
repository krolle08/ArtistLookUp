package Application.api;

import Application.utils.RestTempUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

/**
 * Wikipedia documentation:
 * There is no hard speed limit on read requests, but be considerate and try not to take a site down. Most system
 * administrators reserve the right to unceremoniously block you if you do endanger the stability of their site.
 * Making your requests in series rather than in parallel, by waiting for one request to finish before sending a new
 * request, should result in a safe request rate (https://www.mediawiki.org/wiki/API:Etiquette)
 */
@RestController
public class WikipediaSearchRoute {
    private static final Logger logger = Logger.getLogger(WikipediaSearchRoute.class.getName());
    private final String protocol = "https";
    private final String schemeDelimiter = "://";
    private final String host = "en.wikipedia.org";
    private final String pathPrefix = "/w";
    private final String api = "/api.php";
    private final String postPreFix = "?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles=";

    public URI getUrl(String searchTerm) throws URISyntaxException {
        return new URI(RestTempUtil.constructUri(searchTerm,  protocol,  schemeDelimiter,  host,
                pathPrefix,  api,  postPreFix).toString());
    }
    public ResponseEntity<String> getResponse(URI uri) {
        return RestTempUtil.getResponse(uri);
    }
}
