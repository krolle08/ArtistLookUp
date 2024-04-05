package Application.api;

import Application.utils.RestTempUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * API service collecting the artist name, coverids, wikidata and wikipedia data if present based on the MusicBrainz ID
 * It must be ensured that the API never make more than ONE call per second
 * <a href="https://musicbrainz.org/doc/MusicBrainz_API">...</a>
 */
@RestController
public class MusicBrainzIDSearchRoute {
    private static final Logger logger = LoggerFactory.getLogger(MusicBrainzIDSearchRoute.class.getName());
    private final String protocol = "http";
    private final String schemeDelimiter = "://";
    private final String host = "musicbrainz.org";
    private final Integer port = 80;
    private final String pathPrefix = "/ws";
    private final String version = "/2";
    private final String queryTypeArtist = "/artist/";
    private static final String pathPostFix = "?fmt=json&inc=url-rels+release-groups";

    public URI getUri(String searchTerm) {
        URI uri;
        try {
            uri = new URI(RestTempUtil.constructUri(searchTerm, queryTypeArtist, protocol, schemeDelimiter, host,
                    port, pathPrefix, version, pathPostFix).toString());
        } catch (URISyntaxException e) {
            logger.error("Error constructing URI with mbid: " + searchTerm +
                    " " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
        return uri;
    }

    public ResponseEntity<String> doGetResponse(URI uri) {
        ResponseEntity<String> response = RestTempUtil.getResponse(uri, host, port);
        return response;
    }
}
