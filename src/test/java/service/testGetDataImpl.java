package service;

import Application.YourApplication;
import Application.api.*;
import Application.service.GetDataImpl;
import Application.service.TypeOfSearchEnum;
import Application.utils.Json;
import Application.utils.ScannerWrapper;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testGetDataImpl {

    private String expectedjsonResponse;
    @Test
    public void testTypeOfSearch() {
        //Given
        Map<String, String> response = new HashMap<>();
        String searchType = "2";
        String search = "Nirvana";
        // Mock dependencies
        ScannerWrapper scannerWrapper = mock(ScannerWrapper.class);

        // Configure behavior of mocks
        when(scannerWrapper.nextLine()).thenReturn(searchType, search); // Simulate user input

        // Create instance of the class to be tested
        GetDataImpl testClass = new GetDataImpl(scannerWrapper);

        // Invoke the method
        response = testClass.getTypeOfSearch();

        // Verify that the method behaves as expected
        Assertions.assertThat(response.get(TypeOfSearchEnum.ARTIST.getSearchType()).equals(search));
    }

    @Test
    public void testEndSearch() {
        //Given
        String endSearch = "WrongTypo";
        // Mock dependencies
        ScannerWrapper scannerWrapper = mock(ScannerWrapper.class);

        // Configure behavior of mocks
        when(scannerWrapper.nextLine()).thenReturn(endSearch, endSearch, endSearch, endSearch, endSearch, endSearch, endSearch, endSearch, endSearch, endSearch, endSearch); // Simulate user input

        // Create instance of the class to be tested
        GetDataImpl testClass = new GetDataImpl(scannerWrapper);

        //When
        // Invoke the method
        boolean result = testClass.endSearch();

        //Then
        // Verify that the method behaves as expected
        Assertions.assertThat(result == true);
    }

    @Test
    public void testMockedService() {
        //Given
        Map<String, Object> response = new HashMap<>();
        String searchType = "2";
        String search = "Nirvana";
        // Mock dependencies
        MusicBrainzNameSearchRoute musicBrainzNameSearchRoute = mock(MusicBrainzNameSearchRoute.class);
        MusicBrainzIDSearchRoute musicBrainzIDSearchRoute = mock(MusicBrainzIDSearchRoute.class);
        WikidataSearchRoute wikidataSearchRoute = mock(WikidataSearchRoute.class);
        WikipediaSearchRoute wikipediaSearchRoute = mock(WikipediaSearchRoute.class);
        CoverArtArchiveService coverArtArchiveService = mock(CoverArtArchiveService.class);
        ScannerWrapper scannerWrapper = mock(ScannerWrapper.class);

        // Configure behavior of mocks
        when(scannerWrapper.nextLine()).thenReturn(searchType, search); // Simulate user input
        // when(musicBrainzNameSearchRoute.getMBID(anyMap())).thenReturn(response); // Simulate empty response
        // Create instance of the class to be tested
        GetDataImpl testClass = new GetDataImpl(scannerWrapper);

        //When
        String result = testClass.run();

        //Then
        Assertions.assertThat(result.isEmpty());
    }

    @Test
    public void testtestEndSearch() throws Exception {
        // Given
        String expectedMBID = "MockedMBID";
        ResponseEntity<String> mockedResponse = new ResponseEntity<>(expectedMBID, HttpStatus.OK);
        Map<String, String> typeOFsearch = new HashMap<>();
        typeOFsearch.put("2", TypeOfSearchEnum.ARTIST.toString());
        ScannerWrapper scannerMock = mock(ScannerWrapper.class);
        when(scannerMock.nextLine())
                .thenReturn("2")  // Simulate user selecting Artist
                .thenReturn("Nirvana");  // Simulate user entering search value

        MusicBrainzNameSearchRoute musicBrainzNameSearchRouteMock = mock(MusicBrainzNameSearchRoute.class);
        GetDataImpl getDataImpl = new GetDataImpl(scannerMock);
        // when(musicBrainzNameSearchRouteMock.getMBID(any(Map.class))).thenReturn((Map) mockedResponse);
        // when(getDataImpl.getTypeOfSearch()).thenReturn(typeOFsearch);


        // Act
        String result = getDataImpl.run();

        // Assert
        // Verify that mBID is set to expected value when the ARTIST case is executed
        // You might need to add a getter for mBID in GetDataImpl class for this assertion
        Mockito.verify(musicBrainzNameSearchRouteMock).getMBID(any(Map.class)); // Verify that getMBID method is called
        // Add more assertions as needed


    }

    @Test
    public void testJsonPrint(){
        // Given
        jsonRerponse();
        Map<String, Object> response = new HashMap<>();
        response.put("MBID", "1234");
        response.put("description", "description");
        Map<String, String> covers = new HashMap<>();
        covers.put("TitleName","1234");

        ScannerWrapper scannerMock = mock(ScannerWrapper.class);
        when(scannerMock.nextLine())
                .thenReturn("2")  // Simulate user selecting Artist
                .thenReturn("Nirvana");  // Simulate user entering search value
        GetDataImpl getDataImpl = new GetDataImpl(scannerMock);
        getDataImpl.setResponse(response);
        getDataImpl.setCovers(covers);
        List<Json.Album> album = new ArrayList<>();
        album.add(new Json.Album("Nevermind", "1b022e01-4da6-387b-8658-8678046e4cef", "https://coverartarchive.org/release/a146429a-cedc-3ab0-9e41-1aaf5f6cd"));

        when(Json.createJsonResponse(response,covers)).thenReturn(expectedjsonResponse);
        // Act
        getDataImpl.displayData();

        // Assert


    }

    @Test
    public void testJsonReponse() throws JSONException {
        // Given
        jsonRerponse();
        Map<String, Object> response = new HashMap<>();
        response.put("MBID", "1234");
        response.put("description", "description");
        Map<String, String> coverMetaData = new HashMap<>();
        coverMetaData.put("Live at Reading","48f5d526-0fa6-4ca6-ac59-9b2cf9ef464f");
        response.put("Covers", coverMetaData);
        Map<String, String> coverImageURL = new HashMap<>();
        coverImageURL.put("48f5d526-0fa6-4ca6-ac59-9b2cf9ef464f","http://coverartarchive.org/release/5c3257c8-6b2f-4860-9437-2410509443c7/5520440440.jpg");

        // When
        String jsonbody = Json.createJsonResponse(response,coverImageURL);

        // Then
        JSONAssert.assertEquals(jsonbody, expectedjsonResponse, false );
    }

    private void jsonRerponse() {
        expectedjsonResponse = "{\n" +
                "  \"mbid\": \"1234\",\n" +
                "  \"description\": \"description\",\n" +
                "  \"albums\": [\n" +
                "    {\n" +
                "      \"title\": \"Live at Reading\",\n" +
                "      \"id\": \"48f5d526-0fa6-4ca6-ac59-9b2cf9ef464f\",\n" +
                "      \"image\": \"http://coverartarchive.org/release/5c3257c8-6b2f-4860-9437-2410509443c7/5520440440.jpg\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";


               /* "{\"mbid\":\"5b11f4ce-a62d-471e-81fc-a69a8278c7da\",\"description\":\"<p class=\\\"mw-empty-elt\\\"" +
                ">\\n\\n\\n\\n</p>\\n<p><b>Nirvana</b> was an American rock band formed in Aberdeen, Washington, in 1987. " +
                "Founded by lead singer and guitarist Kurt Cobain and bassist Krist Novoselic, the band went through a " +
                "succession of drummers, most notably Chad Channing, before recruiting Dave Grohl in 1990. Nirvana's " +
                "success popularized alternative rock, and they were often referenced as the figurehead band of Generation X." +
                " Despite a short mainstream career spanning only three years, their music maintains a popular following " +
                "and continues to influence modern rock culture.\\n</p><p>In the late 1980s, Nirvana established itself " +
                "as part of the Seattle grunge scene, releasing its first album, <i>Bleach</i>, for the independent " +
                "record label Sub Pop in 1989. They developed a sound that relied on dynamic contrasts, often between " +
                "quiet verses and loud, heavy choruses. After signing to the major label DGC Records in 1991, Nirvana " +
                "found unexpected mainstream success with \\\"Smells Like Teen Spirit\\\", the first single from their " +
                "landmark second album <i>Nevermind</i> (1991). A cultural phenomenon of the 1990s, <i>Nevermind</i> was" +
                " certified Diamond by the Recording Industry Association of America (RIAA) and is credited for ending " +
                "the dominance of hair metal.</p><p>Characterized by their punk aesthetic, Nirvana's fusion of pop " +
                "melodies with noise, combined with their themes of abjection and social alienation, brought them global" +
                " popularity. Following extensive tours and the 1992 compilation album <i>Incesticide</i> and EP " +
                "<i>Hormoaning</i>, the band released their highly anticipated third studio album, <i>In Utero</i> " +
                "(1993). The album topped both the US and UK album charts, and was acclaimed by critics. Nirvana " +
                "disbanded following Cobain's suicide in April 1994. Further releases have been overseen by Novoselic," +
                " Grohl, and Cobain's widow, Courtney Love. The live album <i>MTV Unplugged in New York</i> (1994) won " +
                "Best Alternative Music Performance at the 1996 Grammy Awards.\\n</p><p>Nirvana is one of the " +
                "best-selling bands of all time, having sold more than 75 million records worldwide. During their " +
                "three years as a mainstream act, Nirvana received an American Music Award, Brit Award, and Grammy " +
                "Award, as well as seven MTV Video Music Awards and two NME Awards. They achieved five number-one hits " +
                "on the <i>Billboard</i> Alternative Songs chart and four number-one albums on the <i>Billboard</i> 200." +
                " In 2004, <i>Rolling Stone</i> named Nirvana among the 100 greatest artists of all time. They were " +
                "inducted into the Rock and Roll Hall of Fame in their first year of eligibility in 2014.\\n</p>\"," +
                "\"albums\":[{\"title\":\"Nevermind\",\"id\":\"1b022e01-4da6-387b-8658-8678046e4cef\",\"image\":" +
                "\"https://coverartarchive.org/release/a146429a-cedc-3ab0-9e41-1aaf5f6cd\"}]}\n";

                */
    }

}
