package api;

import Application.Application;
import Application.api.MusicBrainzNameSearchRoute;
import Application.service.MusicBrainz.MusicBrainzNameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testMusicBrainzNameSearchRoute {

    @Autowired
    private MusicBrainzNameSearchRoute musicBrainzNameSearchRoute;

    @Test
    public void testInputForUri_Succes() {
        //Given
        Map<String, String> filter = new HashMap<>();
        filter.put("Artist", "Nirvana");

        // When

        // Then
        assertDoesNotThrow(() -> musicBrainzNameSearchRoute.getUri(filter), "Exception occurred while creating URI");
    }

    @Test
    public void testBadInputForUri_Error() {
        //Given
        Map<String, String> filter = new HashMap<>();
        filter.put("Artist", "###");

        // When

        // Then
        assertThrows(IllegalArgumentException.class, () -> musicBrainzNameSearchRoute.getUri(filter));
    }

    @Test
    public void testEmptyInputForUri_Error() {
        //Given
        Map<String, String> filter = new HashMap<>();
        filter.put("Artist", "\n");
        // When

        // Then
        assertThrows(IllegalArgumentException.class, () -> musicBrainzNameSearchRoute.getUri(filter));
    }

    @Test
    public void testPositiveResponse_Succes() {
        //Given
        URI uri = URI.create("http://musicbrainz.org:80/ws/2/artist/?query=artist:NIRVANA&fmt=json");

        // When
        ResponseEntity<String> result =  musicBrainzNameSearchRoute.doGetResponse(uri);

        // Then
        Assertions.assertFalse(result.getBody().isEmpty());
    }
}
