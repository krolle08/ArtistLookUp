package Application.service.UserInput;

import Application.features.GetDataImpl;
import Application.utils.UserInputUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Handling terminal userinput events. Not implemented yet.
 */
@Component
public class UserInputEventListener {
    private final UserInputUtil userInputUtil;
    private final GetDataImpl getDataImpl;

    @Autowired
    public UserInputEventListener(UserInputUtil userInputUtil, GetDataImpl getDataImpl) {
        this.userInputUtil = userInputUtil;
        this.getDataImpl = getDataImpl;
    }

    @Async
    @EventListener
    public void handleUserInput(UserInputEvent event) {
        do {
            event.setSearchParam(userInputUtil.getTypeOfSearch());
            getDataImpl.run();
            event.setRestartSearch(userInputUtil.restartSearch());
        } while (event.isRestartSearch());
    }
}
