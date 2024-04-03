package Application.service;

import Application.utils.Json;
import Application.utils.ScannerWrapper;
import Application.utils.UserInputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * I include the Scanner in the constructor when the class relies on user input and it is unlikely that
 * the scanner needs to be swapped out for another Scanner instance during the class's lifetime. It simplifies method
 * signatures and encapsulates the dependency, resulting in cleaner code.
 */
@Component
public class GetDataImpl {
    private static final Logger logger = LoggerFactory.getLogger(GetDataImpl.class);
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000; // 1 second delay between retries
    private ScannerWrapper scannerWrapper;

    @Autowired
    public GetDataImpl(ScannerWrapper scannerWrapper) {
        this.scannerWrapper = scannerWrapper;
    }

    @Autowired
    private SearchArtistService searchArtistService;

    public void run() {
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try {
                MusicEntityObj entity = new MusicEntityObj();
                Map<String, String> searchParam = UserInputUtil.getTypeOfSearch(scannerWrapper);
                if (searchParam.isEmpty()) {
                    logger.warn("No searchparams was available, please try again");
                    retryCount++;
                    continue;
                }
                scannerWrapper.close();
                TypeOfSearchEnum typeOfSearch = TypeOfSearchEnum.convertToEnum(searchParam.entrySet().iterator().next().getKey());
                switch (typeOfSearch) {
                    case AREA:
                        break;
                    case ARTIST:
                        entity.setArtistInfo(searchArtistService.searchArtist(searchParam));
                        break;
                    case EVENT:
                        break;
                    case GENRE:
                        break;
                    case INSTRUMENT:
                        break;
                    case LABEL:
                        break;
                    case PLACE:
                        break;
                    case RECORDING:
                        break;
                    case RELEASE_GROUP:
                        break;
                    case URL:
                        break;
                    case WORK:
                        break;
                    default:
                        break;
                }
                if (entity.getArtistInfo().isEmpty()) {
                    System.out.println("No results on: " + searchParam.get(typeOfSearch.toString()) + " as an/a: " + typeOfSearch);
                }
                String jsonResponse = Json.createJsonResponse(entity);
                System.out.println(jsonResponse);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("An error occurred: " + e.getMessage() + " please try again. Sorry for the inconvenience.");
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
        logger.warn("Operation failed after " + MAX_RETRIES + " retries. Exiting."); // Operation failed
    }
}

