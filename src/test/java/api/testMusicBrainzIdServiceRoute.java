package api;

import Application.Application;
import Application.api.MusicBrainzIDSearchRoute;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testMusicBrainzIdServiceRoute {

    @Autowired
    private MusicBrainzIDSearchRoute musicBrainzIdService;
    @Test
    public void testInputForUri_Success() {
        //Given
        String searchTerm = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";

        // When
        URI uri = musicBrainzIdService.getUri(searchTerm);
        // Then
        assertNotNull(uri); // Assert that URI is not null
        assertEquals("http", uri.getScheme()); // Assert scheme
        assertEquals("musicbrainz.org", uri.getHost()); // Assert host
        String expected = "/ws/2/artist/" + searchTerm;
        String actual = uri.getPath() + uri.getQuery();
        assertTrue(actual.contains(expected));
    }
    @Test
    public void testBadInputForUri_Error() {
        //Given
        String searchTerm = "###";

        // When

        // Then
        assertThrows(IllegalArgumentException.class, () -> musicBrainzIdService.getUri(searchTerm));
    }

    @Test
    public void testEmptyInputForUri_Error() {
        //Given
        String searchTerm = "\n";

        // When

        // Then
        assertThrows(IllegalArgumentException.class, () -> musicBrainzIdService.getUri(searchTerm));
    }

    @Test
    public void testPositiveResponse_Succes() {
        //Given
        String searchTerm="5b11f4ce-a62d-471e-81fc-a69a8278c7da";

        // When
        ResponseEntity<String> result =  musicBrainzIdService.doGetResponse(searchTerm);

        // Then
        Assertions.assertFalse(result.getBody().isEmpty());
    }
}
