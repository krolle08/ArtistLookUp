package utils;

import Application.Application;
import Application.api.MusicBrainzIDSearchRoute;
import Application.api.MusicBrainzNameSearchRoute;
import Application.api.WikidataSearchRoute;
import Application.utils.RestTempUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testRestTempUtil {

    private final Map<String, String> filterParams = new HashMap<>();

    @Autowired
    MusicBrainzIDSearchRoute musicBrainzIDSearchRoute;
    @Autowired
    WikidataSearchRoute wikidataSearchRoute;
    @Autowired
    MusicBrainzNameSearchRoute musicBrainzNameSearchRoute;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        filterParams.put("Artist", "Nirvana");
    }

    @Test
    public void testMusicBrainzIdURIConstructer() {
        // Given
        String expectedURI = "http://musicbrainz.org/ws/2/artist/5b11f4ce-a62d-471e-81fc-a69a8278c7da?fmt=json&inc=url-rels+release-groups";

        // When
        URI result = RestTempUtil.getMBIdUriconstructor("5b11f4ce-a62d-471e-81fc-a69a8278c7da", musicBrainzIDSearchRoute.getRestConfig());

        //Then
        assertTrue(result.toString().equalsIgnoreCase(expectedURI));
    }

    @Test
    public void testMusicBrainzNameURIConstructer() {
        // Given
        URI result;
        String expectedURI = "http://musicbrainz.org:80/ws/2/annotation/?query=artist:nirvana&fmt=json&inc=url-rels+release-groups";

        // When
        result = RestTempUtil.getMBNameUriconstructor(filterParams, musicBrainzNameSearchRoute.getRestConfig());

        //Then
        assertTrue(result.toString().equals(expectedURI));
    }
    @Test
    public void testWikiDataURIConstructor() {
        // Given
        URI result;
        String searchTerm ="Q11649";
        String expectedURI = "https://wikidata.org/w/api.php?action=wbgetentities&format=json&ids=Q11649&props=sitelinks";

        // When
        result = RestTempUtil.getWikiDataUriconstructor(searchTerm, wikidataSearchRoute.getRestConfig());

        //Then
        assertThat(result.equals(expectedURI));
    }

    @Test
    public void testisBodyEmptyFalse() {
        // Given
        ResponseEntity<String> responseEntity = getResponseEntity();

        // When
        boolean result = RestTempUtil.isBodyEmpty(responseEntity);

        //Then
        assertFalse(result);
    }

    @Test
    public void testisBodyEmptyTrue() {
        // Given
        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.OK);

        // When
        boolean result = RestTempUtil.isBodyEmpty(responseEntity);

        //Then
        assertTrue(result);
    }

    private ResponseEntity<String> getResponseEntity(){
        return new ResponseEntity<>("{\"artists\":{\n" +
                "  \"artist\": {\n" +
                "    \"id\": \"b961732c-0c70-4bd7-94b4-843317862853\",\n" +
                "    \"type\": \"Group\",\n" +
                "    \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
                "    \"score\": 100,\n" +
                "    \"name\": \"Nik & Jay\",\n" +
                "    \"sort-name\": \"Nik & Jay\",\n" +
                "    \"country\": \"DK\",\n" +
                "    \"area\": {\n" +
                "      \"id\": \"4757b525-2a60-324a-b060-578765d2c993\",\n" +
                "      \"type\": \"Country\",\n" +
                "      \"type-id\": \"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\n" +
                "      \"name\": \"Denmark\",\n" +
                "      \"sort-name\": \"Denmark\",\n" +
                "      \"life-span\": {\n" +
                "        \"ended\": null\n" +
                "      }\n" +
                "    },\n" +
                "    \"begin-area\": {\n" +
                "      \"id\": \"97b03fbe-7dae-4409-9934-d0a8586f2a36\",\n" +
                "      \"type\": \"City\",\n" +
                "      \"type-id\": \"6fd8f29a-3d0a-32fc-980d-ea697b69da78\",\n" +
                "      \"name\": \"Værløse\",\n" +
                "      \"sort-name\": \"Værløse\",\n" +
                "      \"life-span\": {\n" +
                "        \"ended\": null\n" +
                "      }\n" +
                "    },\n" +
                "    \"disambiguation\": \"Danish R&B / pop duo\",\n" +
                "    \"life-span\": {\n" +
                "      \"begin\": \"2002\",\n" +
                "      \"ended\": null\n" +
                "    },\n" +
                "    \"aliases\": [\n" +
                "      {\n" +
                "        \"sort-name\": \"Nik og Jay\",\n" +
                "        \"name\": \"Nik og Jay\",\n" +
                "        \"locale\": null,\n" +
                "        \"type\": null,\n" +
                "        \"primary\": null,\n" +
                "        \"begin-date\": null,\n" +
                "        \"end-date\": null\n" +
                "      }\n" +
                "    ],\n" +
                "    \"tags\": [\n" +
                "      { \"count\": 2, \"name\": \"pop\" },\n" +
                "      { \"count\": 2, \"name\": \"rap\" },\n" +
                "      { \"count\": 0, \"name\": \"danish\" },\n" +
                "      { \"count\": 1, \"name\": \"pop rap\" }\n" +
                "    ]\n" +
                "  }\n" +
                "}\n}", HttpStatus.OK);
    }

}
