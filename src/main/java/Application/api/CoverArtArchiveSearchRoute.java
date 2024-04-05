package Application.api;

import Application.service.Artist.AlbumInfoObj;
import fm.last.musicbrainz.coverart.CoverArt;
import fm.last.musicbrainz.coverart.CoverArtArchiveClient;
import fm.last.musicbrainz.coverart.impl.DefaultCoverArtArchiveClient;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * API service to collect the imageURL for each album in the AlbumInfoObj list
 */
@RestController
public class CoverArtArchiveSearchRoute {
    private Logger logger = Logger.getLogger(CoverArtArchiveSearchRoute.class.getName());
    CoverArtArchiveClient client = new DefaultCoverArtArchiveClient();

    public void doGetCoverData(List<AlbumInfoObj> albums) {
        albums.forEach(album -> {
            try {
                UUID albumId = UUID.fromString(album.getAlbumId());
                CoverArt coverArt = client.getReleaseGroupByMbid(albumId);
                if (coverArt != null) {
                    coverArt.getImages().stream().findFirst().ifPresent(coverArtImage -> {
                        album.setImageURL(coverArtImage.getImageUrl());
                        album.setAlbumId(String.valueOf(coverArtImage.getId()));
                    });
                } else {
                    logger.warning("No images found for album title: " + album.getTitle() + " with the cover ID: " + album.getiD());
                    album.setImageURL("No URL found for album ID: " + album.getAlbumId());
                }
            } catch (IllegalArgumentException e) {
                logger.severe("Invalid UUID format for album ID: " + album.getAlbumId());
            }
        });
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}


