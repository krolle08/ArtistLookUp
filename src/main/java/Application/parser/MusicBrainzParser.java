package Application.parser;

import Application.model.Album;
import Application.model.response.ArtistResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MusicBrainzParser {

    public static ArtistResponse parseResponse(String responseBody) throws Exception {
        ArtistResponse artistResponse = new ArtistResponse();
        artistResponse.setmBID(parseMusicBrainzId(responseBody));
        artistResponse.setAlbums(parseAlbums(responseBody));
        artistResponse.setWikidataId(parseWikidataId(responseBody));
        return artistResponse;
    }

    public static String parseMusicBrainzId(String responseBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);
        String mBId = rootNode.get("id").asText();
        return mBId;
    }


    public static List<Album> parseAlbums(String responseBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);

        List<Album> parsedAlbums = new ArrayList<>();
        JsonNode albumsNode = rootNode.path("release-groups");

        for (JsonNode albumNode : albumsNode) {
            JsonNode primaryTypeNode = albumNode.path("primary-type");
            if (!primaryTypeNode.isTextual() || !primaryTypeNode.asText().equals("Album")) {
                continue;
            }

            // Extract album title and id
            String albumTitle = albumNode.path("title").asText(null);
            String albumId = albumNode.path("id").asText(null);

            // Handle missing values (title or id) if needed
            if (albumTitle != null && albumId != null) {
                parsedAlbums.add(new Album(albumId, albumTitle));
            }
        }

        return parsedAlbums;
    }

    private static String parseWikidataId(String responseBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);

        // Assuming "relations" is an array in the root node
        JsonNode relationsNode = rootNode.path("relations");
        if (!relationsNode.isArray()) {
            // Handle case where "relations" is not an array
            return null; // Or throw an exception
        }

        for (JsonNode relationNode : relationsNode) {
            // Check for "type" and its value
            JsonNode typeNode = relationNode.path("type");
            if (!typeNode.isTextual() || !typeNode.asText().equals("wikidata")) {
                continue; // Skip non-wikidata relations
            }

            // Extract "url" node and check if it's an object
            JsonNode urlNode = relationNode.path("url");
            if (!urlNode.isObject()) {
                continue; // Skip if "url" is not an object
            }

            // Extract "resource" from "url" node and check if it's text
            JsonNode resourceNode = urlNode.path("resource");
            if (!resourceNode.isTextual()) {
                continue; // Skip if "resource" is not text
            }

            String resource = resourceNode.asText(null);
            if (resource != null) {
                return resource.substring(resource.indexOf("Q"));
            }
        }

        // No wikidata ID found
        return null;
    }

    // Helper methods for safer value extraction
    private static Map<String, Object> extractMapValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        } else {
            return null;
        }
    }

    private static String extractStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof String) {
            return (String) value;
        } else {
            return null;
        }
    }
}
