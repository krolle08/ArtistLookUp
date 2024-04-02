package Application.api;

import Application.service.AlbumInfo;
import Application.service.ArtistInfo;
import Application.service.GetDataImpl;
import Application.service.WikiInfo;
import Application.utils.RestTemp;
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
import java.util.List;

/**
 * All users of the API must ensure that each of their client applications never make more than ONE call per second
 * <a href="https://musicbrainz.org/doc/MusicBrainz_API">...</a>
 */
@RestController
public class MusicBrainzIDSearchRoute {
    private Log log = LogFactory.getLog(MusicBrainzIDSearchRoute.class);
    private final String protocol = "http";
    private final String schemeDelimiter = "://";
    private final String host = "musicbrainz.org";
    private final Integer port = 80;
    private final String pathPrefix = "/ws";
    private final String version = "/2";
    private final String queryTypeArtist = "/artist/";
    private static final String pathPostFix = "?fmt=json&inc=url-rels+release-groups";

    @GetMapping("/MBArtist/{mBid}")
    public ArtistInfo getDataWithMBID(@PathVariable String mBid) {
        URI uri = createURI(mBid);
        ResponseEntity<String> response = getResponse(uri);
        // artistInfo.setmBStatusCode(String.valueOf(response.getStatusCodeValue()));
        if (RestTemp.isBodyEmpty(response, null)) {
            log.info("No response was given on the provided URI: " + uri + " make sure that the search type and criteria are correct");
            return null;
        }
        ArtistInfo artistInfo = extractData(response);
        return artistInfo;
    }

    private URI createURI(String mBid) {
        try {
            String fullPath = RestTemp.constructUri(mBid, queryTypeArtist, protocol, schemeDelimiter, host,
                    port, pathPrefix, version, pathPostFix).toString();
            return new URI(fullPath);
        } catch (URISyntaxException e) {
            log.error("Error constructing URI: " + e.getMessage());
            return null;
        }
    }


    private ResponseEntity<String> getResponse(URI uri) throws RuntimeException {
        RestTemplate restTemplate = RestTemp.restTemplate(host, port);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        return response;
    }

    private ArtistInfo extractData(ResponseEntity response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(response.getBody().toString());
            String name = extractName(rootNode);
            WikiInfo wikiInfo = extractwikiData(rootNode);
            List<AlbumInfo> albums = extractCoverIdAndTitle(rootNode);
            return new ArtistInfo(name, null, wikiInfo, albums);
        } catch (JsonProcessingException e) {
            log.error("A problem occured during the mapping of the response: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String extractName(JsonNode rootNode) {
        JsonNode nameNode = rootNode.get("name");
        if (nameNode != null && !nameNode.isNull()) {
            return nameNode.asText();
        } else {
            log.warn("No name added");
            return null;
        }
    }

    private WikiInfo extractwikiData(JsonNode rootNode) {
        String wikipediaResult = null;
        String wikidataResult = null;

        JsonNode relations = rootNode.get("relations");
        if (relations != null && relations.isArray()) {
            for (JsonNode node : relations) {
                String type = node.path("type").asText();
                if (type.contains("wikipedia")) {
                    wikipediaResult = node.path("url").path("resource").asText();
                    break;
                } else if (type.contains("wikidata")) {
                    String wikiData = node.path("url").path("resource").asText();
                    wikidataResult = extractWikiDataTerm(wikiData);
                }
            }
        }
        return new WikiInfo(wikidataResult, wikipediaResult);
    }

    private String extractWikiDataTerm(String wikiData) {
        return wikiData.replaceAll("^.*?/(Q\\d+)$", "$1");
    }

    private List<AlbumInfo> extractCoverIdAndTitle(JsonNode rootNode) {
        List<AlbumInfo> albums = new ArrayList<>();
        JsonNode relations = rootNode.get("release-groups");
        if (!relations.isNull() && relations.isArray()) {
            for (JsonNode node : relations) {
                if (node.path("primary-type").asText().equals("Album")) {
                    String id = node.path("id").asText();
                    String title = node.path("title").asText();
                    AlbumInfo album = new AlbumInfo(id, title, null);
                    albums.add(album);
                }
            }
        }
        return albums;
    }
}
