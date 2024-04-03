package service.musicbrainz;

import Application.YourApplication;
import Application.api.MusicBrainzNameSearchRoute;
import Application.service.ArtistContainer.ArtistInfoObj;
import Application.service.TypeOfSearchEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testMusicBrainzNameSearchRoute {
    @Autowired
    private MusicBrainzNameSearchRoute musicBrainzNameSearchRoute;

    @Test
    public void testMusicBrainzWithNameSearch_Nirvana() {
        //Given
        Map<String, String> nirvana = new HashMap<>();
        nirvana.put(TypeOfSearchEnum.ARTIST.getSearchType(), "Nirvana");
        String mBid = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        //When
        ArtistInfoObj artistInfoObj = null;
        try {
            artistInfoObj = musicBrainzNameSearchRoute.getArtistMBID(nirvana);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //Then
        assertThat(artistInfoObj.getName()).isEqualTo("Nirvana");
        assertThat(artistInfoObj.getmBStatusCode()).isEqualTo(200);
        assertThat(artistInfoObj.getmBID()).isEqualTo(mBid);
    }

    @Test
    public void testMusicBrainzWithNameSearch_Slipknot() {
        //Given
        Map<String, String> slipknot = new HashMap<>();
        slipknot.put(TypeOfSearchEnum.ARTIST.getSearchType(), "Slipknot");
        String MBID = "a466c2a2-6517-42fb-a160-1087c3bafd9f";
        ArtistInfoObj artistInfoObj = new ArtistInfoObj();
        //When
        try {
            artistInfoObj = musicBrainzNameSearchRoute.getArtistMBID(slipknot);
        } catch (JsonProcessingException e) {

        }

        //Then
        assertThat(artistInfoObj.getName()).isEqualTo("Slipknot");
        assertThat(artistInfoObj.getmBStatusCode()).isEqualTo(200);
        assertThat(artistInfoObj.getmBID()).isEqualTo(MBID);
    }

    @Test
    public void testMusicBrainzNameSearchWithErrorInName() {
        // Given
        Map<String, String> ErrorInSearch = new HashMap<>();
        ErrorInSearch.put(TypeOfSearchEnum.ARTIST.getSearchType(), "ErrorInSearch");
        // Create a spy on Logger
        Logger logger = spy(Logger.getLogger(MusicBrainzNameSearchRoute.class.getName()));
        // Mock external dependencies
        URI mockedUri;
        try {
            mockedUri = new URI("http://example.com");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        ResponseEntity<String> mockedResponseEntity = ResponseEntity.ok("{}");

        MusicBrainzNameSearchRoute musicBrainzNameSearchRoute = Mockito.mock(MusicBrainzNameSearchRoute.class);

        // Mock createURI method
        doReturn(mockedUri).when(musicBrainzNameSearchRoute).createURI(ErrorInSearch);
        // Mock getResponse method
        when(musicBrainzNameSearchRoute.getResponse(mockedUri)).thenReturn(mockedResponseEntity);



        // When
        ArtistInfoObj artistInfoObj = new ArtistInfoObj();
        try {
            artistInfoObj = musicBrainzNameSearchRoute.getArtistMBID(ErrorInSearch);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Level effectiveLogLevel = null;
        Logger currentLogger = logger;

        while (currentLogger != null) {
            effectiveLogLevel = currentLogger.getLevel();
            if (effectiveLogLevel != null) {
                break;
            }
            currentLogger = currentLogger.getParent();
        }

        System.out.println("Effective log level: " + effectiveLogLevel);

        // Then
        assertThat(artistInfoObj.getName()).isNull();
        assertThat(artistInfoObj.getmBID()).isNull();
        assertThat(artistInfoObj.getAlbums()).isEmpty();
        verify(logger, times(1)).warning("No response was given on the provided URI: ");
    }
}
