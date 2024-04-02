package Application.utils;

import Application.service.AlbumInfo;
import Application.service.GetDataImpl;
import Application.service.MusicEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

public class Json {

    private static final Log log = LogFactory.getLog(GetDataImpl.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String createJsonResponse(MusicEntity entity) {
        // Format the dataContainer data as JSON
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("mbid", entity.getArtistInfo().getmBID());
        String description = entity.getArtistInfo().getWikiInfo().getDescription();

        rootNode.put("description", formatDescription(description));

        ArrayNode albumsNode = rootNode.putArray("albums");
        for (AlbumInfo album : entity.getArtistInfo().getAlbums()) {
            ObjectNode albumNode = JsonNodeFactory.instance.objectNode();
            albumNode.put("title", album.getTitle());
            albumNode.put("id", album.getiD());
            albumNode.put("image", album.getImageURL());
            albumsNode.add(albumNode);
        }

        try {
            // String formatted_response = objectMapper.writeValueAsString(response_data);
            return writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            log.error("Error occured during processing the rootNode into the desired jsonformat");
            e.printStackTrace();
            return "{}";
        }
    }
    public static String writeValueAsString(JsonNode rootNode) throws JsonProcessingException {
        return mapper.writeValueAsString(rootNode);
    }
    public static String formatDescription(String description) {
        // Remove HTML tags
        description = description.replaceAll("\\<.*?\\>", "");
        return description;
    }
}
