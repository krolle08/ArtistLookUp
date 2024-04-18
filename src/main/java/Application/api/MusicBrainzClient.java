package Application.api;

import Application.utils.LoggingUtility;
import Application.utils.RestTempUtil;
import Application.utils.RestTemplateConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * API service collecting the artist name, coverids, wikidata and wikipedia data if present based on the MusicBrainz ID
 * It must be ensured that the API never make more than ONE call per second
 * <a href="https://musicbrainz.org/doc/MusicBrainz_API">...</a>
 */
@RestController
public class MusicBrainzClient {

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

    @Value("${musicBrainz.json}")
    private String json;
    @Value("${musicBrainz.inc}")
    private String inc;
    private RestTemplateConfig config;

    @ResponseBody
    public ResponseEntity<String> doGetResponse(String searchTerm) {
        URI uri = getUri(searchTerm);
        ResponseEntity<String> response = RestTempUtil.getResponse(uri);
        return response;
    }

    public URI getUri(String searchTerm) {
        URI uri;
        try {
            uri = new URI(RestTempUtil.getMBIdUriconstructor(searchTerm, config).toString());
        } catch (URISyntaxException e) {
            String errorMessage = "Error constructing URI with Music Brainz id: " + searchTerm + " " + e.getMessage();
            LoggingUtility.error(errorMessage);
            e.printStackTrace();
            throw new IllegalArgumentException(errorMessage);
        }
        return uri;
    }

    public RestTemplateConfig getRestConfig(){
        return config;
    }
}
