import Application.Application;
import Application.utils.ScannerWrapper;
import Application.utils.UserInputUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * These tests are made as End-To-End test for when the application is running
 */


@Disabled("This test is still under development and should not be run automatically")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testEnduranceTests {

    @MockBean
    private ScannerWrapper scannerWrapper;

    @Autowired
    private UserInputUtil userInputUtil;

    @BeforeEach
    public void setup() {
        // Inject the mocked ScannerWrapper into the UserInputUtil
        ReflectionTestUtils.setField(userInputUtil, "scannerWrapper", scannerWrapper);
    }

    @Disabled("This test is still under development and should not be run automatically")
    @Test
    public void testLoadScenario() throws InterruptedException {

    }
}
