package service.musicbrainz;

import Application.api.MusicBrainzIDSearchRoute;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class testMockMusicBrainzIDSearchRoute {
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private MusicBrainzIDSearchRoute musicBrainzIDSearchRoute;
    @Test
    public void testSearchArtist() throws URISyntaxException {
        //Given
        // Mock the response from the external service
        String artistId = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        String responseBody = "Nirvana";

        //When
        // Create a mock ResponseEntity
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        // Stub the behavior of RestTemplate.getForEntity to return the mock ResponseEntity
      /*  Mockito.lenient().when(restTemplate.getForEntity(new URI("http://musicbrainz.org/ws/2/artist/" + artistId + "?inc=aliases"), String.class))
                .thenReturn(responseEntity);

       */
        // Create a mock ResponseEntity
        Map<String, Object> rest = musicBrainzIDSearchRoute.getDataFromArtist("5b11f4ce-a62d-471e-81fc-a69a8278c7da");

        //Then
        assertThat(rest.get("name").toString()).contains(responseBody);
    }


}
