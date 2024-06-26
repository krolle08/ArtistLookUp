package Application.service.impl;

import Application.api.MusicBrainzClientImpl;
import Application.model.response.ArtistResponse;
import Application.parser.MusicBrainzParser;
import Application.service.MusicBrainzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicBrainzServiceImpl implements MusicBrainzService {

    @Autowired
    private MusicBrainzClientImpl musicBrainzClient;
    @Override
    public ArtistResponse getArtistInfo(String mBId) throws Exception {
        String url = musicBrainzClient.buildUrl(mBId);
        String response = musicBrainzClient.getForObject(url);
        ArtistResponse artistResponse = MusicBrainzParser.parseResponse(response);
        return artistResponse;
    }
}
