import Application.Application;
import Application.SearchEngineController;
import Application.utils.ScannerWrapper;
import Application.utils.UserInputUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.when;

/**
 * These tests are made as End-To-End test for when the application is running
 */
@Disabled("This test is still under development and should not be run automatically")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class  testLoadTests {

    @Autowired
    SearchEngineController searchEngineController;

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
        // Number of concurrent users to simulate
        int numUsers = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numUsers);



        // Mock scanner inputs
        Queue<String> scannerInputs = new LinkedList<>();
        scannerInputs.addAll(Arrays.asList("2", "Nirvana", "No"));  // Input sequence for each user
        // Configure behavior of mocks
        when(scannerWrapper.getNextLine())
                .thenAnswer(invocation -> scannerInputs.poll());
        for (int i = 0; i < numUsers; i++) {
            executor.submit(() -> {
                // When
                searchEngineController.runSearchEngine();
            });
        }

        // Shutdown the executor and wait for all tasks to complete
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);
    }
}
