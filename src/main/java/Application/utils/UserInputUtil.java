package Application.utils;

import Application.service.InvalidSearchRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserInputUtil {
    private static final Logger logger = LoggerFactory.getLogger(UserInputUtil.class);
    private static final int MAX_TYPING_ERRORS = 10;
    private static final String YES_OPTION = "Yes";
    private static final String NO_OPTION = "No";
    private final ScannerWrapper scannerWrapper;

    @Autowired
    public UserInputUtil(ScannerWrapper scannerWrapper) {
        this.scannerWrapper = scannerWrapper;
    }
    public static void IsSearchRequestAllowed(Map<String, String> searchRequest) throws InvalidSearchRequestException {
        Map<String, String> searchTypes = SearchTypeUtil.getInputTypes();
        String searchType = searchRequest.entrySet().iterator().next().getKey();

        // Check if the input corresponds to a valid search type
        if (searchTypes.containsValue(searchType)) {
            String searchValue = searchRequest.entrySet().iterator().next().getValue();
            if (searchValue.isEmpty() || !containsAlphanumeric(searchValue)) {
                logger.info("Empty and/or the provided search value is not possible: " + searchValue);
                throw new InvalidSearchRequestException("Empty and/or the provided search value is not possible: " + searchValue);
            } else {
               // String sanitizedValue = sanitizeInput(searchValue);
               // searchRequest.entrySet().iterator().next().setValue(sanitizedValue);
                return;
            }
        }
        logger.warn("SearchType is not available: " + searchType);
        throw new InvalidSearchRequestException("SearchType is not available: " + searchType);
    }

    private static String sanitizeInput(String input) {
        // Replace underscores with empty strings if not followed by another underscore or whitespace
        // Replace other special characters with whitespace surrounding them
        return input.replaceAll("[^a-zA-Z0-9&_\\s-]", " ")
                .replaceAll("(?<![\\w&&[^_]])[_-](?![_\\w])", " ") // Replace standalone underscores or hyphens with whitespace
                .replaceAll("\\s*&\\s*", " & ")
                .replaceAll("\\s*-\\s*", " ")
                .replaceAll("\\s*_\\s*", " ")
                .trim();
    }

    public static boolean containsAlphanumeric(String inputString) {
        // Define a regular expression pattern to match alphanumeric characters
        String alphanumericPattern = ".*[a-zA-Z0-9].*";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(alphanumericPattern);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(inputString);

        // Use matcher.find() to check if the input contains alphanumeric characters
        return matcher.find();
    }

    public Map<String, String> getTypeOfSearch() {
        Map<String, String> searchTypes = SearchTypeUtil.getInputTypes();
        int typos = 0;
        String searchTypeNumber;

        while (typos < MAX_TYPING_ERRORS) {
            System.out.println("Type in the number corresponding to the type of search you want to perform:" +
                    "\n" + "1 Area\n" + "2 Artist\n");

            // User input
            searchTypeNumber = scannerWrapper.getNextLine().trim();


            // Check if the input corresponds to a valid search type
            if (searchTypes.containsKey(searchTypeNumber)) {
                String searchType = searchTypes.get(searchTypeNumber);
                System.out.println("What " + searchType + " do you want to search for?");
                String searchValue = scannerWrapper.getNextLine().trim().toUpperCase();
                if (searchValue.isEmpty() || !containsAlphanumeric(searchValue)) {
                    logger.info("searching with user input is not possible:" + searchTypeNumber);
                    System.out.println("Invalid to search for: " + searchType + ". Please enter a valid search criteria.");
                } else {
                    return Collections.singletonMap(searchType, searchValue);
                }
            } else {
                logger.info("Invalid number for a search type:" + searchTypeNumber);
                ++typos;
                if (typos >= MAX_TYPING_ERRORS) {
                    logger.warn("Too many typos, restarting searchengine");
                    return Collections.emptyMap();
                }
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return Collections.emptyMap();
    }

    public boolean restartSearch() {
        int consecutiveTypoMistakes = 0;

        while (consecutiveTypoMistakes < MAX_TYPING_ERRORS) {

            //Ask user if they want to make a search on the existing session
            System.out.println("Want to make a new search on the existing session? (Yes/No)");
            String input = scannerWrapper.getNextLine();
            if (input.equalsIgnoreCase(YES_OPTION)) {
                return true;
            } else if (input.equalsIgnoreCase(NO_OPTION)) {
                scannerWrapper.close();
                return false;
            } else {
                consecutiveTypoMistakes++;
                System.out.println("Invalid input. Please enter 'Yes' or 'No'.");
            }
        }
        System.out.println("Too many consecutive typos. Restarting search engine.");
        scannerWrapper.close();
        return false;
    }
}

