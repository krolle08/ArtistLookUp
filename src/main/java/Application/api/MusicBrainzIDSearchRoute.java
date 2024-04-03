package Application.api;

import Application.service.ArtistContainer.AlbumInfoObj;
import Application.service.ArtistContainer.ArtistInfoObj;
import Application.service.ArtistContainer.WikiInfoObj;
import Application.utils.RestTempUtil;
import Application.utils.URIException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
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

    @GetMapping("/MBArtist/{mbid}")
    public ArtistInfoObj getArtistDataWithmbid(@PathVariable String mbid) throws URIException {
        URI uri = createURI(mbid);
        ResponseEntity<String> response = getResponse(uri);
        if (RestTempUtil.isBodyEmpty(response, null)) {
            logger.info("No response was given on the provided URI: " + uri + " make sure that the search type and criteria are correct");
            return new ArtistInfoObj();
        }
        ArtistInfoObj artistInfoObj = extractData(response, mbid);
        artistInfoObj.setmBID(mbid);
        return artistInfoObj;
    }

    private URI createURI(String mBid) throws URIException {
        try {
            String fullPath = RestTempUtil.constructUri(mBid, queryTypeArtist, protocol, schemeDelimiter, host,
                    port, pathPrefix, version, pathPostFix).toString();
            return new URI(fullPath);
        } catch (URISyntaxException e) {
            logger.severe("Failed to create URI: " + e.getMessage());
            throw new URIException("Failed to create URI: " + e.getMessage());
        }
    }


    private ResponseEntity<String> getResponse(URI uri) throws RuntimeException {
        RestTemplate restTemplate = RestTempUtil.restTemplate(host, port);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        return response;
    }

    private ArtistInfoObj extractData(ResponseEntity response, String mbid) {
        ObjectMapper mapper = new ObjectMapper();
        ArtistInfoObj artistInfoObj = new ArtistInfoObj();
        try {
            JsonNode rootNode = mapper.readTree(response.getBody().toString());
            String name = extractName(rootNode, mbid);
            WikiInfoObj wikiInfoObj = extractwikiData(rootNode);
            List<AlbumInfoObj> albums = extractCoverIdAndTitle(rootNode);
            artistInfoObj = new ArtistInfoObj(name, null, wikiInfoObj, albums);
            artistInfoObj.setmBStatusCode(response.getStatusCode().value());
            return artistInfoObj;
        } catch (JsonProcessingException e) {
            logger.severe("A problem occurred when mapping the response: " + e.getMessage());
            e.printStackTrace();
            return artistInfoObj;
        }
    }

    private String extractName(JsonNode rootNode, String mbid) {
        JsonNode nameNode = rootNode.get("name");
        if (nameNode != null && !nameNode.isNull()) {
            return nameNode.asText();
        } else {
            logger.warning("No information available for the provided input neither as an Artist or MusicBrainz ID " +
                    mbid);
            return null;
        }
    }

    private WikiInfoObj extractwikiData(JsonNode rootNode) {
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
        return new WikiInfoObj(wikidataResult, wikipediaResult);
    }

    private String extractWikiDataTerm(String wikiData) {
        return wikiData.replaceAll("^.*?/(Q\\d+)$", "$1");
    }

    private List<AlbumInfoObj> extractCoverIdAndTitle(JsonNode rootNode) {
        List<AlbumInfoObj> albums = new ArrayList<>();
        JsonNode relations = rootNode.get("release-groups");
        if (!relations.isNull() && relations.isArray()) {
            for (JsonNode node : relations) {
                if (node.path("primary-type").asText().equals("Album")) {
                    String albumid = node.path("id").asText();
                    String title = node.path("title").asText();
                    AlbumInfoObj album = new AlbumInfoObj(albumid, title);
                    albums.add(album);
                }
            }
        }
        return albums;
    }
}
