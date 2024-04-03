package service.wikipedia;

import Application.YourApplication;
import Application.api.WikipediaSearchRoute;
import Application.service.ArtistContainer.WikiInfoObj;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.net.URISyntaxException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testWikiPediaSearchRoute {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private WikipediaSearchRoute wikipediaSearchRoute;
    @Test
    public void test_WikipediaEndpoint(){
        //Given
        String nirvanaURLQuery = "Nirvana%20(band)";
        WikiInfoObj wikiInfoObj = new WikiInfoObj(null, nirvanaURLQuery);

        //When
        try {
            wikipediaSearchRoute.wikipediaService(wikiInfoObj);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //Then
        assertTrue(wikiInfoObj.getWikiPediaStatuccode().equals(HttpStatus.OK.value()));
        assertFalse(wikiInfoObj.getDescription().isEmpty());
    }
}
