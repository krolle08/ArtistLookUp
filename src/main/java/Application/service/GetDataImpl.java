package Application.service;

import Application.api.MusicBrainzNameSearchRoute;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import Application.api.CoverArtArchiveService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static Application.service.TypeOfSearchEnum.isSearchTypePossible;


public class GetDataImpl {

    private Map<String, String> searchTypes;
    int typoLimit = 10;
    int consecutiveTypoMistakes = 0;
    MusicBrainzNameSearchRoute musicBrainzNameSearchRoute = new MusicBrainzNameSearchRoute();
    Scanner scanner = new Scanner(System.in);
    String paramValue;
    String paramName;

    public GetDataImpl() {
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
            TypeOfSearchEnum typeOfSearch = TypeOfSearchEnum.convertToEnum(paramValue);
            try {
                switch (typeOfSearch) {
                    case AREA:
                        break;
                    case ARTIST:
                        ResponseEntity response = musicBrainzNameSearchRoute.getMBID(searchParam);
                        String mBID = response.getBody()


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




            String artist = scanner.nextLine();

            // Create an instance of api.YourApplication
            CoverArtArchiveService app = new CoverArtArchiveService();

            // Call the method to run the application logic
            app.runApplication(artist);
        }
        while (endSearch());


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

            System.out.println("What" + searchTypes.get(userInputType) + "do you want to search for?");
            String userInputValue = scanner.nextLine().trim();

            // Return the corresponding search type
            return Map.of(searchTypes.get(userInputType), userInputValue);
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

