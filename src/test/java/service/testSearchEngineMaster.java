package service;

import Application.SearchEngineManager;
import Application.YourApplication;
import Application.service.GetDataImpl;
import Application.utils.ScannerWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testSearchEngineMaster {

    @Autowired
    GetDataImpl getData;
    @Test
    public void testEndToEnd() {
        //Given
        String searchType = "2";
        String search = "Nirvana";
        String endSearch = "No";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Mock dependencies
        ScannerWrapper scannerWrapper = mock(ScannerWrapper.class);

        // Configure behavior of mocks
        when(scannerWrapper.getNextLine()).thenReturn(searchType, search, endSearch); // Simulate user input

        // When
        YourApplication.main(new String[0]);
        String output = outputStream.toString().trim();

        // Then
    }
    @Test
    public void testEndToEndNoExceptionThrows() {
        //Given
        String searchType = "2";
        String search = "Nirvana";
        String endSearch = "No";

        // Mock dependencies
        ScannerWrapper scannerWrapper = mock(ScannerWrapper.class);
       // GetDataImpl getDataImpl =  mock(GetDataImpl.class);

        // Configure behavior of mocks
        when(scannerWrapper.getNextLine()).thenReturn(searchType, search, endSearch); // Simulate user input

        // Create the SearchEngineManager instance and inject the mock dependencies
        SearchEngineManager searchEngineManager = new SearchEngineManager(scannerWrapper);

        // When
        searchEngineManager.runSearchEngine();

        // Then
        assertFalse(searchEngineManager.getRestartSearchEngine());
        verify(getData, times(1)).run();
    }
}
