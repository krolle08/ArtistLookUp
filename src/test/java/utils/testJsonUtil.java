package utils;

import Application.YourApplication;
import Application.service.*;
import Application.service.ArtistContainer.AlbumInfoObj;
import Application.service.ArtistContainer.ArtistInfoObj;
import Application.service.ArtistContainer.WikiInfoObj;
import Application.utils.Json;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testJsonUtil {
    @Test
    public void testJsonPrint() {
        // Given
        MusicEntityObj entity = createMusicEntity();
        String expectedJson = createExpectedOutput();

        // When
        String actualJson = Json.createJsonResponse(entity);

        // Parse JSON strings into JSON objects'
        com.google.gson.JsonObject actualObject = JsonParser.parseString(actualJson).getAsJsonObject();
        com.google.gson.JsonObject expectedObject = JsonParser.parseString(expectedJson).getAsJsonObject();

        //Then
        assertEquals(expectedObject, actualObject);
    }

    private MusicEntityObj createMusicEntity() {
        MusicEntityObj entity = new MusicEntityObj();
        ArtistInfoObj artistInfoObj = new ArtistInfoObj("Nirvana", "1234");
        WikiInfoObj wikiInfoObj = new WikiInfoObj("wikidata", "wikipedia");
        wikiInfoObj.setDescription("description");
        entity.setArtistInfo(artistInfoObj);
        entity.getArtistInfo().setWikiInfo(wikiInfoObj);
        List<AlbumInfoObj> albums = new ArrayList<>();
        albums.add(new AlbumInfoObj("1b022e01-4da6-387b-8658-8678046e4cef", "Nevermind"));
        albums.get(0).setImageURL("https://coverartarchive.org/release/a146429a-cedc-3ab0-9e41-1aaf5f6cd");
        entity.getArtistInfo().setAlbums(albums);
        return entity;
    }

    private String createExpectedOutput() {
        return "{\"mbid\":\"1234\",\"description\":\"description\",\"albums\":[{\"title\":\"Nevermind\",\"id\":\"1b022e01-4da6-387b-8658-8678046e4cef\",\"image\":\"https://coverartarchive.org/release/a146429a-cedc-3ab0-9e41-1aaf5f6cd\"}]}";
    }

}
