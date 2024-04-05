package Application.service.Artist;

import Application.service.CoverArtArchive.CoverArtArchiveService;
import Application.service.DataProcessor;
import Application.service.MusicBrainz.MusicBrainzIdService;
import Application.service.MusicBrainz.MusicBrainzNameService;
import Application.service.Wikidata.WikidataService;
import Application.service.Wikipedia.WikiPediaService;
import Application.utils.ServiceProcessingException;
import Application.utils.TypeOfSearchEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The service handles the API calls and populates the MusicEntityObj that is used to process the final Json Response
 */
@Service
public class SearchArtistService implements DataProcessor<ArtistInfoObj> {
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

    @Override
    public ArtistInfoObj getData(Map<String, String> searchParam) {
        ArtistInfoObj entity = getMusicBrainzData(searchParam);
        getWikiAndCoverData(entity);
        return entity;
    }

    private ArtistInfoObj getMusicBrainzData(Map<String, String> searchParam) {
        String searchValue = searchParam.get(TypeOfSearchEnum.ARTIST.getSearchType());
        ArtistInfoObj entity;
        if (isValidUUID(searchValue)) {
            entity = musicBrainzIdService.getMBData(searchValue);
            return entity;
        } else {
            entity = musicBrainzNameService.getMBData(searchParam);
            ArtistInfoObj newEntity;
            if (IsMusicBrainzIdPresent(entity)) {
                // Populate the ArtistInfoObj using mbid
                newEntity = musicBrainzIdService.getMBData(entity.getmBID());
                updateEntity(entity, newEntity, searchParam.get(TypeOfSearchEnum.ARTIST.toString()));
            }
        }
        return entity;
    }

    private boolean isValidUUID(String id) {
        try {
            // Attempt to parse the ID as a UUID
            UUID.fromString(id);
            return true; // Parsing succeeded, so it's a valid UUID
        } catch (IllegalArgumentException e) {
            // Parsing failed, so it's not a valid UUID
            return false;
        }
    }

    private boolean IsMusicBrainzIdPresent(ArtistInfoObj entity) {
        return entity == null || entity.getmBID() != null && !entity.getmBID().isEmpty();
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

        if (!entity.getmBID().equals(newEntity.getmBID())) {
            logger.warn("MusicBrainz ID are different in the entity and newEntity object: {}, {}", entity.getmBID(), newEntity.getmBID());
        }
        // Merge albums from newEntity into entity
        List<AlbumInfoObj> existingAlbums = entity.getAlbums();
        List<AlbumInfoObj> newAlbums = newEntity.getAlbums();
        if (existingAlbums == null) {
            entity.setAlbums(newAlbums);
        } else if (newAlbums != null) {
            for (AlbumInfoObj newAlbum : newAlbums) {
                boolean albumExists = false;
                for (AlbumInfoObj existingAlbum : existingAlbums) {
                    // Check if album with the same albumId already exists
                    if (existingAlbum.getAlbumId().equals(newAlbum.getAlbumId())) {
                        albumExists = true;
                        break;
                    }
                }
                if (!albumExists) {
                    existingAlbums.add(newAlbum);
                }
            }
        }
    }

    /**
     * The use of CompletableFuture for asynchronous processing of Wikipedia and Cover Art Archive data
     * allows concurrent execution of tasks, which can improve performance.
     */
    private void getWikiAndCoverData(ArtistInfoObj entity) {
        // Executor service with a fixed pool size
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        CompletableFuture<Void> wikidataFuture;

        // CompletableFuture for Wikipedia service
        CompletableFuture<Void> wikipediaFuture = null;

        // CompletableFuture for Cover Art service
        CompletableFuture<Void> coverArtFuture = runCoverArtServiceAsync(entity, executorService);

        // Check if Wikipedia service can run
        if (shouldRunWikipediaService(entity)) {
            // Run Wikipedia service first
            wikipediaFuture = runWikipediaServiceAsync(entity, executorService);
        } else if (shouldRunWikidataService(entity)) {
            // Run Wikidata service first
            wikidataFuture = runWikidataServiceAsync(entity, executorService);
            // Continue with Wikipedia service after Wikidata service completes
            wikipediaFuture = wikidataFuture.thenRunAsync(() ->
                    runWikipediaServiceAsync(entity, executorService)
            );
        }

        // Wait for completion of Wikipedia service and cover art service if Wikipedia service is invoked
        if (wikipediaFuture != null) {
            CompletableFuture.allOf(wikipediaFuture, coverArtFuture).join();
        } else {
            // Wait for completion of only cover art service if Wikipedia service is not invoked
            coverArtFuture.join();
        }
    }

    private CompletableFuture<Void> runWikidataServiceAsync(ArtistInfoObj entity, ExecutorService executorService) {
        return CompletableFuture.runAsync(() -> {
            try {
                wikidataService.getWikidata(entity.getWikiInfo());
            } catch (URISyntaxException | JsonProcessingException e) {
                handleServiceErrorAsync(entity, e, executorService);
            }
        }, executorService);
    }

    private CompletableFuture<Void> runWikipediaServiceAsync(ArtistInfoObj entity, ExecutorService executorService) {
        return CompletableFuture.runAsync(() -> {
            try {
                wikiPediaService.getWikiPediadata(entity.getWikiInfo());
            } catch (URISyntaxException | JsonProcessingException e) {
                handleServiceErrorAsync(entity, e, executorService);
            }
        }, executorService);
    }

    private CompletableFuture<Void> runCoverArtServiceAsync(ArtistInfoObj entity, ExecutorService executorService) {
        return CompletableFuture.runAsync(() -> {
            coverArtArchiveService.getCoverData(entity.getAlbums());
        }, executorService);
    }

    private boolean shouldRunWikipediaService(ArtistInfoObj entity) {
        return entity.getWikiInfo() != null && !entity.getWikiInfo().getWikipediaSearchTerm().isEmpty();
    }

    private boolean shouldRunWikidataService(ArtistInfoObj entity) {
        return entity.getWikiInfo() != null && !entity.getWikiInfo().getWikidataSearchTerm().isEmpty();
    }

    private void handleServiceErrorAsync(ArtistInfoObj entity, Exception e, ExecutorService executorService) {
        CompletableFuture.runAsync(() -> handleServiceError(entity, e), executorService);
    }

    private void handleServiceError(ArtistInfoObj entity, Exception e) {
        logger.error("Error processing the API service for entity: {}", entity, e);
        throw new ServiceProcessingException("Failed creating URI or processing response as Json", e);

    }
}

