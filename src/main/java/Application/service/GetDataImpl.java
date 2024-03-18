package Application.service;

import Application.api.*;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * I include the Scanner in the constructor when the class relies on user input and it is unlikely that
 * the scanner needs to be swapped out for another Scanner instance during the class's lifetime. It simplifies method
 * signatures and encapsulates the dependency, resulting in cleaner code.
 */
public class GetDataImpl {
    private Log log = LogFactory.getLog(GetDataImpl.class);
    private final Map<String, String> searchTypes;
    private final MusicBrainzNameSearchRoute musicBrainzNameSearchRoute; // Dependency injection for MusicBrainzNameSearchRoute
    private final MusicBrainzIDSearchRoute musicBrainzIDSearchRoute = new MusicBrainzIDSearchRoute();
    private final CoverArtArchiveService coverArtArchiveService = new CoverArtArchiveService();
    private final WikidataSearchRoute wikidataSearchRoute = new WikidataSearchRoute();
    private final WikipediaSearchRoute wikipediaSearchRoute = new WikipediaSearchRoute();
    int typoLimit = 10;
    int consecutiveTypoMistakes = 0;
    private final Scanner scanner;

    public GetDataImpl(Scanner scanner, MusicBrainzNameSearchRoute musicBrainzNameSearchRoute) {
        this.musicBrainzNameSearchRoute = musicBrainzNameSearchRoute;
        this.scanner = scanner;
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

    public void run() throws Exception {
        do {
            Map<String, String> searchParam = getTypeOfSearch();
            Map.Entry<String, String> entry = searchParam.entrySet().iterator().next();
            TypeOfSearchEnum typeOfSearch = TypeOfSearchEnum.convertToEnum(entry.getKey());
            try {
                switch (typeOfSearch) {
                    case AREA:
                        break;
                    case ARTIST:
                        Map<String, String> response = musicBrainzNameSearchRoute.getMBID(searchParam);
                        if (response.isEmpty()) {
                            response.putAll(musicBrainzIDSearchRoute.getDataFromArtist(searchParam.get(TypeOfSearchEnum.ARTIST.toString()))); // Do some logic that can determin what type of search is needed
                        } else {
                            response.putAll(musicBrainzIDSearchRoute.getDataFromArtist(response.get("MBID"))); // Do some logic that can determin what type of search is needed
                        }
                        if (response.isEmpty()) {
                            log.info("No information available for the provided input neither as an Artist name or Music Brainz ID:" + searchParam.get(TypeOfSearchEnum.ARTIST.toString()));
                            return;
                        }
                        if (response.containsKey("wikidata") && !response.containsKey("wikipedia")) {
                            response.putAll(wikidataSearchRoute.getWikidataFromArtist(response.get("wikidataSearchTerm")));
                        }
                        response.putAll(wikipediaSearchRoute.getWikipediadataFromArtist(response.get("enwiki")));
                        response.putAll(coverArtArchiveService.getCovers(response.get("MBID")));
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
                }
            } catch (Exception e) {
                throw new Exception("An internal error occured, please try again. Sorry for the inconvenience");
            }
        } while (endSearch());
        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Make a GET request to /hello endpoint
        ResponseEntity<String> helloResponse = restTemplate.getForEntity("http://localhost:8080/hello", String.class);
        System.out.println("Response from /hello endpoint: " + helloResponse.getBody());

        // Make a GET request to /farvel endpoint
        ResponseEntity<String> farvelResponse = restTemplate.getForEntity("http://localhost:8080/farvel", String.class);
        System.out.println("Response from /farvel endpoint: " + farvelResponse.getBody());
    }

    public Map<String, String> getTypeOfSearch() throws Exception {
        System.out.println("Type in the number corresponding to the type of search you want to perform:\n" +
                "1 Area\n" +
                "2 Artist\n" +
                "3 Event\n" +
                "4 Genre\n" +
                "5 Instrument\n" +
                "6 Label\n" +
                "7 Place\n" +
                "8 Recording\n" +
                "9 Relase Group\n" +
                "10 URL\n" +
                "11 Work\n");

        // Get user input
        String userInputType = scanner.nextLine().trim();

        // Check if the input corresponds to a valid search type
        if (!searchTypes.containsKey(userInputType)) {
            throw new Exception("Invalid search type.");
        }

        System.out.println("What " + searchTypes.get(userInputType) + " do you want to search for?");
        String userInputValue = scanner.nextLine().trim();

        // Return the corresponding search type
        Map<String, String> result = new HashMap<>();
        result.put(searchTypes.get(userInputType), userInputValue);
        return result;
    }

    private String extractMBID(ResponseEntity response) {
        String artistIdSubstring = "";
        // Find the start index of "[artist:"
        int startIndex = response.toString().indexOf("[artist:");
        if (startIndex != -1) {
            // Find the end index of "]"
            int endIndex = response.toString().indexOf("|", startIndex);
            if (endIndex != -1) {
                // Extract the substring between "[artist:" and "]"
                artistIdSubstring = response.toString().substring(startIndex + "[artist:".length(), endIndex);
                // Now artistIdSubstring should contain "5b11f4ce-a62d-471e-81fc-a69a8278c7da"
            } else {
                // Handle case where "|" is not found
                System.out.println("Closing bracket '|' not found");
            }
        } else {
            // Handle case where "[artist:" is not found
            System.out.println("Substring '[artist:' not found");
        }
        return artistIdSubstring;
    }

    private boolean endSearch() {
        while (typoLimit > consecutiveTypoMistakes) {
            System.out.println("Want to make a new search? (Yes/No)");
            String input = scanner.nextLine();
            if (input.equals("Yes")) {
                return false;
            } else if (input.equals("No")) {
                return true;
            } else {
                consecutiveTypoMistakes++;
            }
        }
        System.out.println("Too many consecutive typos. Terminating program.");
        scanner.close();
        return true;
    }
}

