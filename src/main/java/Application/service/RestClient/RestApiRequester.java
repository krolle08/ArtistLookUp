package Application.service.RestClient;

import Application.service.Area.SearchAreaService;
import Application.service.Artist.SearchArtistService;
import Application.service.InvalidArtistException;
import Application.service.InvalidSearchRequestException;
import Application.service.MusicEntityObj;
import Application.utils.Json;
import Application.utils.TypeOfSearchEnum;
import Application.utils.UserInputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * API Requester for HTTP request, accepts both Name and MusicBrainz ID
 */
@RestController
public class RestApiRequester {
    private static final Logger logger = LoggerFactory.getLogger(RestApiRequester.class.getName());

    private final SearchArtistService searchArtistService;
    private final SearchAreaService searchAreaService;

    @Autowired
    public RestApiRequester(SearchArtistService searchArtistService, SearchAreaService searchAreaService) {
        this.searchArtistService = searchArtistService;
        this.searchAreaService = searchAreaService;
        logger.info("RestApiRequester bean initialized");
    }

    @GetMapping("/artist/{id}")
    public ResponseEntity<String> getArtistRequest(@PathVariable String id) {
        try {
            Map<String, String> searchParam = new HashMap<>();
            MusicEntityObj entity;
            searchParam.put(TypeOfSearchEnum.ARTIST.getSearchType(), id);

            UserInputUtil.IsSearchRequestAllowed(searchParam);

            entity = searchArtistService.getData(searchParam);

            if (entity.getArtistInfoObj() == null || entity.getArtistInfoObj().isEmpty()) {
                throw new InvalidArtistException("Invalid artist: " + id);
            }
            logger.info("Request Completed for: " + id);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Request", "Completed with success");
            return ResponseEntity.ok().headers(headers).body(Json.createJsonResponse(entity));

          //  return ResponseEntity.ok(Json.createJsonResponse(entity));
        } catch (InvalidSearchRequestException e) {
            e.printStackTrace();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Reason", "Invalid search value");
            return ResponseEntity.badRequest().headers(headers).body(e.getMessage());
        } catch (InvalidArtistException e) {
            e.printStackTrace();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Reason", "Invalid artist");
            return ResponseEntity.badRequest().headers(headers).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Reason", "Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(e.getMessage());
        }
    }

    @GetMapping("/area/{id}")
    public ResponseEntity<String> getAreaRequest(@PathVariable String id) {
        Map<String, String> searchParam = new HashMap<>();
        searchParam.put(TypeOfSearchEnum.AREA.getSearchType(), id);
        MusicEntityObj entity = searchAreaService.getData(searchParam);
        if (entity.getArtistInfoObj().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid area ID: " + id);
        }
        return ResponseEntity.ok(Json.createJsonResponse(entity));
    }

}
