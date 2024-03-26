package Application.api;

import fm.last.musicbrainz.coverart.CoverArt;
import fm.last.musicbrainz.coverart.CoverArtArchiveClient;
import fm.last.musicbrainz.coverart.CoverArtImage;
import fm.last.musicbrainz.coverart.impl.DefaultCoverArtArchiveClient;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class CoverArtArchiveService {

    private Log log = LogFactory.getLog(MusicBrainzIDSearchRoute.class);
    private final String protocol = "http";
    private final String schemeDelimiter = "://";
    private final String host = "musicbrainz.org";
    private final Integer port = 80;
    private final String pathPrefix = "/ws";
    private final String version = "/2";
    private final String queryTypeArtist = "/artist/";
    private static final String pathPostFix = "?fmt=json&inc=url-rels+release-groups";
    private Map<String, String> result = new HashMap<>();
    DefaultCoverArtArchiveClient defaultCoverArtArchiveClient = new DefaultCoverArtArchiveClient();
    CoverArtArchiveClient client = new DefaultCoverArtArchiveClient();

    public Map<String, String> getCovers(Map<String, String> covers) {
        Map<String, String> imageURLAndId = new HashMap<>();
        try {
            covers.forEach((title, id) -> {
                UUID coverId = UUID.fromString(id);
                Map<String, Object> extractedData = new HashMap<>();
                CoverArt coverArt = null;
                coverArt = client.getByMbid(coverId);
                coverArt = client.getReleaseGroupByMbid(coverId);
                if (coverArt != null) {
                    for (CoverArtImage coverArtImage : coverArt.getImages()) {
                        imageURLAndId.put(id,coverArtImage.getImageUrl());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageURLAndId;
    }
}
