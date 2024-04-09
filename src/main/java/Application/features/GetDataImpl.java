package Application.features;

import Application.service.Area.SearchAreaService;
import Application.service.Artist.SearchArtistService;
import Application.service.DataController;
import Application.service.InvalidArtistException;
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
    private final SearchArtistService searchArtistService;
    private final SearchAreaService searchAreaService;

    private final UserInputUtil userInputUtil;

    /**
     * Responsible for handling terminal user inputs. Not implemented yet.
     * @param userInputUtil       Utility class for handling user input.
     * @param searchArtistService Service for searching artists.
     * @param searchAreaService Service for searching areas.
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
                if (searchParam.isEmpty()) {
                    logger.warn("No search parameters were available. Please try again.");
                    retryCount++;
                    continue;
                }
                TypeOfSearchEnum typeOfSearch = TypeOfSearchEnum.convertToEnum(searchParam.entrySet().iterator().next().getKey());
                switch (typeOfSearch) {
                    case AREA:
                        //Example of how future development could look like
                        searchAreaService.getData(searchParam);
                        break;
                    case ARTIST:
                        searchArtistService.getData(searchParam);
                        break;
                    default:
                        logger.warn("Unsupported type of search: {}", typeOfSearch);
                        break;
                }
                return;
            } catch (RuntimeException | InvalidArtistException e) {
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
}

