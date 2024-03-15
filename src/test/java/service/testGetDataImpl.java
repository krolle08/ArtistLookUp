package service;
import Application.api.MusicBrainzNameSearchRoute;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

public class testGetDataImpl {
    @Test
    public void testGetTypeOfSearch() {
        /*
        // Arrange
        // Mock the behavior of Scanner
        Scanner scannerMock = Mockito.mock(Scanner.class);
        Mockito.when(scannerMock.nextLine())
                .thenReturn("2")  // First input: search type "2" (Artist)
                .thenReturn("Nirvana");  // Second input: search value "Nirvana"

        // Set the mocked Scanner instance to getDataImpl
        GetDataImpl getDataImpl = new GetDataImpl(scannerMock, new MusicBrainzNameSearchRoute());

        // Act
        try {
            Map<String, String> searchParams = getDataImpl.getTypeOfSearch();

            // Assert
            Assertions.assertNotNull(searchParams);
            Assertions.assertEquals("Artist", searchParams.keySet().iterator().next());
            Assertions.assertEquals("Nirvana", searchParams.values().iterator().next());
        } catch (Exception e) {
            Assertions.fail("Exception occurred: " + e.getMessage());
        }

         */
    }

    @Test
    public void testRun_Artist() throws Exception {
        // Arrange
        String expectedMBID = "MockedMBID";
        ResponseEntity<String> mockedResponse = new ResponseEntity<>(expectedMBID, HttpStatus.OK);

        MusicBrainzNameSearchRoute musicBrainzNameSearchRouteMock = Mockito.mock(MusicBrainzNameSearchRoute.class);
        Mockito.when(musicBrainzNameSearchRouteMock.getMBID(any(Map.class))).thenReturn((Map) mockedResponse);
/*
        Scanner scannerMock = Mockito.mock(Scanner.class);
        Mockito.when(scannerMock.nextLine())
                .thenReturn("2")  // Simulate user selecting Artist
                .thenReturn("Nirvana");  // Simulate user entering search value

        GetDataImpl getDataImpl = new GetDataImpl(scannerMock, musicBrainzNameSearchRouteMock);

        // Act
        getDataImpl.run();

        // Assert
        // Verify that mBID is set to expected value when the ARTIST case is executed
        // You might need to add a getter for mBID in GetDataImpl class for this assertion
     //   Mockito.verify(musicBrainzNameSearchRouteMock).getMBID(any(Map.class)); // Verify that getMBID method is called
        // Add more assertions as needed

 */
    }
}
