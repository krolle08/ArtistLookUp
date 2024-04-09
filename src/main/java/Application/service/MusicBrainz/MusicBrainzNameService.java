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

import java.net.URI;
import java.util.Iterator;
import java.util.Map;

@Service
public class MusicBrainzNameService {
    private static final Logger logger = LoggerFactory.getLogger(MusicBrainzNameService.class.getName());

    @Autowired
    MusicBrainzNameSearchRoute musicBrainzNameSearchRoute;

    public ArtistInfoObj getMBIdData(Map<String, String> searchParam) throws IllegalArgumentException, InvalidArtistException {
        URI uri = musicBrainzNameSearchRoute.getUri(searchParam);
        ResponseEntity<String> response = musicBrainzNameSearchRoute.doGetResponse(uri);
        if (RestTempUtil.isBodyEmpty(response, "artists")) {
            logger.warn("No response was given on the provided URI: " + uri + " . Make sure that search type and " +
                    "search parameter are correct " + searchParam.entrySet().iterator().next().getKey() + ", " +
                    searchParam.entrySet().iterator().next().getValue());
            throw new InvalidArtistException("No response was given on the provided search value: " + searchParam.entrySet().iterator().next().getValue());
        }
        return extractDataAndPopulateObj(response, searchParam.get(TypeOfSearchEnum.ARTIST.getSearchType()));
    }
    public ArtistInfoObj extractDataAndPopulateObj(ResponseEntity responseEntity, String searchParam){
        ArtistInfoObj artistInfoObj = new ArtistInfoObj();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(responseEntity.getBody().toString());
            String mbid = "";
            String artistName = "";
            int highestScore = Integer.MIN_VALUE;

            Iterator<JsonNode> annotationIterator = rootNode.path("artists").elements();

            while (annotationIterator.hasNext()) {
                JsonNode annotation = annotationIterator.next();
                int score = annotation.path("score").asInt();
                String type = annotation.path("name").asText();

                if (searchParam.equalsIgnoreCase(type) && score > highestScore) {
                    highestScore = score;
                    mbid = annotation.path("id").asText();
                    artistName = type;
                }
            }
            artistInfoObj.setName(artistName);
            artistInfoObj.setmBID(mbid);
            artistInfoObj.setmBStatusCode(responseEntity.getStatusCodeValue());
            return artistInfoObj;}
        catch (JsonProcessingException e){
            logger.warn("A problem occurred mapping the response: " + e.getMessage());
            e.printStackTrace();
            return artistInfoObj;
        }
    }
}
