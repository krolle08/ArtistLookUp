package Application;

import Application.service.GetDataImpl;
import Application.utils.ScannerWrapper;
import Application.utils.UserInputUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchEngineManager {
    private boolean restartSearchEngine = true;
    private ScannerWrapper scannerWrapper;
    @Autowired
    private GetDataImpl getDataImpl;

    @Autowired
    public SearchEngineManager(ScannerWrapper scannerWrapper) {
        this.scannerWrapper = scannerWrapper;
    }


    public void runSearchEngine() {
        do {
            getDataImpl.run();
            restartSearchEngine = UserInputUtil.restartSearch(scannerWrapper);
        } while (restartSearchEngine);
        scannerWrapper.close();
    }

    public boolean getRestartSearchEngine() {
        return restartSearchEngine;
    }

    public void setGetDataImpl(GetDataImpl getDataImpl){
        this.getDataImpl = getDataImpl;
    }

}