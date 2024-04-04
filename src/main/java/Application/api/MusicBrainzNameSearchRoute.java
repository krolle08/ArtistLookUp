package Application.api;

import Application.service.Artist.ArtistInfoObj;
import Application.service.Artist.SearchArtistService;
import Application.utils.TypeOfSearchEnum;
import Application.utils.RestTempUtil;
import Application.utils.URIException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

/**
 * API service collecting the MusicBrainz ID according to the input providing by the user
 */
@RestController
public class MusicBrainzNameSearchRoute {
    private static final Logger logger = LoggerFactory.getLogger(MusicBrainzNameSearchRoute.class.getName());
    private final String protocol = "http";
    private final String schemeDelimiter = "://";
    private final String host = "musicbrainz.org";
    private final Integer port = 80;
    private final String pathPrefix = "/ws";
    private final String version = "/2";
    private final String annotation = "/artist";
    private final String query = "/?query=";
    private final String json = "fmt=json";

    public URI getUri(Map<String, String> filterParams) throws IllegalArgumentException {
        URI uri;
        try {
            uri = new URI(RestTempUtil.constructUri(filterParams, query, protocol, annotation, schemeDelimiter, host, port, pathPrefix, version, json).toString());
        } catch (URISyntaxException e) {
            logger.error("Error constructing URI with param: " + filterParams.entrySet().iterator().next().getValue() +
                    " " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
        return uri;
    }
    public ResponseEntity<String> getResponse(URI uri){
        ResponseEntity<String> responseEntity = RestTempUtil.getResponse(uri, host, port);
        if (RestTempUtil.isBodyEmpty(responseEntity, "artists")) {
            logger.warn("No response was given on the provided URI: " + uri + " make sure that the search type and search parameter are correct " + filterParams.entrySet().iterator().next().getKey() + ", " + filterParams.entrySet().iterator().next().getValue());
        }
        return responseEntity;
    }
}