package Application.api;

import Application.model.response.ArtistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * API service collecting the artist name, coverids, wikidata and wikipedia data if present based on the MusicBrainzClientImpl ID
 * It must be ensured that the API never make more than ONE call per second
 * <a href="https://musicbrainz.org/doc/MusicBrainz_API">...</a>
 */
@Service
public class MusicBrainzClientImpl{

    @Value("${musicBrainz.url}")
    private String musicBrainzBaseUrl;
    @Value("${musicBrainz.param}")
    private String musicBrainzParam;
    @Autowired
    private RestTemplate restTemplate;

    public String buildUrl(String mBId) {
        return musicBrainzBaseUrl + mBId + musicBrainzParam;
    }

    public ResponseEntity<ArtistResponse> getForObject(String url) throws Exception {
        ResponseEntity<ArtistResponse> response = restTemplate.getForEntity(url, ArtistResponse.class);
        return response;
    }
}
