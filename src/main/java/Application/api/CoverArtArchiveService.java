package Application.api;

import fm.last.musicbrainz.coverart.CoverArt;
import fm.last.musicbrainz.coverart.CoverArtArchiveClient;
import fm.last.musicbrainz.coverart.CoverArtImage;
import fm.last.musicbrainz.coverart.impl.DefaultCoverArtArchiveClient;
import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.util.Scanner;
import java.util.UUID;

public class CoverArtArchiveService {
    DefaultCoverArtArchiveClient defaultCoverArtArchiveClient = new DefaultCoverArtArchiveClient();
    CoverArtArchiveClient client = new DefaultCoverArtArchiveClient();
    public void runApplication(String artist) {
        UUID mbid = UUID.fromString("76df3287-6cda-33eb-8e9a-044b5e15ffdd");
        defaultCoverArtArchiveClient.getByMbid(mbid);

        CoverArt coverArt = null;
        try {
            coverArt = client.getByMbid(mbid);
            //coverArt = client.getReleaseGroupByMbid(mbid);
            if (coverArt != null) {
                for (CoverArtImage coverArtImage : coverArt.getImages()) {
                    File output = new File(mbid.toString() + "_" + coverArtImage.getId() + ".jpg");
                    FileUtils.copyInputStreamToFile(coverArtImage.getImage(), output);
                }
            }
        } catch (Exception e) {
            // ...
        }

        final boolean useHttps = true;
        CoverArtArchiveClient clients = new DefaultCoverArtArchiveClient(useHttps);
    }

    public void test(String test) {


        Scanner scanner = new Scanner(System.in);

        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Make a GET request to /hello endpoint
        ResponseEntity<String> helloResponse = restTemplate.getForEntity("http://localhost:8080/hello", String.class);
        System.out.println("Response from /hello endpoint: " + helloResponse.getBody());

        // Make a GET request to /farvel endpoint
        ResponseEntity<String> farvelResponse = restTemplate.getForEntity("http://localhost:8080/farvel", String.class);
        System.out.println("Response from /farvel endpoint: " + farvelResponse.getBody());

        scanner.close();
    }
}
