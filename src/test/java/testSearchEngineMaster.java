import Application.SearchEngineController;
import Application.Application;
import Application.utils.ScannerWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * These tests are made as End-To-End test for when the application is running
 */
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testSearchEngineMaster {

    @Autowired
    SearchEngineController searchEngineController;

    @MockBean
    private ScannerWrapper scannerWrapper;
    @Test
    public void testCorrectInput() {
        //Given
        String searchType = "2";
        String search = "Nirvana";
        String endSearch = "No";

        // Configure behavior of mocks
        when(scannerWrapper.getNextLine())
                .thenReturn(searchType)
                .thenReturn(search)
                .thenReturn(endSearch);

        // When
        searchEngineController.runSearchEngine();

        // Then
        assertFalse(searchEngineController.getRestartSearchEngine());
    }

    @Test
    public void testEmptyInput() {
        //Given
        String searchType = "2";
        String search = "\n";
        String endSearch = "No";

        // Configure behavior of mocks
        when(scannerWrapper.getNextLine())
                .thenReturn(searchType)
                .thenReturn(search)
                .thenReturn(endSearch);

        // When
        searchEngineController.runSearchEngine();

        // Then
        assertFalse(searchEngineController.getRestartSearchEngine());
    }
}
