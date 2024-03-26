package Application.api;

import Application.features.RestTemp;
import Application.service.ArtistInfo;
import Application.service.TypeOfSearchEnum;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public ArtistInfo getArtistInfo(Map<String, String> filterParams) throws URISyntaxException, JsonProcessingException {
        URI uri = createURI(filterParams);
        ResponseEntity<String> responseEntity = getResponse(uri);
        if (RestTemp.isBodyEmpty(responseEntity)) {
            log.warn("No response was given on the provided URI: " + uri + " make sure that the search type and criteria are correct");
            return null;
        }
            return extractData(responseEntity, filterParams, uri);
        }

    private URI createURI(Map<String, String> filterParams) throws URISyntaxException {
            return new URI(RestTemp.constructUrl(filterParams, query, protocol, annotation, schemeDelimiter, host,
                    port, pathPrefix, version, json).toString());
        }

    private ResponseEntity getResponse(URI uri) {
        RestTemplate restTemplate = RestTemp.restTemplate(host, port);
        return restTemplate.getForEntity(uri, String.class);
    }

    private ArtistInfo extractData(ResponseEntity responseEntity, Map<String, String> filterParams, URI uri) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseEntity.getBody().toString());
        try {
            String artist = filterParams.get(TypeOfSearchEnum.ARTIST.getSearchType());
            Iterator<JsonNode> annotationIterator = rootNode.path("artists").elements();
            // Flag variable to indicate whether the desired ID has been found
            int highestScore = Integer.MIN_VALUE;
            String highestScoreId = null;
            String artistName = null;

            // Iterate through the annotations
            while (annotationIterator.hasNext()) {
                JsonNode annotation = annotationIterator.next();
                int score = annotation.path("score").asInt(); // Get the score
                String type = annotation.path("name").asText();

                if (artist.equalsIgnoreCase(type) && score > highestScore) {
                    highestScore = score;
                    highestScoreId = annotation.path("id").asText();
                    artistName = type;
                }
            }
            ArtistInfo artistInfo = new ArtistInfo(artistName, highestScoreId, uri);
            artistInfo.setmBStatusCode(String.valueOf(responseEntity.getStatusCodeValue()));
            return artistInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArtistInfo();
    }

    private String extractMBIdFromText(String text, String searchTerm) {
        // Regular expression to extract the ID between [artist: and | characters
        String regex = "([a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12})" + Pattern.quote(searchTerm);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}