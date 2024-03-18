package service.Wikidata;

import Application.YourApplication;
import Application.api.MusicBrainzIDSearchRoute;
import Application.api.WikidataSearchRoute;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testWikiDataSearchRoute {
    String cdkey = "VHVQX-NNDCE-G08DB";

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private WikidataSearchRoute wikidataSearchRoute;

    @Test
    public void testHelloWorldEndpoint() throws URISyntaxException {
        //Given
        String nirvanaTerm = "Q11649";
        String succescriteria = "Nirvana (band)";

        //When
        Map<String, String> rest = wikidataSearchRoute.getWikidataFromArtist(nirvanaTerm);

        //Then
        assertThat(decodeString(rest.get("wikipediaSearchTerm"))).isEqualTo(succescriteria);
        assertThat(rest.get("wikidataStatusCode")).isEqualTo("200");

    }
    public static String decodeString(String input) {
        try {
            return URLDecoder.decode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Handle decoding exception
            e.printStackTrace();
            return null;
        }
    }
}
