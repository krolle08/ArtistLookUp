package Application.service.RestClient;

import Application.service.Area.SearchAreaService;
import Application.service.Artist.SearchArtistService;
import Application.service.MusicEntityObj;
import Application.utils.Json;
import Application.utils.LoggingUtility;
import Application.utils.TypeOfSearchEnum;
import Application.utils.UserInputUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    private final SearchArtistService searchArtistService;
    private final SearchAreaService searchAreaService;

    @Autowired
    public RestApiRequester(SearchArtistService searchArtistService, SearchAreaService searchAreaService) {
        this.searchArtistService = searchArtistService;
        this.searchAreaService = searchAreaService;
        LoggingUtility.info("RestApiRequester bean initialized");
    }

    @GetMapping("/artist/{id}")
    public ResponseEntity<String> getArtistRequest(@PathVariable String id) {
            Map<String, String> searchParam = new HashMap<>();
            MusicEntityObj entity = new MusicEntityObj();
            searchParam.put(TypeOfSearchEnum.ARTIST.getSearchType(), id);

            boolean isSearchAllowed = UserInputUtil.IsSearchRequestAllowed(searchParam);
            if(isSearchAllowed) {
                entity = searchArtistService.getData(searchParam);
            }
            if (entity.getArtistInfoObj() == null || entity.getArtistInfoObj().isEmpty()) {
                LoggingUtility.warn("Invalid artist: " + id);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Reason", "Invalid artist");
                return ResponseEntity.badRequest().headers(headers).body("Invalid search value: " + id);
            }
            LoggingUtility.info("Request Completed for: " + searchParam.entrySet().iterator().next().getValue());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Request", "Completed with success");
            return ResponseEntity.ok().headers(headers).body(Json.createJsonResponse(entity));
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
