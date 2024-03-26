package Application.service;

import Application.api.*;
import Application.utils.ScannerWrapper;
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
    private final Map<String, String> searchTypes;
    private final MusicBrainzNameSearchRoute musicBrainzNameSearchRoute = new MusicBrainzNameSearchRoute(); // Dependency injection for MusicBrainzNameSearchRoute
    private final MusicBrainzIDSearchRoute musicBrainzIDSearchRoute = new MusicBrainzIDSearchRoute();
    private final CoverArtArchiveService coverArtArchiveService = new CoverArtArchiveService();
    private final WikidataSearchRoute wikidataSearchRoute = new WikidataSearchRoute();
    private final WikipediaSearchRoute wikipediaSearchRoute = new WikipediaSearchRoute();
    int typoLimit = 10;
    int consecutiveTypoMistakes = 0;
    int typos = 0;
    private final ScannerWrapper scannerWrapper;
    private Map<String, String> covers = new HashMap<>();
    private Map<String, Object> response = new HashMap<>();

    public GetDataImpl(ScannerWrapper scannerWrapper) {
        this.scannerWrapper = scannerWrapper;
        // Initialize the map with mappings from numbers and corresponding search types
        searchTypes = new HashMap<>();
        searchTypes.put("1", "Area");
        searchTypes.put("2", "Artist");
        searchTypes.put("3", "Event");
        searchTypes.put("4", "Genre");
        searchTypes.put("5", "Instrument");
        searchTypes.put("6", "Label");
        searchTypes.put("7", "Place");
        searchTypes.put("8", "Recording");
        searchTypes.put("9", "Release Group");
        searchTypes.put("10", "URL");
        searchTypes.put("11", "Work");
    }

    public String run() {
        do {
            Map<String, String> searchParam = getTypeOfSearch();
            Map.Entry<String, String> entry = searchParam.entrySet().iterator().next();
            TypeOfSearchEnum typeOfSearch = TypeOfSearchEnum.convertToEnum(entry.getKey());
            try {
                switch (typeOfSearch) {
                    case AREA:
                        break;
                    case ARTIST:
                        response = musicBrainzNameSearchRoute.getMBID(searchParam);
                        if (!response.containsKey("MBID")) {
                            response.putAll(musicBrainzIDSearchRoute.getDataFromArtist(searchParam.get(TypeOfSearchEnum.ARTIST).toString()));
                        } else {
                            response.putAll(musicBrainzIDSearchRoute.getDataFromArtist(response.get("MBID").toString()));
                        }
                        if (!response.containsKey("name")) {
                            log.info("No information available for the provided input neither as an Artist or Music Brainz ID:" + searchParam.get(TypeOfSearchEnum.ARTIST.toString()));
                            return null;
                        }
                        if (response.containsKey("wikidataSearchTerm") && !response.containsKey("wikipedia")) {
                            response.putAll(wikidataSearchRoute.getWikidataFromArtist(response.get("wikidataSearchTerm").toString()));
                        }
                        response.putAll(wikipediaSearchRoute.getWikipediadataFromArtist(response.get("wikipediaSearchTerm").toString()));
                        // Extract covers map from the response
                        covers.putAll(coverArtArchiveService.getCovers((Map<String, String>) response.get("Covers")));
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
            } catch (RuntimeException | URISyntaxException e) {
                throw new RuntimeException("An internal error occured, please try again. Sorry for the inconvenience");
            }
            displayData();
        } while (endSearch());
        return null;
    }

    public Map<String, String> getTypeOfSearch() {
        System.out.println("Type in the number corresponding to the type of search you want to perform:\n" + "1 Area\n" + "2 Artist\n" + "3 Event\n" + "4 Genre\n" + "5 Instrument\n" + "6 Label\n" + "7 Place\n" + "8 Recording\n" + "9 Relase Group\n" + "10 URL\n" + "11 Work\n");

        // Get user input
        String userInputType = scannerWrapper.nextLine().trim();

        // Check if the input corresponds to a valid search type
        if (!searchTypes.containsKey(userInputType)) {
            log.info("Invalid search type:" + userInputType);
            if (++typos > 10) {
                System.exit(0);
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
                return false;
            } else if (input.equals("No")) {
                return true;
            } else {
                consecutiveTypoMistakes++;
            }
        }
        terminate();
        return true;
    }

    private void terminate() {
        System.out.println("Too many consecutive typos. Restarting search engine.");
        scannerWrapper.close();
    }

    public void displayData() {
        String jsonResponse = createJsonResponse(response, covers);
        System.out.println(jsonResponse);
    }

    public void setResponse(Map<String, Object> response) {
        this.response = response;
    }

    public void setCovers(Map<String, String> covers) {
        this.covers = covers;
    }
}

