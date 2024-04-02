package Application.service;

import Application.api.*;
import Application.utils.InputSearchType;
import Application.utils.ScannerWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static Application.utils.Json.createJsonResponse;


/**
 * I include the Scanner in the constructor when the class relies on user input and it is unlikely that
 * the scanner needs to be swapped out for another Scanner instance during the class's lifetime. It simplifies method
 * signatures and encapsulates the dependency, resulting in cleaner code.
 */
public class GetDataImpl {
    private Log log = LogFactory.getLog(GetDataImpl.class);
    int typoLimit = 10;
    int consecutiveTypoMistakes = 0;
    int typos = 0;
    private final ScannerWrapper scannerWrapper;
    private SearchArtistService searchArtistService;
    private final MusicBrainzNameSearchRoute musicBrainzNameSearchRoute;
    private final MusicBrainzIDSearchRoute musicBrainzIDSearchRoute;
    private final CoverArtArchiveService coverArtArchiveService;
    private final WikidataSearchRoute wikidataSearchRoute;
    private final WikipediaSearchRoute wikipediaSearchRoute;
    private static Map<String, String> searchTypes;

    public GetDataImpl(ScannerWrapper scannerWrapper,
                       MusicBrainzNameSearchRoute musicBrainzNameSearchRoute,
                       MusicBrainzIDSearchRoute musicBrainzIDSearchRoute,
                       CoverArtArchiveService coverArtArchiveService,
                       WikidataSearchRoute wikidataSearchRoute,
                       WikipediaSearchRoute wikipediaSearchRoute) {
        this.scannerWrapper = scannerWrapper;
        this.musicBrainzNameSearchRoute = musicBrainzNameSearchRoute;
        this.musicBrainzIDSearchRoute = musicBrainzIDSearchRoute;
        this.coverArtArchiveService = coverArtArchiveService;
        this.wikidataSearchRoute = wikidataSearchRoute;
        this.wikipediaSearchRoute = wikipediaSearchRoute;
        searchTypes = InputSearchType.getInputTypes();
    }

    public void run() {
        do {
            MusicEntity entity = new MusicEntity();
            Map<String, String> searchParam = getTypeOfSearch();
            if (searchParam.isEmpty()) {
                return;
            }
            String searchType = searchParam.entrySet().iterator().next().getKey();
            TypeOfSearchEnum typeOfSearch = TypeOfSearchEnum.convertToEnum(searchType);
            try {
                switch (typeOfSearch) {
                    case AREA:
                        break;
                    case ARTIST:
                        getArtistSearchService();
                        entity = searchArtistService.searchArtist(searchParam);
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
                displayData(entity);
            } catch (URISyntaxException e) {
                e.getMessage();
                log.warn("An internal error occurred, please try again. Sorry for the inconvenience.");
            } catch (RuntimeException e) {
                System.out.println("There was a typo in your search criteria: " + searchParam.get(searchType) + " please try again.");
                run();
            }
        } while (endSearch());
    }

    private SearchArtistService getArtistSearchService() {
        if (searchArtistService == null) {
            searchArtistService = new SearchArtistService(musicBrainzNameSearchRoute,
                    musicBrainzIDSearchRoute,
                    coverArtArchiveService,
                    wikidataSearchRoute,
                    wikipediaSearchRoute);
        }
        return searchArtistService;
    }

    public Map<String, String> getTypeOfSearch() {
        System.out.println("Type in the number corresponding to the type of search you want to perform:\n" + "1 Area\n" + "2 Artist\n" + "3 Event\n" + "4 Genre\n" + "5 Instrument\n" + "6 Label\n" + "7 Place\n" + "8 Recording\n" + "9 Relase Group\n" + "10 URL\n" + "11 Work\n");

        // Get user input
        String userInputType = scannerWrapper.nextLine().trim();

        // Check if the input corresponds to a valid search type
        if (!searchTypes.containsKey(userInputType)) {
            log.info("Invalid search type:" + userInputType);
            ++typos;
            if (typos > 10) {
                return null;
            }
            getTypeOfSearch();
        }

        System.out.println("What " + searchTypes.get(userInputType) + " do you want to search for?");
        String userInputValue = scannerWrapper.nextLine().trim();

        // Return the corresponding search type
        Map<String, String> result = new HashMap<>();
        result.put(searchTypes.get(userInputType), userInputValue);
        return result;
    }

    public boolean endSearch() {
        while (typoLimit > consecutiveTypoMistakes) {
            System.out.println("Want to make a new search? (Yes/No)");
            String input = scannerWrapper.nextLine();
            if (input.equals("Yes")) {
                return true;
            } else if (input.equals("No")) {
                return false;
            } else {
                consecutiveTypoMistakes++;
                endSearch();
            }
        }
        terminate();
        return false;
    }

    private void terminate() {
        System.out.println("Too many consecutive typos. Restarting search engine.");
        scannerWrapper.close();
    }

    public void displayData(MusicEntity entity) {
        String jsonResponse = createJsonResponse(entity);
        System.out.println(jsonResponse);
    }
}

