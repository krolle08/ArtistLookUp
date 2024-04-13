package Application.utils;

import Application.service.Artist.AlbumInfoObj;
import Application.service.MusicEntityObj;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Json {

    private static final ObjectMapper mapper = new ObjectMapper();
    public static String createJsonResponse(MusicEntityObj entity) {
        if(entity.getArtistInfoObj().getmBID() == null || entity.getArtistInfoObj().getmBID().isEmpty()){
            String warn = "No information found on: " + entity.getArtistInfoObj().getName();
            LoggingUtility.warn(warn);
            return warn;
        }

        // Format the dataContainer data as JSON
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("mbid", entity.getArtistInfoObj().getmBID());
        String description = entity.getArtistInfoObj().getWikiInfo().getDescription();

        rootNode.put("description", formatDescription(description));

        ArrayNode albumsNode = rootNode.putArray("albums");
        for (AlbumInfoObj album : entity.getArtistInfoObj().getAlbums()) {
            ObjectNode albumNode = JsonNodeFactory.instance.objectNode();
            albumNode.put("title", album.getTitle());
            albumNode.put("id", album.getAlbumId());
            albumNode.put("image", album.getImageURL());
            albumsNode.add(albumNode);
        }
        return writeValueAsString(rootNode);
        }

    private static String writeValueAsString(JsonNode rootNode) {
        String output;
        try {
            output = mapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e){
            String errorMessage = "Error occured during processing the rootNode into the desired jsonformat. " + e.getMessage();
            LoggingUtility.error(errorMessage);
            e.printStackTrace();
            throw new RuntimeException(errorMessage, e);
        }
        return output;
    }
    private static String formatDescription(String description) {
        // Remove HTML tags
        description = description.replaceAll("\\<.*?\\>", "");
        return description;
    }
}
