package Application.service.MusicBrainz;

import Application.api.MusicBrainzIDSearchRoute;
import Application.service.Artist.AlbumInfoObj;
import Application.service.Artist.ArtistInfoObj;
import Application.service.Artist.WikiInfoObj;
import Application.utils.LoggingUtility;
import Application.utils.RestTempUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MusicBrainzIdService {

    @Autowired
    MusicBrainzIDSearchRoute musicBrainzIDSearchRoute;


    public ArtistInfoObj getMBData(String mbid) {
        ArtistInfoObj artistInfoObj = new ArtistInfoObj();
        ResponseEntity<String> response = musicBrainzIDSearchRoute.doGetResponse(mbid);
        if (RestTempUtil.isBodyEmpty(response)) {
            LoggingUtility.info("No body was provided on mbid: " + mbid + " make sure that the search " +
                    "type and search criteria are correct");
            return artistInfoObj;
        } else {
            artistInfoObj = extractData(response, mbid);
            artistInfoObj.setmBStatusCode(response.getStatusCodeValue()); // Relevant for future development, handling bad responses
            return artistInfoObj;
        }
    }

    public ArtistInfoObj extractData(ResponseEntity response, String mbid) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        ArtistInfoObj artistInfoObj = new ArtistInfoObj();
        try {
            rootNode = mapper.readTree(response.getBody().toString());
        } catch (JsonProcessingException e) {
            String errorMessage = "An error occurred when mapping the response: " + e.getMessage();
            LoggingUtility.error(errorMessage);
            e.printStackTrace();
            return artistInfoObj;
        }
            String name = extractName(rootNode, mbid);
            WikiInfoObj wikiInfoObj = extractwikiData(rootNode);
            List<AlbumInfoObj> albums = extractCoverIdAndTitle(rootNode);
            artistInfoObj.setName(name);
            artistInfoObj.setmBID(mbid);
            artistInfoObj.setWikiInfo(wikiInfoObj);
            artistInfoObj.setAlbums(albums);
            artistInfoObj.setmBStatusCode(response.getStatusCode().value()); // Relevant for future development, handling bad responses
            return artistInfoObj;
    }

    private String extractName(JsonNode rootNode, String mbid) {
        JsonNode nameNode = rootNode.get("name");
        if (nameNode != null && !nameNode.isNull()) {
            return nameNode.asText();
        } else {
            LoggingUtility.warn("No information available for the provided input neither as an Artist or MusicBrainz ID " +
                    mbid);
            return null;
        }
    }

    private WikiInfoObj extractwikiData(JsonNode rootNode) {
        String wikipediaResult = "";
        String wikidataResult = "";

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
