package service.musicbrainz;

import Application.Application;
import Application.service.Artist.ArtistInfoObj;
import Application.service.MusicBrainzService.MusicBrainzNameService;
import Application.utils.TypeOfSearchEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testMusicBrainzServiceImplNameService {
    @Autowired
    private MusicBrainzNameService musicBrainzNameService;

    @Test
    public void testMusicBrainzNameSearchExtractData() {
        // Given
        Map<String, String> fillterparams = new HashMap<>();
        ResponseEntity<String> mockedResponseEntity = ResponseEntity.ok(responseBody());

        fillterparams.put(TypeOfSearchEnum.ARTIST.getSearchType(), "Nirvana");

        // When
        ArtistInfoObj artistInfoObj = musicBrainzNameService.extractDataAndPopulateObj(mockedResponseEntity, fillterparams.get(TypeOfSearchEnum.ARTIST.getSearchType()));

        // Then
        assertFalse(artistInfoObj.getName().isEmpty());
        assertFalse(artistInfoObj.getmBID().isEmpty());
    }

    private String responseBody() {
 return "{\n" +
         "    \"annotations\": [\n" +
         "        {\n" +
         "            \"type\": \"recording\",\n" +
         "            \"score\": 100,\n" +
         "            \"entity\": \"4de01720-b633-44bf-81f1-44dc32656188\",\n" +
         "            \"name\": \"Asshole\",\n" +
         "            \"text\": \"This was circulated as a (fake) [http://musicbrainz.org/show/artist/?artistid=54|Nirvana] song.\"\n" +
         "        },\n" +
         "        {\n" +
         "            \"type\": \"artist\",\n" +
         "            \"score\": 90,\n" +
         "            \"entity\": \"6d797b95-2011-4859-91ab-9284d1e1f472\",\n" +
         "            \"name\": \"Skid Row\",\n" +
         "            \"text\": \"During their first few months, the group that would become [artist:5b11f4ce-a62d-471e-81fc-a69a8278c7da|Nirvana] played under several different names. ''Skid Row'' was one of these.\"\n" +
         "        },\n" +
         "        {\n" +
         "            \"type\": \"artist\",\n" +
         "            \"score\": 83,\n" +
         "            \"entity\": \"31649596-23c3-4454-a7b0-30615ed1f504\",\n" +
         "            \"name\": \"Kera Schaley\",\n" +
         "            \"text\": \"Cellist that has performed on studio recordings and/or live concerts for [http://musicbrainz.org/artist/5b11f4ce-a62d-471e-81fc-a69a8278c7da.html|Nirvana], [http://musicbrainz.org/artist/eeee6fb2-d69a-42e2-af8a-9899957abf70.html|Azure Ray], [http://musicbrainz.org/artist/b07cdbbe-421e-418a-bbb7-ce0d8d53bfa8.html|Vic Chesnutt], and [http://musicbrainz.org/artist/42faad37-8aaa-42e4-a300-5a7dae79ed24.html|Cat Power].\"\n" +
         "        }\n" +
         "    ]\n" +
         "}";
    }
}
