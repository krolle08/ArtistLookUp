package service.musicbrainz;

import Application.YourApplication;
import Application.api.MusicBrainzIDSearchRoute;
import Application.service.ArtistInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testMusicBrainzIDSearchRoute {

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
        ArtistInfo artistInfo = musicBrainzIDSearchRoute.getDataWithMBID(nirvana);

        //Then
        assertThat(artistInfo.getName()).isEqualTo("Nirvana");
        assertThat(artistInfo.getmBStatusCode()).isEqualTo("200");
        assertThat(artistInfo.getWikiInfo().getWikidata()).isEqualTo(succescriteria);
        assertThat(!artistInfo.getAlbums().isEmpty());
        assertThat(artistInfo.getAlbums().size()).isEqualTo(17); // Check if it has a size of 17
    }


    public void testSearchArtistEndpoint() {
        // Given
        String nirvana = "5b11f4ce-a62d-471e-81fc-a69a8278c7da"; // Example artist ID

        // When
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/MBArtist/" + nirvana, String.class);

        //Then


    }
}
