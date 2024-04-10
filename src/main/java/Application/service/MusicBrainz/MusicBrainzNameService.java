package Application.service.MusicBrainz;

import Application.api.MusicBrainzNameSearchRoute;
import Application.service.Artist.ArtistInfoObj;
import Application.service.InvalidArtistException;
import Application.utils.RestTempUtil;
import Application.utils.TypeOfSearchEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MusicBrainzNameService {
    private static final Logger logger = LoggerFactory.getLogger(MusicBrainzNameService.class.getName());

    @Autowired
    MusicBrainzNameSearchRoute musicBrainzNameSearchRoute;

    public ArtistInfoObj getMBIdData(Map<String, String> searchParam) throws IllegalArgumentException, InvalidArtistException {
        ResponseEntity<String> response = musicBrainzNameSearchRoute.doGetResponse(searchParam);
        if (RestTempUtil.isBodyEmpty(response)) {
            logger.info("No response on the provided searchparameters: {}, {}.", searchParam.entrySet().iterator().next().getKey(),
                    searchParam.entrySet().iterator().next().getValue());
            throw new InvalidArtistException("No response on the provided search value: " + searchParam.entrySet().iterator().next().getValue());
        }
        return extractDataAndPopulateObj(response, searchParam.get(TypeOfSearchEnum.ARTIST.getSearchType()));
    }

    public ArtistInfoObj extractDataAndPopulateObj(ResponseEntity responseEntity, String searchParam) {
        ArtistInfoObj artistInfoObj = new ArtistInfoObj();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(responseEntity.getBody().toString());
            String artistName = "";

            Iterator<JsonNode> annotationIterator = rootNode.path("annotations").elements();
            boolean foundUUID = false; // Flag to track if UUID is found

            while (annotationIterator.hasNext() && !foundUUID) {
                JsonNode annotation = annotationIterator.next();
                if (annotation.path("type").asText().equals("artist")) {
                    String text = annotation.get("text").asText();
                    String uuid = extractUUIDForTerm(text, searchParam);
                    if(uuid != null){
                        artistInfoObj.setmBID(uuid);
                        artistInfoObj.setName(searchParam);
                        artistInfoObj.setmBStatusCode(responseEntity.getStatusCodeValue());
                        foundUUID = true;
                    }
                }
            }
            return artistInfoObj;
        } catch (JsonProcessingException e) {
            logger.warn("A problem occurred mapping the response: " + e.getMessage());
            e.printStackTrace();
            return artistInfoObj;
        }
    }

    public static String extractUUIDForTerm(String text, String searchTerm) {
        String regex = "\\[http://musicbrainz.org/artist/([a-fA-F0-9\\-]+)\\.html\\|" + Pattern.quote(searchTerm) + "]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String extractUUID(String text) {
        String regex = "\\[artist:([a-fA-F0-9\\-]+)\\|.*\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
