package service.coverart;

import Application.YourApplication;
import Application.api.CoverArtArchiveService;
import Application.api.TestService;
import Application.service.AlbumInfo;
import Application.service.ArtistInfo;
import Application.service.MusicEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testCoverArtSearchRoute {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private CoverArtArchiveService coverArtArchiveService;

    @Test
    public void testHelloWorldEndpoint(){
        //Given
        String nirvana = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        String succescriteria = "http://coverartarchive.org/release/df89fb27-14a6-4814-87a4-39b7d9698e4d/38239449645.jpg";

        MusicEntity result = new MusicEntity();
        ArtistInfo artistInfo = new ArtistInfo();
        List<AlbumInfo> albums = new ArrayList<>();
        AlbumInfo album1 = new AlbumInfo("01cf1391-141b-3c87-8650-45ade6e59070","Incesticide", null);
        AlbumInfo album2 = new AlbumInfo("249e7835-5c39-3a10-b15b-e2d3470fb40c","From the Muddy Banks of the Wishkah", null);
        AlbumInfo album3 = new AlbumInfo("2a0981fb-9593-3019-864b-ce934d97a16e","In Utero", null);
        albums.add(album1);
        albums.add(album2);
        albums.add(album3);
        artistInfo.setAlbums(albums);
        result.setArtistInfo(artistInfo);

        //When
        coverArtArchiveService.getCovers(albums);

        //Then
        assertThat(result.getArtistInfo().getAlbums().stream().anyMatch(album-> album.getiD().equals(succescriteria)));
    }

    @Test
    public void testaftest(){
        // Given
        String expectedMBID = "MockedMBID";
        //Given
        String nirvana = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        String succescriteria = "http://coverartarchive.org/release/df89fb27-14a6-4814-87a4-39b7d9698e4d/38239449645.jpg";
        Map<String, String> covers = new HashMap<>();
        covers.put("Incesticide","01cf1391-141b-3c87-8650-45ade6e59070");
        covers.put("From the Muddy Banks of the Wishkah","249e7835-5c39-3a10-b15b-e2d3470fb40c");
        covers.put("Bleach","f1afec0b-26dd-3db5-9aa1-c91229a74a24");
        covers.put("In Utero","2a0981fb-9593-3019-864b-ce934d97a16e");
        //When
        TestService test = new TestService();
        Map<String, String> result = test.getCovers(covers);

        //Then
    }
}
