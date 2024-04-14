package Application.service.MusicBrainz;

import Application.api.MusicBrainzNameSearchRoute;
import Application.service.Artist.ArtistInfoObj;
import Application.utils.LoggingUtility;
import Application.utils.TypeOfSearchEnum;
import Application.utils.UserInputUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MusicBrainzNameService {

    @Autowired
    MusicBrainzNameSearchRoute musicBrainzNameSearchRoute;

    public ArtistInfoObj getMBId(Map<String, String> searchParam) {
        ResponseEntity<String> response = musicBrainzNameSearchRoute.doGetResponse(searchParam);
        ArtistInfoObj artistInfoObj = extractDataAndPopulateObj(response, searchParam.get(TypeOfSearchEnum.ARTIST.getSearchType()));
        return artistInfoObj;
    }

    public ArtistInfoObj extractDataAndPopulateObj(ResponseEntity responseEntity, String searchParam) {
        ArtistInfoObj artistInfoObj = new ArtistInfoObj();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(responseEntity.getBody().toString());
        } catch (JsonProcessingException e) {
            LoggingUtility.warn("A problem occurred when mapping the response: " + e.getMessage());
            e.printStackTrace();
            return artistInfoObj;
        }
            Iterator<JsonNode> annotationIterator = rootNode.path("annotations").elements();
            boolean foundUUID = false; // Flag to track if UUID is found
            String text;
            String mbid = "";
            while (annotationIterator.hasNext() && !foundUUID) {
                JsonNode annotation = annotationIterator.next();
                boolean isNamePresent = annotation.path("name").asText().equalsIgnoreCase(UserInputUtil.sanitizeInput(searchParam));
                if (isNamePresent && annotation.path("type").asText().equals("artist")) {
                    mbid = annotation.path("id").asText();
                    foundUUID = true;
                } else if (annotation.path("type").asText().equals("artist")) {
                    text = annotation.get("text").asText();
                    String uuid = extractUUIDForTerm(text, searchParam);
                    if (uuid != null) {
                        mbid = uuid;
                        foundUUID = true;
                    }
                }
            }
            artistInfoObj.setmBID(mbid);
            artistInfoObj.setName(searchParam);
            artistInfoObj.setmBStatusCode(responseEntity.getStatusCodeValue());
            return artistInfoObj;
        }


    public static String extractUUIDForTerm(String text, String searchTerm) {
        String regex = "\\[artist:([a-fA-F0-9\\-]+)\\|" + Pattern.quote(searchTerm) + "]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            // Check if the match contains both UUID and search term
            if (matcher.group(1) != null && matcher.group(1).length() > 0) {
                return matcher.group(1);
            }
        }
        return null;
    }
}

