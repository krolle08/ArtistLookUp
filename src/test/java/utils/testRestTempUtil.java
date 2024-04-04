package utils;

import Application.Application;
import Application.utils.RestTempUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testRestTempUtil {

    private Map<String, String> filterParams = new HashMap<>();
    private String query ="/?query=";
    private String protocol="http";
    private String annotation = "/artist";
    private String schemeDelimiter = "://";
    private String host = "musicbrainz.org";
    private int port = 80;
    private String pathPrefix = "/ws";
    private String version = "/2";
    private String json = "fmt=json";

    private String mbid= "5b11f4ce-a62d-471e-81fc-a69a8278c7da";

    private String pathPostFix ="?fmt=json&inc=url-rels+release-groups";

    private String postPrefix = "?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles=";

    private String searchTerm ="Nirvana+%28band%29";

    private String api = "/api.php";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        filterParams.put("Artist", "Nirvana");
    }

    @Test
    public void testConstructURI_Type1() {
        // Given
        StringBuffer result;
        String expectedURI = "http://musicbrainz.org:80/ws/2/artist/?query=artist:Nirvana&fmt=json";

        // When
        result = RestTempUtil.constructUri(filterParams,query,protocol,annotation,schemeDelimiter,host,port,pathPrefix,version,json);

        //Then
        assertThat(result.toString().equals(expectedURI));
    }
    @Test
    public void testConstructURI_Type2() {
        // Given
        StringBuffer result;
        String expectedURI = "http://musicbrainz.org:80/ws/2/artist/5b11f4ce-a62d-471e-81fc-a69a8278c7da?fmt=json&inc=url-rels+release-groups";

        // When
        result = RestTempUtil.constructUri(mbid,annotation+"/", protocol, schemeDelimiter, host, port, pathPrefix, version, pathPostFix);

        //Then
        assertThat(result.toString().equals(expectedURI));
    }
    @Test
    public void testConstructURI_Type3() {
        // Given
        String result;
        String expectedURI = "http://musicbrainz.org:80/ws/2/artist/?query=artist:Nirvana&fmt=json";

        // When
        result = RestTempUtil.constructUri(searchTerm, protocol, schemeDelimiter, host, pathPrefix, api, postPrefix);

        //Then
        assertThat(result.equals(expectedURI));
    }

    @Test
    public void testisBodyEmpty() {
        // Given
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Not Empty", HttpStatus.OK);
        String criteria = "artists";

        // When
        boolean result = RestTempUtil.isBodyEmpty(responseEntity, criteria);

        //Then
        assertFalse(result);
    }

}
