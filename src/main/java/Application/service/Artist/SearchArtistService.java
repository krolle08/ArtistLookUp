package Application.service.Artist;

import Application.service.CoverArtArchive.CoverArtArchiveService;
import Application.service.MusicBrainz.MusicBrainzIdService;
import Application.service.MusicBrainz.MusicBrainzNameService;
import Application.service.Wikidata.WikidataService;
import Application.service.Wikipedia.WikiPediaService;
import Application.utils.TypeOfSearchEnum;
import Application.utils.URIException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The service handles the API calls and populates the MusicEntityObj that is used to process the final Json Response
 */
@Service
public class SearchArtistService {
    private static final Logger logger = LoggerFactory.getLogger(SearchArtistService.class.getName());
    private MusicBrainzNameService musicBrainzNameService;
    private MusicBrainzIdService musicBrainzIdService;
    private CoverArtArchiveService coverArtArchiveService;
    private WikiPediaService wikiPediaService;
    private WikidataService wikidataService;

    public SearchArtistService(MusicBrainzNameService musicBrainzNameService,
                               MusicBrainzIdService musicBrainzIdService,
                               CoverArtArchiveService coverArtArchiveService,
                               WikiPediaService wikiPediaService,
                               WikidataService wikidataService) {
        this.musicBrainzNameService = musicBrainzNameService;
        this.musicBrainzIdService = musicBrainzIdService;
        this.coverArtArchiveService = coverArtArchiveService;
        this.wikiPediaService = wikiPediaService;
        this.wikidataService = wikidataService;
    }


    public ArtistInfoObj searchArtist(Map<String, String> searchParam) {

        ArtistInfoObj entity = getMusicBrainzData(searchParam);

        musicBrainzNameService.getDataByName(searchParam);
        ArtistInfoObj newEntity;
        if (!IsMusicBrainzIdPresent(entity)) {
            // Populate the new ArtistInfoObj using params as id instead of name
            newEntity = musicBrainzIdService.getDataBymbid(searchParam
                    .get(TypeOfSearchEnum.ARTIST.getSearchType()));
        } else {
            // Populate the new ArtistInfoObj using the mbid extraced from the getDataByName() method
            newEntity = (musicBrainzIdService.getDataBymbid(entity.getmBID()));
        }
        updateEntity(entity, newEntity, searchParam.get(TypeOfSearchEnum.ARTIST.toString()));

        // Executor service with a fixed pool size
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // Fetch wikidata and wikipedia info concurrently
        CompletableFuture<Void> wikidataAndWikipediaFuture = CompletableFuture.runAsync(() -> {
            try {
                if (entity.getWikiInfo().getWikidata() != null) {
                    wikidataService.getWikidataForArtist(entity.getWikiInfo());
                }
                wikiPediaService.getWikidataForArtist(entity.getWikiInfo());
            } catch (URISyntaxException | JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed creating URI or processing response as Json", e);
            }
        }, executorService);

        // Fetch cover data concurrently with wikidata/wikipedia
        CompletableFuture<Void> coverArtFuture = CompletableFuture.runAsync(() -> {
            coverArtArchiveService.getCovers(entity.getAlbums());
        }, executorService);

        // Wait for all tasks to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(wikidataAndWikipediaFuture, coverArtFuture);
        allOf.join();
        return entity;
    }

    private ArtistInfoObj getMusicBrainzData(Map<String, String> searchParam) {
        ArtistInfoObj entity = musicBrainzNameService.getDataByName(searchParam);
        ArtistInfoObj newEntity;
        if (!IsMusicBrainzIdPresent(entity)) {
            // Populate the new ArtistInfoObj using params as id instead of name
            newEntity = musicBrainzIdService.getDataBymbid(searchParam
                    .get(TypeOfSearchEnum.ARTIST.getSearchType()));
        } else {
            // Populate the ArtistInfoObj using mbid
            newEntity = musicBrainzIdService.getDataBymbid(entity.getmBID());
        }
        updateEntity(entity, newEntity, searchParam.get(TypeOfSearchEnum.ARTIST.toString()));
        return entity;
    }

    private boolean IsMusicBrainzIdPresent(ArtistInfoObj entity) {
        return entity == null || entity.getmBID() == null || entity.getmBID().isEmpty();
    }

    private void updateEntity(ArtistInfoObj entity, ArtistInfoObj newEntity, String searchParam) {
        // Update name if not already set
        if (entity.getName() == null || entity.getName().isEmpty()) {
            entity.setName(newEntity.getName());
        } else if (!entity.getName().equals(newEntity.getName())) {
            throw new RuntimeException("Different names has been received doing the search of: " + searchParam +
                    " and when searching with the following mbid: " + entity.getmBID());
        }

        // Update WikiInfo if not already set
        if (entity.getWikiInfo() == null || entity.getWikiInfo().isEmpty()) {
            entity.setWikiInfo(newEntity.getWikiInfo());
        }
    }
}

