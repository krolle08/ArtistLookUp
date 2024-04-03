package service.musicbrainz;

import Application.YourApplication;
import Application.api.MusicBrainzIDSearchRoute;
import Application.service.ArtistContainer.ArtistInfoObj;
import Application.utils.URIException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testMusicBrainzIDSearchRoute {

    @Autowired
    private MusicBrainzIDSearchRoute musicBrainzIDSearchRoute;

    @Test
    public void testMBidEndpoint(){
        //Given
        String nirvana = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        String succescriteria = "Q11649";
        ArtistInfoObj artistInfoObj;

        //When
        try {
            artistInfoObj = musicBrainzIDSearchRoute.getArtistDataWithmbid(nirvana);
        } catch (URIException e) {
            throw new RuntimeException(e);
        }

        //Then
        assertThat(artistInfoObj.getName()).isEqualTo("Nirvana");
        assertThat(artistInfoObj.getmBStatusCode()).isEqualTo(200);
        assertThat(artistInfoObj.getWikiInfo().getWikidata()).isEqualTo(succescriteria);
        assertThat(!artistInfoObj.getAlbums().isEmpty());
        assertThat(artistInfoObj.getAlbums().size()).isEqualTo(16); // Check if it has a size of 16, prepare for updates if they release albums
    }
}
