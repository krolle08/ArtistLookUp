package Application.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible to check if type of search requested is possible
 * Not implemented yet.
 */
public class SearchTypeUtil {
    private static final Map<String, String> searchTypes = createMapOfSearchTypes();

    public static Map<String, String> getInputTypes() {
        return searchTypes;
    }

    private static Map<String, String> createMapOfSearchTypes() {
        Map<String, String> searchTypes = new HashMap<>();
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
        return searchTypes;
    }
}


