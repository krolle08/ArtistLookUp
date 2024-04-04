package Application.api;

import Application.service.Artist.ArtistInfoObj;
import Application.utils.RestTempUtil;
import Application.utils.URIException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

/**
 * API service collecting the artist name, coverids, wikidata and wikipedia data if present based on the MusicBrainz ID
 * It must be ensured that the API never make more than ONE call per second
 * <a href="https://musicbrainz.org/doc/MusicBrainz_API">...</a>
 */
@RestController
public class MusicBrainzIDSearchRoute {
    private static final Logger logger = Logger.getLogger(MusicBrainzIDSearchRoute.class.getName());
    private final String protocol = "http";
    private final String schemeDelimiter = "://";
    private final String host = "musicbrainz.org";
    private final Integer port = 80;
    private final String pathPrefix = "/ws";
    private final String version = "/2";
    private final String queryTypeArtist = "/artist/";
    private static final String pathPostFix = "?fmt=json&inc=url-rels+release-groups";

    public URI getUrl(String searchTerm) {
        URI uri = null;
        try {
            uri = new URI(RestTempUtil.constructUri(searchTerm, queryTypeArtist, protocol, schemeDelimiter, host,
                    port, pathPrefix, version, pathPostFix).toString());
        } catch (URISyntaxException e) {
            logger.warning("Error constructing URI with mbid: " + searchTerm +
                    " " + e.getMessage());
        }
        return uri;
    }

    public ResponseEntity<String> getResponse(URI uri) {
        ResponseEntity<String> response = RestTempUtil.getResponse(uri, host, port);
        return response;
    }
}
