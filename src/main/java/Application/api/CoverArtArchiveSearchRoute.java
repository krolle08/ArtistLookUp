package Application.api;

import fm.last.musicbrainz.coverart.CoverArt;
import fm.last.musicbrainz.coverart.CoverArtArchiveClient;
import fm.last.musicbrainz.coverart.impl.DefaultCoverArtArchiveClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * API service to collect the imageURL for each album in the AlbumInfoObj list
 */
@RestController
public class CoverArtArchiveSearchRoute {
    private Logger logger = LoggerFactory.getLogger(CoverArtArchiveSearchRoute.class.getName());
    CoverArtArchiveClient client = new DefaultCoverArtArchiveClient();

    public CoverArt doGetCoverData(String albumId) {
        UUID albumIdUUID = UUID.fromString(albumId);
        return client.getReleaseGroupByMbid(albumIdUUID);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}


