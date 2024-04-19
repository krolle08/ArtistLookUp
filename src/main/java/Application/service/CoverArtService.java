package Application.service;

import Application.model.Album;
import Application.model.response.AlbumResponse;

import java.util.List;

public interface CoverArtService {
    public List<AlbumResponse> getImageUrls(List<Album> albums) throws Exception;

}
