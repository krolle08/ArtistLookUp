package Application;

import Application.api.MusicBrainzNameSearchRoute;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import Application.service.GetDataImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
public class YourApplication {
    static int typoLimit = 10;
    static int consecutiveTypoMistakes = 0;

    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
        Scanner scanner = new Scanner(System.in);
        GetDataImpl getData = new GetDataImpl(scanner, new MusicBrainzNameSearchRoute());
        do {
            try {
                getData.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } while (endSearch(scanner));
        scanner.close();
        System.exit(0);
    }

    public static String generateJsonFromMap(Map<String, String> map) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(map);
    }

        private static boolean endSearch(Scanner scanner) {
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
            return true;
        }
}


