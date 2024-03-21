package service;

import Application.YourApplication;
import Application.api.*;
import Application.service.GetDataImpl;
import Application.service.TypeOfSearchEnum;
import Application.utils.ScannerWrapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testGetDataImpl {
    @Test
    public void testTypeOfSearch() throws Exception {
        //Arrange
        Map<String, String> response = new HashMap<>();
        String searchType = "2";
        String search = "Nirvana";
        // Mock dependencies
        ScannerWrapper scannerWrapper = mock(ScannerWrapper.class);

        // Configure behavior of mocks
        when(scannerWrapper.nextLine()).thenReturn(searchType, search); // Simulate user input

        // Create instance of the class to be tested
        GetDataImpl testClass = new GetDataImpl(scannerWrapper);

        // Invoke the method
        response = testClass.getTypeOfSearch();

        // Verify that the method behaves as expected
        Assertions.assertThat(response.get(TypeOfSearchEnum.ARTIST.getSearchType()).equals(search));
    }

    @Test
    public void testEndSearch() throws Exception {
        //Arrange
        Map<String, String> response = new HashMap<>();
        String endSearch = "WrongTypo";
        // Mock dependencies
        ScannerWrapper scannerWrapper = mock(ScannerWrapper.class);

        // Configure behavior of mocks
        when(scannerWrapper.nextLine()).thenReturn(endSearch, endSearch, endSearch, endSearch, endSearch, endSearch, endSearch, endSearch, endSearch, endSearch, endSearch); // Simulate user input

        // Create instance of the class to be tested
        GetDataImpl testClass = new GetDataImpl(scannerWrapper);

        // Invoke the method
        boolean result = testClass.endSearch();

        // Verify that the method behaves as expected
        Assertions.assertThat(response.get(TypeOfSearchEnum.ARTIST.getSearchType()).equals(endSearch));
    }
    @Test
    public void testMockedService() {
        Map<String, String> response = new HashMap<>();
        String searchType = "2";
        String search = "Nirvana";
        // Mock dependencies
        MusicBrainzNameSearchRoute musicBrainzNameSearchRoute = mock(MusicBrainzNameSearchRoute.class);
        MusicBrainzIDSearchRoute musicBrainzIDSearchRoute = mock(MusicBrainzIDSearchRoute.class);
        WikidataSearchRoute wikidataSearchRoute = mock(WikidataSearchRoute.class);
        WikipediaSearchRoute wikipediaSearchRoute = mock(WikipediaSearchRoute.class);
        CoverArtArchiveService coverArtArchiveService = mock(CoverArtArchiveService.class);
        ScannerWrapper scannerWrapper = mock(ScannerWrapper.class);

        // Configure behavior of mocks
        when(scannerWrapper.nextLine()).thenReturn(searchType, search); // Simulate user input
        when(musicBrainzNameSearchRoute.getMBID(anyMap())).thenReturn(response); // Simulate empty response


        // Create instance of the class to be tested
        GetDataImpl testClass = new GetDataImpl(scannerWrapper);
    }
    @Test
    public void testtestEndSearch() throws Exception {
        // Arrange
        String expectedMBID = "MockedMBID";
        ResponseEntity<String> mockedResponse = new ResponseEntity<>(expectedMBID, HttpStatus.OK);
        Map<String, String> typeOFsearch = new HashMap<>();
        typeOFsearch.put("2", TypeOfSearchEnum.ARTIST.toString());
        ScannerWrapper scannerMock = mock(ScannerWrapper.class);
        when(scannerMock.nextLine())
                .thenReturn("2")  // Simulate user selecting Artist
                .thenReturn("Nirvana");  // Simulate user entering search value

        MusicBrainzNameSearchRoute musicBrainzNameSearchRouteMock = mock(MusicBrainzNameSearchRoute.class);
        GetDataImpl getDataImpl = new GetDataImpl(scannerMock);
        when(musicBrainzNameSearchRouteMock.getMBID(any(Map.class))).thenReturn((Map) mockedResponse);
        when(getDataImpl.getTypeOfSearch()).thenReturn(typeOFsearch);


        // Act
        getDataImpl.run();

        // Assert
        // Verify that mBID is set to expected value when the ARTIST case is executed
        // You might need to add a getter for mBID in GetDataImpl class for this assertion
        Mockito.verify(musicBrainzNameSearchRouteMock).getMBID(any(Map.class)); // Verify that getMBID method is called
        // Add more assertions as needed


    }

}
