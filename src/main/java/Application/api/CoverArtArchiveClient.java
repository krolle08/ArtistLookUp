package Application.api;

import fm.last.musicbrainz.coverart.CoverArt;
import fm.last.musicbrainz.coverart.impl.DefaultCoverArtArchiveClient;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * API service to collect the imageURL for each album in the Album list
 */
@RestController
public class CoverArtArchiveClient {
    fm.last.musicbrainz.coverart.CoverArtArchiveClient client = new DefaultCoverArtArchiveClient();

    public CoverArt doGetCoverData(String albumId) {
        UUID albumIdUUID = UUID.fromString(albumId);
        return client.getReleaseGroupByMbid(albumIdUUID);
    }
}


