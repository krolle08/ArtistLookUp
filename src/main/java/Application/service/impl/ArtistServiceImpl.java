package Application.service.impl;

import Application.model.Album;
import Application.model.ArtistDetails;
import Application.model.ArtistInfo;
import Application.model.response.AlbumResponse;
import Application.model.response.ArtistResponse;
import Application.model.response.WikiDataResponse;
import Application.model.response.WikipediaResponse;
import Application.service.ArtistService;
import Application.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The service handles the API calls and populates the MusicEntityObj that is used to process the final Json Response
 */
@Service
public class ArtistServiceImpl implements ArtistService {
    private final Logger logger = LoggerFactory.getLogger(ArtistServiceImpl.class);

    private final MusicBrainzServiceImpl musicBrainzService;
    private final WikidataServiceImpl wikidataService;
    private final WikipediaServiceImpl wikipediaService;
    private final CoverArtServiceImpl coverArtService;

    @Autowired
    public ArtistServiceImpl(MusicBrainzServiceImpl musicBrainzService,
                             WikidataServiceImpl wikidataService,
                             WikipediaServiceImpl wikipediaService,
                             CoverArtServiceImpl coverArtService) {
        this.musicBrainzService = musicBrainzService;
        this.wikidataService = wikidataService;
        this.wikipediaService = wikipediaService;
        this.coverArtService = coverArtService;
    }

    @Override
    public ArtistDetails getData(String mBId) throws Exception {
        AppUtils.validateMusicBrainzId(mBId);
        ArtistInfo artistInfo = new ArtistInfo(mBId);
        ArtistDetails artistDetails = getArtistDetails(artistInfo);
        return artistDetails;
    }

    private ArtistDetails getArtistDetails(ArtistInfo artistInfo) throws Exception {
        ArtistResponse artistResponse = getArtistInfoFromMusicBrainz(artistInfo.getmBId());
        String wikiDataId = artistResponse.getWikidataId();

        WikipediaResponse wikipediaResponse = null;
        if (wikiDataId != null) {
            WikiDataResponse wikiDataResponse = getWikipediaValueFromWikiData(wikiDataId); // Delegate to specific service
            wikipediaResponse = getDescriptionFromWikipedia(wikiDataResponse.getId());
        }

        List<AlbumResponse> albums = null;
        if (artistResponse.getAlbums() != null || !artistResponse.getAlbums().isEmpty()) {
            albums = getImageUrlFromCoverArt(artistResponse.getAlbums());
        }
        return new ArtistDetails(artistResponse.getmBID(), wikipediaResponse.getDescription(), albums);
    }

    private ArtistResponse getArtistInfoFromMusicBrainz(String mBId) throws Exception {
        return musicBrainzService.getArtistInfo(mBId);

    }

    private WikiDataResponse getWikipediaValueFromWikiData(String wikiDataId) throws Exception {
        return wikidataService.getWikipediaId(wikiDataId);
    }

    private WikipediaResponse getDescriptionFromWikipedia(String wikiPediaId) throws Exception {
        return wikipediaService.getDescription(wikiPediaId);
    }

    private List<AlbumResponse> getImageUrlFromCoverArt(List<Album> albums) throws Exception {
        return coverArtService.getImageUrls(albums);
    }
}

