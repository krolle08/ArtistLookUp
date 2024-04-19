package Application.service;

import Application.model.response.ArtistResponse;

public interface MusicBrainzService {

    // Specific logic to interact with MusicBrainz API
    public ArtistResponse getArtistInfo(String mbid) throws Exception;
}
