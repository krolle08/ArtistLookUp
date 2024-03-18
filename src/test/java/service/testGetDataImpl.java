package service;

import Application.YourApplication;
import Application.api.*;
import Application.service.GetDataImpl;
import Application.service.TypeOfSearchEnum;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testGetDataImpl {
/*
    @Test
    public void testRun() throws Exception {
        //Arrange
        Map<String, String> response = new HashMap<>();
        response.put("MBID", "123456");
        // Mock dependencies
        MusicBrainzNameSearchRoute musicBrainzNameSearchRoute = mock(MusicBrainzNameSearchRoute.class);
        MusicBrainzIDSearchRoute musicBrainzIDSearchRoute = mock(MusicBrainzIDSearchRoute.class);
        WikidataSearchRoute wikidataSearchRoute = mock(WikidataSearchRoute.class);
        WikipediaSearchRoute wikipediaSearchRoute = mock(WikipediaSearchRoute.class);
        CoverArtArchiveService coverArtArchiveService = mock(CoverArtArchiveService.class);
        Scanner scanner = mock(Scanner.class);

        // Configure behavior of mocks
        when(scanner.nextLine()).thenReturn("2", "Nirvana"); // Simulate user input
        when(musicBrainzNameSearchRoute.getMBID(anyMap())).thenReturn(response); // Simulate empty response

        when(musicBrainzNameSearchRoute.getMBID(anyMap())).thenReturn(response);

        // Create instance of the class to be tested
        GetDataImpl testClass = new GetDataImpl(musicBrainzNameSearchRoute, musicBrainzIDSearchRoute, wikidataSearchRoute, wikipediaSearchRoute, coverArtArchiveService);

        // Invoke the method
        testClass.run();

        // Verify that the method behaves as expected
        // For example, you can verify that certain methods were called with specific arguments
        Mockito.verify(musicBrainzIDSearchRoute).getDataFromArtist("someArtistName");
        // You can also verify other interactions with mocks based on the expected behavior of the method
    }
    @Test
    public void testRun_Artist() throws Exception {
        // Arrange
        String expectedMBID = "MockedMBID";
        ResponseEntity<String> mockedResponse = new ResponseEntity<>(expectedMBID, HttpStatus.OK);
        Map<String, String> typeOFsearch = new HashMap<>();
        typeOFsearch.put("2", TypeOfSearchEnum.ARTIST.toString());
        Scanner scannerMock = mock(Scanner.class);
        when(scannerMock.nextLine())
                .thenReturn("2")  // Simulate user selecting Artist
                .thenReturn("Nirvana");  // Simulate user entering search value

        MusicBrainzNameSearchRoute musicBrainzNameSearchRouteMock = mock(MusicBrainzNameSearchRoute.class);
        GetDataImpl getDataImpl = new GetDataImpl(scannerMock, musicBrainzNameSearchRouteMock);
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

 */
}
