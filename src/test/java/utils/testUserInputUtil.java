package utils;

import Application.YourApplication;
import Application.service.GetDataImpl;
import Application.service.TypeOfSearchEnum;
import Application.utils.ScannerWrapper;
import Application.utils.UserInputUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testUserInputUtil {
    @Test
    public void testTypeOfSearch() {
        // Given
        Map<String, String> response;
        String searchType = "2";
        String search = "Nirvana";
        String endSearch = "No";
        // Mock dependencies
        ScannerWrapper scannerWrapper = mock(ScannerWrapper.class);

        // Configure behavior of mocks
        when(scannerWrapper.getNextLine()).thenReturn(searchType, search, endSearch); // Simulate user input

        // When
        response = UserInputUtil.getTypeOfSearch(scannerWrapper);

        // Then
        assertThat(response.get(TypeOfSearchEnum.ARTIST.getSearchType()).equals(search));
        assertThat(response.containsKey(TypeOfSearchEnum.ARTIST.getSearchType()));
    }

    @Test
    public void testRestartSearchTypoError() {
        // Given
        String restartSearch = "WrongTypo";
        // Mock dependencies
        ScannerWrapper scannerWrapper = mock(ScannerWrapper.class);
        // Configure behavior of mocks
        when(scannerWrapper.getNextLine()).thenReturn(restartSearch, restartSearch, restartSearch, restartSearch, restartSearch, restartSearch, restartSearch, restartSearch, restartSearch, restartSearch, restartSearch); // Simulate user input

        // When
        Map<String, String> result = UserInputUtil.getTypeOfSearch(scannerWrapper);

        // Then
        assertThat(result.isEmpty());
    }

    @Test
    public void testRestartSearchTrue() {
        // Given
        String restartSearch = "Yes";
        // Mock dependencies
        ScannerWrapper scannerWrapper = mock(ScannerWrapper.class);

        // Configure behavior of mocks
        when(scannerWrapper.getNextLine()).thenReturn(restartSearch); // Simulate user input

        // When
        boolean result = UserInputUtil.restartSearch(scannerWrapper);

        // Then
        assertTrue(result);
    }

    @Test
    public void testRestartSearchFalse() {
        // Given
        String restartSearch = "No";
        // Mock dependencies
        ScannerWrapper scannerWrapper = mock(ScannerWrapper.class);

        // Configure behavior of mocks
        when(scannerWrapper.getNextLine()).thenReturn(restartSearch); // Simulate user input

        // When
        boolean result = UserInputUtil.restartSearch(scannerWrapper);

        // Then
        assertFalse(result);
    }


}
