package service.coverart;

import Application.YourApplication;
import Application.api.CoverArtArchiveService;
import Application.service.ArtistContainer.AlbumInfoObj;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testCoverArtSearchRoute {
    @Test
    public void testCoverArtEndpoint() {
        //Given
        String imageUrl = "http://coverartarchive.org/release/df89fb27-14a6-4814-87a4-39b7d9698e4d/38239449645.jpg";

        List<AlbumInfoObj> albums = new ArrayList<>();
        AlbumInfoObj album1 = new AlbumInfoObj("01cf1391-141b-3c87-8650-45ade6e59070", "Incesticide");
        AlbumInfoObj album2 = new AlbumInfoObj("249e7835-5c39-3a10-b15b-e2d3470fb40c", "From the Muddy Banks of the Wishkah");
        AlbumInfoObj album3 = new AlbumInfoObj("2a0981fb-9593-3019-864b-ce934d97a16e", "In Utero");
        albums.add(album1);
        albums.add(album2);
        albums.add(album3);
        CoverArtArchiveService coverArtArchiveService = new CoverArtArchiveService();
        //When
        coverArtArchiveService.getCovers(albums);

        //Then
        Assertions.assertThat(albums).as("Albums list is not null").isNotNull();
        Assertions.assertThat(albums)
                .filteredOn(album -> imageUrl.equals(album.getImageURL()))
                .as("Only one album has a matching imageUrl")
                .hasSize(1);
    }

    @Test
    public void testIllegalArgumentExceptionThrownForInvalidUUID() {
        // Given
        List<AlbumInfoObj> albums = new ArrayList<>();
        albums.add(new AlbumInfoObj("InvalidUUID", "Incesticide"));

        // Create a spy on Logger
        Logger logger = spy(Logger.getLogger(CoverArtArchiveService.class.getName()));
        // Create an instance of CoverArtArchiveService with the spy logger
        CoverArtArchiveService service = new CoverArtArchiveService();
        service.setLogger(logger);

        //When
        service.getCovers(albums);

        //Then
        verify(logger, times(1)).severe("Invalid UUID format for album ID: InvalidUUID");
    }
}
