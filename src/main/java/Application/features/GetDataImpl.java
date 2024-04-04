package Application.features;

import Application.service.Area.SearchAreaService;
import Application.service.Artist.SearchArtistService;
import Application.service.DataController;
import Application.service.MusicEntityObj;
import Application.utils.Json;
import Application.utils.TypeOfSearchEnum;
import Application.utils.UserInputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Component responsible for retrieving data based on user input and search parameters.
 */
@Component
public class GetDataImpl implements DataController {
    private static final Logger logger = LoggerFactory.getLogger(GetDataImpl.class);
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000; // 1 second delay between retries
    private SearchArtistService searchArtistService;
    private SearchAreaService searchAreaService;

    private UserInputUtil userInputUtil;

    /**
     * @param userInputUtil       Utility class for handling user input.
     * @param searchArtistService Service for searching artists.
     */
    @Autowired
    public GetDataImpl(UserInputUtil userInputUtil, SearchArtistService searchArtistService, SearchAreaService searchAreaService) {
        this.userInputUtil = userInputUtil;
        this.searchArtistService = searchArtistService;
        this.searchAreaService = searchAreaService;
    }
    @Override
    public void run() {
        int retryCount = 0;
        Map<String, String> searchParam= new HashMap<>();
        while (retryCount < MAX_RETRIES) {
            try {
                MusicEntityObj entity = new MusicEntityObj();
                searchParam = userInputUtil.getTypeOfSearch();
                if (searchParam.isEmpty()) {
                    logger.warn("No search parameters were available. Please try again.");
                    retryCount++;
                    continue;
                }
                TypeOfSearchEnum typeOfSearch = TypeOfSearchEnum.convertToEnum(searchParam.entrySet().iterator().next().getKey());
                switch (typeOfSearch) {
                    case AREA:
                        //Example of how future development could look like
                        entity.setAreaInfoObj(searchAreaService.getData(searchParam));
                        break;
                    case ARTIST:
                        entity.setArtistInfoObj(searchArtistService.getData(searchParam));
                        if (entity.getArtistInfoObj().isEmpty()) {
                            logger.info("No results found for: {} as an {}", searchParam.entrySet().iterator().next().getValue(), typeOfSearch);
                            return;
                        }
                        break;
                    default:
                        logger.warn("Unsupported type of search: {}", typeOfSearch);
                        break;
                }
                String jsonResponse = Json.createJsonResponse(entity);
                printJsonResponse(jsonResponse);
                return;
            } catch (RuntimeException e) {
                logger.error("An error occurred: {}, while searching for artist: " + searchParam
                        .get(TypeOfSearchEnum.ARTIST.getSearchType()),e.getMessage());
                e.printStackTrace();
                retryCount++;
                if (retryCount < MAX_RETRIES) {
                    logger.info("Retrying...");
                    try {
                        Thread.sleep(RETRY_DELAY_MS); // Introduce delay before retry
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt(); // Restore interrupted status
                    }
                }
            }
        }
        logger.warn("Operation failed after {} retries.", MAX_RETRIES);
    }

    @Override
    public void printJsonResponse(String jsonResponse) {
        System.out.println(jsonResponse);
    }
}

