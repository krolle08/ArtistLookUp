package Application.utils;

import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class UserInputUtil {
    private static final Logger logger = LoggerFactory.getLogger(UserInputUtil.class);
    private static final int MAX_TYPING_ERRORS = 10;
    private static final String YES_OPTION = "Yes";
    private static final String NO_OPTION = "No";
    private ScannerWrapper scannerWrapper;

    @Autowired
    public UserInputUtil(ScannerWrapper scannerWrapper) {
        this.scannerWrapper = scannerWrapper;
    }

    public Map<String, String> getTypeOfSearch() {
        Map<String, String> searchTypes = SearchTypeUtil.getInputTypes();
        int typos = 0;
        String searchTypeNumber = new String();

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
                return Collections.singletonMap(searchType, searchValue);
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
            if (input.equals(YES_OPTION)) {
                return true;
            } else if (input.equals(NO_OPTION)) {
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

