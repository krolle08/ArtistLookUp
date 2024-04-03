package Application.api;

import Application.service.ArtistContainer.ArtistInfoObj;
import Application.service.SearchArtistService;
import Application.service.TypeOfSearchEnum;
import Application.utils.RestTempUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

/**
 * API service collecting the MusicBrainz ID according to the input providing by the user
 */
@RestController
public class MusicBrainzNameSearchRoute {
    private static final Logger logger = LoggerFactory.getLogger(SearchArtistService.class.getName());
    private final String protocol = "http";
    private final String schemeDelimiter = "://";
    private final String host = "musicbrainz.org";
    private final Integer port = 80;
    private final String pathPrefix = "/ws";
    private final String version = "/2";
    private final String annotation = "/artist";
    private final String query = "/?query=";
    private final String json = "fmt=json";


    public ArtistInfoObj getArtistMBID(Map<String, String> filterParams) throws JsonProcessingException {
        ArtistInfoObj artistInfoObj = new ArtistInfoObj();

        URI uri = createURI(filterParams);
        if(uri == null) {
            logger.warn("Error creating URI, skipped musicbrainz lookup");
            return artistInfoObj;
        }
        ResponseEntity<String> responseEntity = getResponse(uri);
        if (RestTempUtil.isBodyEmpty(responseEntity, "artists")) {
            logger.warn("No response was given on the provided URI: " + uri + " make sure that the search type and search parameter: " + filterParams.entrySet().iterator().next().getKey() + " are correct");
            return artistInfoObj;
        }
        artistInfoObj = extractData(responseEntity, filterParams.get(TypeOfSearchEnum.ARTIST.getSearchType()));
        return artistInfoObj;
    }

    public URI createURI(Map<String, String> filterParams) {
        try {
            StringBuffer uri = RestTempUtil.constructUri(filterParams, query, protocol, annotation, schemeDelimiter, host,
                    port, pathPrefix, version, json);
            return new URI(uri.toString());
        } catch (URISyntaxException e) {
            logger.error("Error constructing URI with param: " + filterParams.entrySet().iterator().next().getValue() +
                    " " + e.getMessage());
            return null;
        }
    }

    public ResponseEntity<String> getResponse(URI uri) {
        RestTemplate restTemplate = RestTempUtil.restTemplate(host, port);
        return restTemplate.getForEntity(uri, String.class);
    }

    private ArtistInfoObj extractData(ResponseEntity responseEntity, String filterParams) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseEntity.getBody().toString());
        String mbid = null;
        String artistName = null;
        int highestScore = Integer.MIN_VALUE;

        Iterator<JsonNode> annotationIterator = rootNode.path("artists").elements();

        while (annotationIterator.hasNext()) {
            JsonNode annotation = annotationIterator.next();
            int score = annotation.path("score").asInt();
            String type = annotation.path("name").asText();

            if (filterParams.equalsIgnoreCase(type) && score > highestScore) {
                highestScore = score;
                mbid = annotation.path("id").asText();
                artistName = type;
            }
        }
        ArtistInfoObj artistInfoObj = new ArtistInfoObj(artistName, mbid);
        artistInfoObj.setmBStatusCode(responseEntity.getStatusCodeValue());
        return artistInfoObj;
    }
}