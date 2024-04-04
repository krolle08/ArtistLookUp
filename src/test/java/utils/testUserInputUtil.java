package utils;

import Application.Application;
import Application.utils.TypeOfSearchEnum;
import Application.utils.ScannerWrapper;
import Application.utils.UserInputUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testUserInputUtil {

    @MockBean
    private ScannerWrapper scannerWrapper;
    @Autowired
    private UserInputUtil userInputUtil;
    @Test
    public void testTypeOfSearch() {
        // Given
        Map<String, String> result;
        String searchType = "2";
        String search = "Nirvana";
        String endSearch = "No";
        // Mock dependencies

        // Configure behavior of mocks
        when(scannerWrapper.getNextLine()).thenReturn(searchType).thenReturn(search).thenReturn(endSearch); // Simulate user input

        // When
        result = userInputUtil.getTypeOfSearch();

        // Then
        assertThat(result.get(TypeOfSearchEnum.ARTIST.getSearchType()).equals(search));
        assertThat(result.containsKey(TypeOfSearchEnum.ARTIST.getSearchType()));
    }

    @Test
    public void testRestartSearchTypoError() {
        // Given
        String restartSearch = "WrongTypo";
        Map<String, String> result;
        // Mock dependencies
        // Configure behavior of mocks
        when(scannerWrapper.getNextLine())
                .thenReturn(restartSearch)
                .thenReturn(restartSearch)
                .thenReturn(restartSearch)
                .thenReturn(restartSearch)
                .thenReturn(restartSearch)
                .thenReturn(restartSearch)
                .thenReturn(restartSearch)
                .thenReturn(restartSearch)
                .thenReturn(restartSearch)
                .thenReturn(restartSearch);

        // When
        result = userInputUtil.getTypeOfSearch();

        // Then
        assertThat(result.isEmpty());
    }

    @Test
    public void testRestartSearchTrue() {
        // Given
        String restartSearch = "Yes";

        // Configure behavior of mocks
        when(scannerWrapper.getNextLine()).thenReturn(restartSearch); // Simulate user input

        // When
        boolean result = userInputUtil.restartSearch();

        // Then
        assertTrue(result);
    }

    @Test
    public void testRestartSearchFalse() {
        // Given
        String restartSearch = "No";

        // Configure behavior of mocks
        when(scannerWrapper.getNextLine()).thenReturn(restartSearch); // Simulate user input

        // When
        boolean result = userInputUtil.restartSearch();

        // Then
        assertFalse(result);
    }


}
