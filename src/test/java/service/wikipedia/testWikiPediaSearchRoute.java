package service.wikipedia;

import Application.YourApplication;
import Application.api.WikipediaSearchRoute;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.net.URISyntaxException;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testWikiPediaSearchRoute {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private WikipediaSearchRoute wikipediaSearchRoute;
    @Test
    public void test_WikipediaEndpoint() throws URISyntaxException {
        //Given
        String nirvanaURL = "Nirvana%20(band)";

        //When
        //Map<String, Object> rest = wikipediaSearchRoute.wikipediaService(nirvanaURL);

        //Then
        //assertThat(rest.get("wikidatastatusCode")).isEqualTo("200");
    }
}
