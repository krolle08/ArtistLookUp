package Application.service;

import Application.api.*;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.net.URISyntaxException;
import java.util.Map;

/**
 * I include the Scanner in the constructor when the class relies on user input and it is unlikely that
 * the scanner needs to be swapped out for another Scanner instance during the class's lifetime. It simplifies method
 * signatures and encapsulates the dependency, resulting in cleaner code.
 */
public class SearchArtistService {

    private Log log = LogFactory.getLog(SearchArtistService.class);
    private MusicBrainzNameSearchRoute musicBrainzNameSearchRoute;
    private MusicBrainzIDSearchRoute musicBrainzIDSearchRoute;
    private CoverArtArchiveService coverArtArchiveService;
    private WikidataSearchRoute wikidataSearchRoute;
    private WikipediaSearchRoute wikipediaSearchRoute;

    public SearchArtistService(MusicBrainzNameSearchRoute musicBrainzNameSearchRoute,
                               MusicBrainzIDSearchRoute musicBrainzIDSearchRoute,
                               CoverArtArchiveService coverArtArchiveService,
                               WikidataSearchRoute wikidataSearchRoute,
                               WikipediaSearchRoute wikipediaSearchRoute) {
        this.musicBrainzNameSearchRoute = musicBrainzNameSearchRoute;
        this.musicBrainzIDSearchRoute = musicBrainzIDSearchRoute;
        this.coverArtArchiveService = coverArtArchiveService;
        this.wikidataSearchRoute = wikidataSearchRoute;
        this.wikipediaSearchRoute = wikipediaSearchRoute;
    }

    public MusicEntity searchArtist(Map<String, String> searchParam) throws URISyntaxException, RuntimeException {
        MusicEntity entity = new MusicEntity();
        ArtistInfo artistInfo = (musicBrainzNameSearchRoute.getArtistMBID(searchParam));
        ArtistInfo newArtistInfo;
            if (artistInfo == null || artistInfo.getmBID() == null || artistInfo.getmBID().isEmpty()) {
                // Get the new ArtistInfo from the first search method
                newArtistInfo = musicBrainzIDSearchRoute.getDataWithMBID(searchParam.get(TypeOfSearchEnum.ARTIST.getSearchType()));
                updateArtistInfoNameField(artistInfo, newArtistInfo, searchParam.get(TypeOfSearchEnum.ARTIST.getSearchType()));
            } else {
                newArtistInfo = (musicBrainzIDSearchRoute.getDataWithMBID(artistInfo.getmBID()));
                updateArtistInfoNameField(artistInfo, newArtistInfo, searchParam.get(TypeOfSearchEnum.ARTIST.toString()));
            }
            entity.setArtistInfo(artistInfo);
            if (entity.getArtistInfo().getName().isEmpty()) {
                log.warn("No information available for the provided input neither as an Artist or Music " +
                        "Brainz ID:" + searchParam.get(TypeOfSearchEnum.ARTIST.toString()));
                return entity;
            }
            if (entity.getArtistInfo().getWikiInfo().getWikidata() != null) {
                wikidataSearchRoute.getWikidataForArtist(entity.getArtistInfo().getWikiInfo());
            }
            wikipediaSearchRoute.wikipediaService(entity.getArtistInfo().getWikiInfo());
            // entity.getArtistInfo().setWikiInfo(wikiInfo);

            // Extract covers map from the response
            coverArtArchiveService.getCovers(entity.getArtistInfo().getAlbums());
        return entity;
    }

    private void updateArtistInfoNameField(ArtistInfo existingInfo, ArtistInfo newInfo, String searchParam) {
        // Update additional fields if necessary
        if (existingInfo.getName() == null || existingInfo.getName().isEmpty()) {
            if (!existingInfo.getName().equals(newInfo.getName())) {
                throw new RuntimeException("Different names has been received doing the search input: " + searchParam + " and when searching with the following mbid: " + existingInfo.getmBID());
            }
        }
        if (existingInfo.getWikiInfo() == null || existingInfo.getWikiInfo().getWikidata() == null) {
            existingInfo.setWikiInfo(newInfo.getWikiInfo());
        }

        if (existingInfo.getAlbums() == null || existingInfo.getAlbums().isEmpty()) {
            existingInfo.setAlbums(newInfo.getAlbums());
        }
    }
}

