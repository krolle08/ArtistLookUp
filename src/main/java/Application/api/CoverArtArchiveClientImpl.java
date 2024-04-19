package Application.api;

import fm.last.musicbrainz.coverart.CoverArt;
import fm.last.musicbrainz.coverart.impl.DefaultCoverArtArchiveClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * API service to collect the imageURL for each album in the Album list
 */
@Service
public class CoverArtArchiveClientImpl {
    fm.last.musicbrainz.coverart.CoverArtArchiveClient client = new DefaultCoverArtArchiveClient();

    public CoverArt getForObject(UUID albumIdUUID) {
        return client.getReleaseGroupByMbid(albumIdUUID);
    }
}


