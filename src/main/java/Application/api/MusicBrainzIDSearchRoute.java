package Application.api;

import Application.service.AlbumInfo;
import Application.service.ArtistInfo;
import Application.service.WikiInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MusicBrainzIDSearchRoute {

    /*  @Value("${musicBrainz.protocol}")
      private String PROTOCOL;
      @Value("${musicBrainz.host}")
      private String HOST;
      @Value("${musicBrainz.host}")
      private int PORT;
      @Value("${musicBrainz.servicePath}")

     */
    private Log log = LogFactory.getLog(MusicBrainzIDSearchRoute.class);
    private final String protocol = "http";
    private final String schemeDelimiter = "://";
    private final String host = "musicbrainz.org";
    private final Integer port = 80;
    private final String pathPrefix = "/ws";
    private final String version = "/2";
    private final String queryTypeArtist = "/artist/";
    private static final String pathPostFix = "?fmt=json&inc=url-rels+release-groups";

    @GetMapping("/MBArtist/{Id}")
    public ArtistInfo  getDataWithMBID(@PathVariable String Id, ArtistInfo artistInfo) throws URISyntaxException {
        URI uri = createURI(artistInfo.getmBID().toString(), Id);
        ResponseEntity<String> response = getResponse(uri);
       // artistInfo.setmBStatusCode(String.valueOf(response.getStatusCodeValue()));
        if (RestTemp.isBodyEmpty(response)) {
            log.info("No response was given on the provided URI: " + uri + " make sure that the search type and criteria are correct");
            return null;
        }
        return extractData(response, artistInfo);
    }

    private URI createURI(String mBID, String iD) throws URISyntaxException {
        String fullPath;
        if (mBID != null) {
            fullPath = RestTemp.constructUrl(mBID, queryTypeArtist, protocol, schemeDelimiter, host,
                    port, pathPrefix, version, pathPostFix).toString();
        } else {
            fullPath = RestTemp.constructUrl(iD, queryTypeArtist, protocol, schemeDelimiter, host,
                    port, pathPrefix, version, pathPostFix).toString();
        }
        return new URI(fullPath);
    }


    private ResponseEntity getResponse(URI uri) {
        RestTemplate restTemplate = RestTemp.restTemplate(host, port);
        return restTemplate.getForEntity(uri, String.class);
    }

    private void extractData(ResponseEntity response, ArtistInfo artistInfo) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(response.getBody().toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        artistInfo.setName(extractName(rootNode));
        artistInfo.setWikiInfo(extractwikiData(rootNode));
        artistInfo.setAlbums(extractCoverAlbumData(rootNode));
    }

    private String extractName(JsonNode rootNode) {
        if (!rootNode.get("name").isNull()) {
            return rootNode.get("name").asText();
        }
        log.warn("No name was found for artist");
        return null;
    }

    private WikiInfo extractwikiData(JsonNode rootNode) {
        Map<String, String> result = new HashMap<>();
        JsonNode relations = rootNode.get("relations");
        final String wikipedia = "wikipedia";
        final String wikidata = "wikidata";
        String wikipediaResult = null;
        String wikidataResult = null;
        // Iterate through the array of objects
        for (JsonNode node : relations) {
            if (!node.isNull() || !node.isEmpty()) {
                // Check if the object has a direct wikipedia link
                if ((node.get("type").asText()).toString().contains(wikipedia)) {
                    wikipediaResult = node.get("url").get("resource").asText();
                    break;
                } else if (node.get("type").asText().toString().contains(wikidata)) {
                    String wikiData = node.get("url").get("resource").asText().toString();
                    String wikiDataTerm = wikiData.replaceAll("^.*?/(Q\\d+)$", "$1");
                    wikidataResult = wikiDataTerm;
                }
            }
        }
        return new WikiInfo(wikidataResult, wikipediaResult);
    }

    private List<AlbumInfo> extractCoverAlbumData(JsonNode rootNode) {
        // Add coverAlbumData
        return extractCoverIdAndTitle(rootNode);
    }

    private List<AlbumInfo> extractCoverIdAndTitle(JsonNode rootNode) {
        List<AlbumInfo> albums = new ArrayList<>();
        JsonNode relations = rootNode.get("release-groups");
        for (JsonNode node : relations) {
            if (!node.isNull() || !node.isEmpty()) {
                if (node.path("primary-type").asText().equals("Album")) {
                    AlbumInfo album = new AlbumInfo(node.get("id").asText(), node.get("title").asText(), null);
                    albums.add(album);
                }
            }
        }
        return albums;
    }
}
