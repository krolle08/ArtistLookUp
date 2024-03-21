package service.musicbrainz;

import Application.YourApplication;
import Application.api.MusicBrainzNameSearchRoute;
import Application.service.TypeOfSearchEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testMusicBrainzNameSearchRoute {
    String cdkey = "VHVQX-NNDCE-G08DB"; //Helldivers

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MusicBrainzNameSearchRoute musicBrainzNameSearchRoute;
    //"Q11649"
    @Test
    public void testHelloWorldEndpoint() {
        //Given
        Map<String, String> nirvana = new HashMap<>();
        nirvana.put(TypeOfSearchEnum.ARTIST.getSearchType(),"Nirvana");
        String MBID = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        //When
        Map<String, String> result = musicBrainzNameSearchRoute.getMBID(nirvana);

        //Then
        assertThat(result.get(TypeOfSearchEnum.ARTIST.getSearchType())).isEqualTo("Nirvana");
        assertThat(result.get("MBstatuscode")).isEqualTo("200");
        assertThat(result.get("MBID")).isEqualTo(MBID);
    }

    @Test
    public void testSearchArtistEndpoint() {
        //Given
        Map<String, String> nirvana = new HashMap<>();
        nirvana.put(TypeOfSearchEnum.ARTIST.getSearchType(),"Slipknot");
        String MBID = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        //When
        Map<String, String> result = musicBrainzNameSearchRoute.getMBID(nirvana);

        //Then
        assertThat(result.get(TypeOfSearchEnum.ARTIST.getSearchType())).isEqualTo("Slipknot");
        assertThat(result.get("MBstatuscode")).isEqualTo("200");
        assertThat(result.get("MBID")).isEqualTo(MBID);
    }
}
