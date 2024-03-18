package service.musicbrainz;

import Application.YourApplication;
import Application.api.MusicBrainzIDSearchRoute;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testMusicBrainzIDSearchRoute {
    String cdkey = "VHVQX-NNDCE-G08DB"; //Helldivers

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MusicBrainzIDSearchRoute musicBrainzIDSearchRoute;

    @Test
    public void testHelloWorldEndpoint() throws URISyntaxException {
        //Given
        String nirvana = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        String succescriteria = "Q11649";
        //When
        Map<String, String> result = musicBrainzIDSearchRoute.getDataFromArtist(nirvana);

        //Then
        assertThat(result.get("name")).isEqualTo("Nirvana");
        assertThat(result.get("statuscode")).isEqualTo("200");
        assertThat(result.get("wikidataSearchTerm")).isEqualTo(succescriteria);
    }

    @Test
    public void testSearchArtistEndpoint() {
        // Given
        String nirvana = "5b11f4ce-a62d-471e-81fc-a69a8278c7da"; // Example artist ID

        // When
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/MBArtist/" + nirvana, String.class);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        // You can add more assertions based on the expected response from the MusicBrainz API
    }
}
