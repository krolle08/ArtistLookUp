package service;

import Application.YourApplication;
import Application.api.MusicBrainzSearchRoute;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testMusicBrainzSearchRoute {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private MusicBrainzSearchRoute musicBrainzSearchRoute;
    @Test
    public void testHelloWorldEndpoint() throws URISyntaxException {
        String nirvana = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        String rest = musicBrainzSearchRoute.searchArtist(nirvana);
      //  assertThat(rest).isEqualTo("MB ROUTE");
    }
}
