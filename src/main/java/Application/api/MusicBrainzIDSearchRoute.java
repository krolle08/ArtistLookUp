package Application.api;

import Application.utils.RestTempUtil;
import Application.utils.RestTemplateConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
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
    @Value("${musicBrainz.protocol}")
    private String protocol;

    @Value("${musicBrainz.host}")
    private String host;

    @Value("${musicBrainz.pathPrefix}")
    private String pathPrefix;

    @Value("${musicBrainz.version}")
    private String version;

    @Value("${musicBrainz.queryTypeArtist}")
    private String queryTypeArtist;

    @Value("${musicBrainz.pathPostFix}")
    private String pathPostFix;
    private RestTemplateConfig config;

    @PostConstruct
    public void init() {
        config = new RestTemplateConfig(protocol, host, null, pathPostFix, version,
                queryTypeArtist, pathPrefix, null);
        // Initialize any properties or perform setup logic here
        logger.info("Initialized MusicBrainzIDSearchRoute with properties: " +
                        "protocol={}, host={}, pathPrefix={}, version={}, queryTypeArtist={}",
                protocol, host, pathPrefix, version, queryTypeArtist);
    }

    @ResponseBody
    public ResponseEntity<String> doGetResponse(String searchTerm) {
        URI uri = getUri(searchTerm);
        ResponseEntity<String> response = RestTempUtil.getResponse(uri);
        return response;
    }

    public URI getUri(String searchTerm) {
        URI uri;
        try {
            uri = new URI(RestTempUtil.constructUri(searchTerm, config).toString());
        } catch (URISyntaxException e) {
            logger.error("Error constructing URI with mbid: " + searchTerm +
                    " " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
        return uri;
    }

    public RestTemplateConfig getRestConfig(){
        return config;
    }
}
