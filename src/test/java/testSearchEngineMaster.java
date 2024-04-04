import Application.SearchEngineController;
import Application.Application;
import Application.utils.ScannerWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testSearchEngineMaster {

    @Autowired
    SearchEngineController searchEngineController;

    @MockBean
    private ScannerWrapper scannerWrapper;
    @Test
    public void testSearchEngine() {
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
}
