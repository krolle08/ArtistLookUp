package Application;

import Application.service.GetDataImpl;
import Application.utils.ScannerWrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YourApplication {
    static int typoLimit = 10;
    static int consecutiveTypoMistakes = 0;

    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
        ScannerWrapper scanner;
        do {
            scanner = new ScannerWrapper();
            GetDataImpl getData = new GetDataImpl(scanner);
            try {
                String test = getData.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } while (endSearch(scanner));
    }

    private static boolean endSearch(ScannerWrapper scanner) {
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
        System.out.println("Too many consecutive typos. Restarting program.");
        return false;
    }
}