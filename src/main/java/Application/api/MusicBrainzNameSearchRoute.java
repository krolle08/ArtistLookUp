package Application.api;

import Application.service.ArtistInfo;
import Application.service.TypeOfSearchEnum;
import Application.utils.RestTemp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

@RestController
public class MusicBrainzNameSearchRoute {

    /*  @Value("${musicBrainz.protocol}")
      private String PROTOCOL;
      @Value("${musicBrainz.host}")
      private String HOST;
      @Value("${musicBrainz.host}")
      private int PORT;
      @Value("${musicBrainz.servicePath}")

     */
    private final Log log = LogFactory.getLog(MusicBrainzNameSearchRoute.class);
    private final String protocol = "http";
    private final String schemeDelimiter = "://";
    private final String host = "musicbrainz.org";
    private final Integer port = 80;
    private final String pathPrefix = "/ws";
    private final String version = "/2";
    private final String annotation = "/artist";
    private final String query = "/?query=";
    private final String json = "fmt=json";

    public ArtistInfo getArtistMBID(Map<String, String> filterParams){
        try {
            URI uri = createURI(filterParams);
            ResponseEntity<String> responseEntity = getResponse(uri);
            if (RestTemp.isBodyEmpty(responseEntity, "artists")) {
                log.warn("No response was given on the provided URI: " + uri + " make sure that the search type and criteria are correct");
                return null;
            }
            return extractData(responseEntity, filterParams.get(TypeOfSearchEnum.ARTIST.getSearchType()));
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON response: " + e.getMessage());
            return null;
        }
    }

    private URI createURI(Map<String, String> filterParams) {
        try {
            return new URI(RestTemp.constructUri(filterParams, query, protocol, annotation, schemeDelimiter, host,
                    port, pathPrefix, version, json).toString());
        } catch (URISyntaxException e) {
            log.error("Error constructing URI: " + e.getMessage());
            return null;
        }
    }

    private ResponseEntity<String> getResponse(URI uri) {
        RestTemplate restTemplate = RestTemp.restTemplate(host, port);
        return restTemplate.getForEntity(uri, String.class);
    }

    private ArtistInfo extractData(ResponseEntity responseEntity, String filterParams) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseEntity.getBody().toString());
        String artist = filterParams;
        String highestScoreId = null;
        String artistName = null;
        int highestScore = Integer.MIN_VALUE;

        Iterator<JsonNode> annotationIterator = rootNode.path("artists").elements();

        while (annotationIterator.hasNext()) {
            JsonNode annotation = annotationIterator.next();
            int score = annotation.path("score").asInt();
            String type = annotation.path("name").asText();

            if (artist.equalsIgnoreCase(type) && score > highestScore) {
                highestScore = score;
                highestScoreId = annotation.path("id").asText();
                artistName = type;
            }
        }
        ArtistInfo artistInfo = new ArtistInfo(artistName, highestScoreId);
        artistInfo.setmBStatusCode(String.valueOf(responseEntity.getStatusCodeValue()));
        return artistInfo;
    }
}