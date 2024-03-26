package Application.api;

import Application.service.AlbumInfo;
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
    CoverArtArchiveClient client = new DefaultCoverArtArchiveClient();


    public Map<String, String> getCovers(Map<String, String> covers) {
        Map<String, Object> imageURLAndId = new HashMap<>();
        try {
            covers.forEach((title, id) -> {
                UUID coverId = UUID.fromString(id);
                Map<String, Object> extractedData = new HashMap<>();
                CoverArt coverArt = null;
                //coverArt = client.getByMbid(coverId);
                coverArt = client.getReleaseGroupByMbid(coverId);
                if (coverArt != null) {
                    for (CoverArtImage coverArtImage : coverArt.getImages()) {
                        AlbumInfo album = new AlbumInfo(id, title, coverArtImage.getImageUrl());
                        if (album instanceof Map) {
                            imageURLAndId.put(id, album);
                        } else {
                            log.warn("Object CoverArtArchiveImpl is not an instanceof Map");
                        }
                    }
                } else {
                    log.warn("No images found for album title: " + title + " with the coverid: " + id);
                }
                ;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageURLAndId;
    }
}
