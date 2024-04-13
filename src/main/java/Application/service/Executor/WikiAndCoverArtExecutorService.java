package Application.service.Executor;

import Application.service.Artist.ArtistInfoObj;
import Application.service.CoverArtArchive.CoverArtArchiveService;
import Application.service.Wiki.WikiDataService;
import Application.service.Wiki.WikiPediaService;
import Application.utils.LoggingUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class WikiAndCoverArtExecutorService {
    @Autowired
    WikiPediaService wikiPediaService;
    @Autowired
    WikiDataService wikiDataService;
    @Autowired
    CoverArtArchiveService coverArtArchiveService;

    /**
     * The use of CompletableFuture for asynchronous processing of Wiki and Cover Art Archive data
     * allows concurrent execution of tasks, which can improve performance.
     */
    public void getWikiAndCoverData(ArtistInfoObj entity) {
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
            wikiDataService.getWikidata(entity.getWikiInfo());
            if(entity.getWikiInfo().getWikipediaSearchTerm() == null ||entity.getWikiInfo().getWikipediaSearchTerm().isEmpty()){
                handleServiceErrorAsync(entity, executorService);
            }
        }, executorService);
    }
    private CompletableFuture<Void> runWikipediaServiceAsync(ArtistInfoObj entity, ExecutorService executorService) {
        return CompletableFuture.runAsync(() -> {
            wikiPediaService.getWikiPediadata(entity.getWikiInfo());
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

    private void handleServiceErrorAsync(ArtistInfoObj entity, ExecutorService executorService) {
        CompletableFuture.runAsync(() -> handleServiceError(entity), executorService);
    }

    private void handleServiceError(ArtistInfoObj entity) {
        LoggingUtility.error("No response given on Music Brainz ID: {}", entity.getmBID());
        throw new RuntimeException("No response on Music Brainz ID: " + entity.getmBID());

    }
}
