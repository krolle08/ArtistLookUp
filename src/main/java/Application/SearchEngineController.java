package Application;

import Application.features.GetDataImpl;
import Application.utils.ScannerWrapper;
import Application.utils.UserInputUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controls the operation of the search engine for retrieving data.
 * This class manages the search engine's lifecycle.
 */
@Component
public class SearchEngineController {
    private boolean restartSearchEngine = true;
    private final GetDataImpl getDataImpl;
    private final UserInputUtil userInputUtil;

    /**
     * @param getDataImpl   The component responsible for retrieving data from APIs.
     * @param userInputUtil The utility class for handling user input.
     */
    @Autowired
    public SearchEngineController(GetDataImpl getDataImpl, UserInputUtil userInputUtil) {
        this.getDataImpl = getDataImpl;
        this.userInputUtil = userInputUtil;
    }

    public void runSearchEngine() {
        do {
            getDataImpl.run();
            restartSearchEngine = userInputUtil.restartSearch();
        } while (restartSearchEngine);
    }

    public boolean getRestartSearchEngine() {
        return restartSearchEngine;
    }
}