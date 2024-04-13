package Application.service.CoverArtArchive;

import Application.api.CoverArtArchiveSearchRoute;
import Application.service.Artist.AlbumInfoObj;
import Application.utils.LoggingUtility;
import fm.last.musicbrainz.coverart.CoverArt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * API service to collect the imageURL for each album in the AlbumInfoObj list
 */
@Service
public class CoverArtArchiveService {
    @Autowired
    private CoverArtArchiveSearchRoute coverArtArchiveSearchRoute;

    public void getCoverData(List<AlbumInfoObj> albums) {
        albums.forEach(album -> {
                CoverArt coverArt = coverArtArchiveSearchRoute.doGetCoverData(album.getAlbumId());
                if (coverArt != null) {
                    coverArt.getImages().stream().findFirst().ifPresent(coverArtImage -> {
                        album.setImageURL(coverArtImage.getImageUrl());
                        album.setAlbumId(String.valueOf(coverArtImage.getId()));
                    });
                } else {
                    LoggingUtility.warn("No images found for album title: " + album.getTitle() + " with the cover ID: " + album.getiD());
                    album.setImageURL("No URL found for album ID: " + album.getAlbumId());
                }
        });
    }
}


