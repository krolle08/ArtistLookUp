package Application.service.impl;

import Application.api.MusicBrainzClient;
import Application.model.response.ArtistResponse;
import Application.service.ArtistService;
import Application.service.Executor.WikiAndCoverArtExecutorService;
import Application.service.MusicBrainz.MusicBrainzService;
import Application.service.MusicBrainzConfig;
import Application.utils.AppUtils;
import Application.utils.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * The service handles the API calls and populates the MusicEntityObj that is used to process the final Json Response
 */
@Service
public class ArtistServiceImpl implements ArtistService {
    Logger logger = LoggerFactory.getLogger(ArtistServiceImpl.class);

    private final MusicBrainzClient musicBrainzClient;
    private MusicBrainzConfig musicBrainzConfig;
    @Autowired
    public ArtistServiceImpl(MusicBrainzClient musicBrainzClient,
                             MusicBrainzConfig musicBrainzConfig) {
        this.musicBrainzClient = musicBrainzClient;
        this.musicBrainzConfig = musicBrainzConfig;
    }

    @Override
    public String getArtistData(String mBId){
        AppUtils.validateMusicBrainzId(mBId);

        ArtistResponse artistResponse;
        try{
          artistResponse = new ObjectMapper().readValue(doHttpRequest("/ws/2/artist/:" + mBId), ArtistResponse.class);
          if(artistResponse.getmBID() != null || !artistResponse.getmBID().isEmpty()){
              musicBrainzClient.getMBData(mBId);

              wikiAndCoverArtExecutorService.getWikiAndCoverData(entity);

          }
        }catch (Exception e){

        }

        return Json.createJsonResponse(entity);

    }


    public String doHttpRequest(String uri) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(buildUri(uri).toString(), String.class);
        logger.debug("doHttpRequest::get result: [{}]", result);
        return result;
    }

    /**
     * Build uri
     *
     * @param path
     *
     * @return
     *
     * @throws URISyntaxException
     */
    private URI buildUri(String path) throws URISyntaxException {
        return new URI(musicBrainzConfig.getHost() + path + "?fmt=json&inc=url-rels+release-groups");
    }

}

