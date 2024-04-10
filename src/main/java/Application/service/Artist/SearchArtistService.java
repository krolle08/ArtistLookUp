package Application.service.Artist;

import Application.service.CoverArtArchive.CoverArtArchiveService;
import Application.service.DataProcessor;
import Application.service.InvalidArtistException;
import Application.service.MusicBrainz.MusicBrainzIdService;
import Application.service.MusicBrainz.MusicBrainzNameService;
import Application.service.MusicEntityObj;
import Application.service.Wikidata.WikidataService;
import Application.service.Wikipedia.WikiPediaService;
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
    private final MusicBrainzNameService musicBrainzNameService;
    private final MusicBrainzIdService musicBrainzIdService;
    private final CoverArtArchiveService coverArtArchiveService;
    private final WikiPediaService wikiPediaService;
    private final WikidataService wikidataService;

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
    public MusicEntityObj getData(Map<String, String> searchParam) throws InvalidArtistException {
        MusicEntityObj entity = new MusicEntityObj();
        ArtistInfoObj artistInfoObj;
        try {
            entity = new MusicEntityObj();
            artistInfoObj = getMusicBrainzData(searchParam);
            if (artistInfoObj.isEmpty()) {
                logger.info("No results found for: {} as an {}", searchParam.entrySet().iterator().next().getValue(),
                        searchParam.entrySet().iterator().next().getKey());
                throw new InvalidArtistException("No results found for: " + searchParam.entrySet().iterator().next().getValue()
                        + " as an " + searchParam.entrySet().iterator().next().getKey());
            }
            getWikiAndCoverData(artistInfoObj);
        } catch (RuntimeException e) {
            logger.error("Failed creating URI or processing response", e);
            return entity;
        }
        entity.setArtistInfoObj(artistInfoObj);
        return entity;
    }

    private ArtistInfoObj getMusicBrainzData(Map<String, String> searchParam) throws InvalidArtistException {
        String searchValue = searchParam.entrySet().iterator().next().getValue();
        ArtistInfoObj entity;
        if (isValidUUID(searchValue)) {
            entity = musicBrainzIdService.getMBData(searchValue);
            return entity;
        } else {
            entity = musicBrainzNameService.getMBIdData(searchParam);
            ArtistInfoObj newEntity;
            if (IsMBIdPresent(entity)) {
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

    private boolean IsMBIdPresent(ArtistInfoObj entity) {
        return entity == null || entity.getmBID() != null && !entity.getmBID().isEmpty() && isValidUUID(entity.getmBID());
    }

    private void updateEntity(ArtistInfoObj entity, ArtistInfoObj newEntity, String searchParam) {
        checkAndAddMBIdPresence(entity, newEntity);
        checkAndAddNamePresence(entity, newEntity, searchParam);
        checkAndAddWikiInfo(entity, newEntity);
        addAlbumsIfNotPresent(entity, newEntity);
    }

    private void checkAndAddMBIdPresence(ArtistInfoObj entity, ArtistInfoObj newEntity) {
        if (!entity.getmBID().equals(newEntity.getmBID())) {
            logger.warn("MusicBrainz ID are different in the entity and newEntity object: {}, {}", entity.getmBID(), newEntity.getmBID());
            throw new RuntimeException("MusicBrainz ID are different in the entity and newEntity object");
        }
    }

    private void checkAndAddNamePresence(ArtistInfoObj entity, ArtistInfoObj newEntity, String searchParam) {
        // Update name if not already set
        if (entity.getName() == null || entity.getName().isEmpty()) {
            entity.setName(newEntity.getName());
        } else if (!entity.getName().equalsIgnoreCase(newEntity.getName())) {
            throw new RuntimeException("Different names has been received doing the search of: " + searchParam +
                    " and when searching with the following mbid: " + entity.getmBID());
        }
    }

    private void checkAndAddWikiInfo(ArtistInfoObj entity, ArtistInfoObj newEntity) {
        if (entity.getWikiInfo() == null || entity.getWikiInfo().isEmpty()) {
            entity.setWikiInfo(newEntity.getWikiInfo());
        }
    }

    private void addAlbumsIfNotPresent(ArtistInfoObj entity, ArtistInfoObj newEntity) {
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
        throw new RuntimeException("Failed creating URI or processing response as Json", e);

    }
}

