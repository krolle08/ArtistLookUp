package Application.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import Application.api.CoverArtArchiveService;

import java.util.Scanner;

import static Application.service.TypeOfSearchEnum.isSearchTypePossible;


public class GetDataImpl {
    private boolean endSearch;
    int typoLimit = 10;
    int consecutiveTypoMistakes = 0;
    Scanner scanner = new Scanner(System.in);
    String type;

    public void run() throws Exception {
        do {
            if (istypeOfSearchPossible()) {
                continue;
            }

            TypeOfSearchEnum typeOfSearch = TypeOfSearchEnum.convertToEnum(type);
            try {
                switch (typeOfSearch) {
                    case AREA:
                        break;
                    case ARTIST:
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
            endSearch(scanner);
        }
        while (endSearch);


        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Make a GET request to /hello endpoint
        ResponseEntity<String> helloResponse = restTemplate.getForEntity("http://localhost:8080/hello", String.class);
        System.out.println("Response from /hello endpoint: " + helloResponse.getBody());

        // Make a GET request to /farvel endpoint
        ResponseEntity<String> farvelResponse = restTemplate.getForEntity("http://localhost:8080/farvel", String.class);
        System.out.println("Response from /farvel endpoint: " + farvelResponse.getBody());
    }

    private boolean istypeOfSearchPossible() throws Exception {
        System.out.println("What do you want to search for?\n" +
                "1. Area\n" +
                "2. Artist\n" +
                "3. Event\n" +
                "4. Genre\n" +
                "5. Instrument\n" +
                "6. Label\n" +
                "7. Place\n" +
                "8. Recording\n" +
                "9. Relase Group\n" +
                "10. URL\n" +
                "11. Work\n");

        type = scanner.nextLine();

        if (TypeOfSearchEnum.isSearchTypePossible(type)) {
            System.out.println("The type of search is not possible, please type in the type of search as presentend in the list.");
            return true;
        }
        return false;
    }

    private void endSearch(Scanner scanner) {
        while (typoLimit > consecutiveTypoMistakes) {
            System.out.println("Want to make a new search? (Yes/No)");
            String input = scanner.nextLine();
            if (input.equals("Yes")) {
                endSearch = false;
            } else if (input.equals("No")) {
                endSearch = true;
            } else {
                consecutiveTypoMistakes++;
            }
        }
        System.out.println("Too many consecutive typos. Terminating program.");
        scanner.close();
    }
}

