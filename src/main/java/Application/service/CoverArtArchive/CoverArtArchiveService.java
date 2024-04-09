package Application.service.CoverArtArchive;

import Application.api.CoverArtArchiveSearchRoute;
import Application.service.Artist.AlbumInfoObj;
import fm.last.musicbrainz.coverart.CoverArt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * API service to collect the imageURL for each album in the AlbumInfoObj list
 */
@Service
public class CoverArtArchiveService {
    private final Logger logger = LoggerFactory.getLogger(CoverArtArchiveService.class.getName());
    @Autowired
    private CoverArtArchiveSearchRoute coverArtArchiveSearchRoute;

    public void getCoverData(List<AlbumInfoObj> albums) {
        albums.forEach(album -> {
            try {
                CoverArt coverArt = coverArtArchiveSearchRoute.doGetCoverData(album.getAlbumId());
                if (coverArt != null) {
                    coverArt.getImages().stream().findFirst().ifPresent(coverArtImage -> {
                        album.setImageURL(coverArtImage.getImageUrl());
                        album.setAlbumId(String.valueOf(coverArtImage.getId()));
                    });
                } else {
                    logger.warn("No images found for album title: " + album.getTitle() + " with the cover ID: " + album.getiD());
                    album.setImageURL("No URL found for album ID: " + album.getAlbumId());
                }
            } catch (IllegalArgumentException e) {
                logger.error("Invalid UUID format for album ID: " + album.getAlbumId());
            }
        });
    }
}


