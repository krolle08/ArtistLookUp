package Application.service.UserInput;

import java.util.HashMap;
import java.util.Map;
/**
 * Handling terminal userinput events. Not implemented yet.
 */
public class UserInputEvent {
    private boolean restartSearch;
    private Map<String, String> searchParam = new HashMap<>();

    public void setRestartSearch(boolean restartSearch) {
        this.restartSearch = restartSearch;
    }

    public Map<String, String> getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(Map<String, String> searchParam) {
        this.searchParam = searchParam;
    }

    public UserInputEvent(boolean restartSearch) {
        this.restartSearch = restartSearch;
    }

    public boolean isRestartSearch() {
        return restartSearch;
    }
}
