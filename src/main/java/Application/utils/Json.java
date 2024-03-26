package Application.utils;

import Application.service.GetDataImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Json {

    private static final Log log = LogFactory.getLog(GetDataImpl.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String createJsonResponse(Map<String, Object> dataContainer, Map<String, String> coverImageUrls) {
        List<Album> albums = new ArrayList<>();
        albums.addAll(addCovers(coverImageUrls, dataContainer));

        // Format the dataContainer data as JSON
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("mbid", dataContainer.get("MBID").toString());
        rootNode.put("description", dataContainer.get("description").toString());

        ArrayNode albumsNode = rootNode.putArray("albums");
        for (Album album : albums) {
            ObjectNode albumNode = JsonNodeFactory.instance.objectNode();
            albumNode.put("title", album.getTitle());
            albumNode.put("id", album.getId());
            albumNode.put("image", album.getImageUrl());
            albumsNode.add(albumNode);
        }

        try {
            // String formatted_response = objectMapper.writeValueAsString(response_data);
            return writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public static List<Album> addCovers(Map<String, String> covers, Map<String, Object> albumTitle) {
        List<Album> result = new ArrayList<>();
        Map<String, String> titles = getCoversAsMap(albumTitle);
        for (Map.Entry<String, String> coverEntry : covers.entrySet()) {
            String title = findTitleForCoverId(titles, coverEntry.getValue());
            if (title != null) {
                result.add(new Album(title, coverEntry.getKey(), coverEntry.getValue()));
            } else {
                log.warn("No title found for the provided cover ID: " + coverEntry.getValue());
            }
        }
        return result;
    }

    private static String findTitleForCoverId(Map<String, String> titles, String coverId) {
        for (Map.Entry<String, String> titleEntry : titles.entrySet()) {
            if (coverId.equals(titleEntry.getValue())) {
                return titleEntry.getKey();
            }
        }
        return null; // No title found for the given cover ID
    }

    private static Map<String, String> getCoversAsMap(Map<String, Object> albumTitle) {
        Map<String, String> titles = new HashMap<>();
        if (albumTitle != null && albumTitle.containsKey("Covers")) {
            Object coversObject = albumTitle.get("Covers");
            if (coversObject instanceof Map) {
                titles.putAll((Map<String, String>) coversObject);
            }
        }
        return titles;
    }

    public static String writeValueAsString(JsonNode rootNode) throws JsonProcessingException {
        return mapper.writeValueAsString(rootNode);
    }

    public static class Album {
        private String title;
        private String id;
        private String imageUrl;

        public Album(String title, String id, String imageUrl) {
            this.title = title;
            this.id = id;
            this.imageUrl = imageUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getId() {
            return id;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
