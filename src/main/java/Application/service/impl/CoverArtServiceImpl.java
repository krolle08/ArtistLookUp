package Application.service.impl;

import Application.api.CoverArtArchiveClientImpl;
import Application.model.Album;
import Application.model.response.AlbumResponse;
import Application.service.CoverArtService;
import Application.utils.LoggingUtility;
import fm.last.musicbrainz.coverart.CoverArt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CoverArtServiceImpl implements CoverArtService {

    @Autowired
    CoverArtArchiveClientImpl coverArtArchiveClient;

    @Override
    public List<AlbumResponse> getImageUrls(List<Album> albums) throws Exception {
        List<AlbumResponse> updatedAlbums = new ArrayList<>();
        for (Album album : albums) {
            UUID url = UUID.fromString(album.getAlbumId());
            CoverArt coverArt = coverArtArchiveClient.getForObject(url);
            if (coverArt != null) {
                coverArt.getImages().stream().findFirst().ifPresent(coverArtImage -> {
                    updatedAlbums.add(new AlbumResponse(album.getTitle(), String.valueOf(coverArtImage.getId()), coverArtImage.getImageUrl()));
                });
            } else {
                LoggingUtility.warn("No images found for albumid: " + album.getAlbumId());
                updatedAlbums.add(new AlbumResponse(album.getTitle(), album.getAlbumId(), "No imageURL found"));
            }
        }
        return updatedAlbums;
    }
}
