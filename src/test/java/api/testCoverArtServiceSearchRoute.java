package api;

import Application.Application;
import Application.service.Artist.AlbumInfoObj;
import fm.last.musicbrainz.coverart.CoverArt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class testCoverArtServiceSearchRoute {

    @Autowired
    CoverArtArchiveSearchRoute coverArtArchiveSearchRoute;

    @Test
    public void testCoverArtEndpoint_Succes() {
        //Given
        List<AlbumInfoObj> albums = new ArrayList<>();
        AlbumInfoObj album1 = new AlbumInfoObj("01cf1391-141b-3c87-8650-45ade6e59070", "Incesticide");
        AlbumInfoObj album2 = new AlbumInfoObj("249e7835-5c39-3a10-b15b-e2d3470fb40c", "From the Muddy Banks of the Wishkah");
        AlbumInfoObj album3 = new AlbumInfoObj("2a0981fb-9593-3019-864b-ce934d97a16e", "In Utero");
        albums.add(album1);
        albums.add(album2);
        albums.add(album3);
        //When
        CoverArt coverArt = coverArtArchiveSearchRoute.doGetCoverData(album1.getAlbumId());

        //Then
        Assertions.assertThat(coverArt).as("Albums list is not null").isNotNull();
        Assertions.assertThat(coverArt.getImages().isEmpty()).isFalse();
        Assertions.assertThat(coverArt.getImages().stream()
                .anyMatch(album -> album.getImageUrl().isEmpty())).isFalse();
    }

    @Test
    public void testIllegalArgumentExceptionThrownForInvalidUUID() {
        // Given
        String Invallid ="InvalidUUID";

        //When Then
        assertThrows(IllegalArgumentException.class, () -> coverArtArchiveSearchRoute.doGetCoverData(Invallid));
    }
}
