package api;

import Application.Application;
import Application.api.MusicBrainzNameSearchRoute;
import Application.service.MusicBrainz.MusicBrainzNameService;
import Application.utils.RestTempUtil;
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
    public void testInputForUriToOrdOgTegn_Succes() {
        //Given
        Map<String, String> filter = new HashMap<>();
        filter.put("Artist", "Nik & Jay");

        // When

        // Then
        assertDoesNotThrow(() -> musicBrainzNameSearchRoute.getUri(filter), "Exception occurred while creating URI");
    }

    @Test
    public void testInputForUriEtOrd_Succes2() {
        //Given
        Map<String, String> filter = new HashMap<>();
        filter.put("Artist", "Nirvana");
        String expected = "/ws/2/annotation/query=artist:" + "nirvana";

        // When
        URI result = musicBrainzNameSearchRoute.getUri(filter);

        // Then
        String actual = result.getPath() + result.getQuery();
        assertTrue(actual.contains(expected));
    }

    @Test
    public void testBadInputForUri_Error() {
        //Given
        Map<String, String> filter = new HashMap<>();
        filter.put("Artist", "###");

        // When
        // Then
        assertDoesNotThrow( () -> musicBrainzNameSearchRoute.getUri(filter));
    }

    @Test
    public void testEmptyInputForUri_Error() {
        //Given
        Map<String, String> filter = new HashMap<>();
        filter.put("Artist", "\n");
        // When

        // Then
        assertDoesNotThrow(() -> musicBrainzNameSearchRoute.getUri(filter));
    }

    @Test
    public void testPositiveResponse_Succes() {
        //Given
        Map<String, String> filter = new HashMap<>();
        filter.put("Artist", "Nirvana");

        // When
        ResponseEntity<String> result =  musicBrainzNameSearchRoute.doGetResponse(filter);

        // Then
        Assertions.assertFalse(result.getBody().isEmpty());
    }
}
