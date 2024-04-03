package Application.service;

import Application.api.*;
import Application.service.ArtistContainer.ArtistInfoObj;
import Application.utils.URIException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The service handles the API calls and construction of the MusicEntityObj class that is used to process the final Json Response
 * being displayed at the end
 */
@Service
public class SearchArtistService {
    private static final Logger logger = LoggerFactory.getLogger(SearchArtistService.class.getName());
    @Autowired
    private MusicBrainzNameSearchRoute musicBrainzNameSearchRoute;

    @Autowired
    private MusicBrainzIDSearchRoute musicBrainzIDSearchRoute;

    @Autowired
    private CoverArtArchiveService coverArtArchiveService;

    @Autowired
    private WikidataSearchRoute wikidataSearchRoute;

    @Autowired
    private WikipediaSearchRoute wikipediaSearchRoute;

    public ArtistInfoObj searchArtist(Map<String, String> searchParam) throws URISyntaxException, JsonProcessingException, URIException {
        try {
            ArtistInfoObj entity = musicBrainzNameSearchRoute.getArtistMBID(searchParam);
            ArtistInfoObj newEntity;
            if (entity == null || entity.getmBID() == null || entity.getmBID().isEmpty()) {
                // Get the new ArtistInfoObj from the first search method
                newEntity = musicBrainzIDSearchRoute.getArtistDataWithmbid(searchParam
                        .get(TypeOfSearchEnum.ARTIST.getSearchType()));
            } else {
                newEntity = (musicBrainzIDSearchRoute.getArtistDataWithmbid(entity.getmBID()));
            }
            updateArtistInfoNameField(entity, newEntity, searchParam.get(TypeOfSearchEnum.ARTIST.toString()));

            // Executor service with fixed pool size
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            // Fetch wikidata and wikipedia info concurrently
            CompletableFuture<Void> wikidataAndWikipediaFuture = CompletableFuture.runAsync(() -> {
                try {
                    if (entity.getWikiInfo().getWikidata() != null) {
                        wikidataSearchRoute.getWikidataForArtist(entity.getWikiInfo());
                    }
                    wikipediaSearchRoute.wikipediaService(entity.getWikiInfo());
                } catch (URISyntaxException | JsonProcessingException e) {
                    e.printStackTrace();
                }
            }, executorService);

            CompletableFuture<Void> coverArtFuture = CompletableFuture.runAsync(() -> {
                coverArtArchiveService.getCovers(entity.getAlbums());
            }, executorService);

            // Wait for all tasks to complete
            CompletableFuture<Void> allOf = CompletableFuture.allOf(wikidataAndWikipediaFuture, coverArtFuture);
            allOf.join();


            /*if (entity.getWikiInfo().getWikidata() != null) {
                wikidataSearchRoute.getWikidataForArtist(entity.getWikiInfo());
            }
            wikipediaSearchRoute.wikipediaService(entity.getWikiInfo());
           coverArtArchiveService.getCovers(entity.getAlbums());
            */

            return entity;
        } catch (JsonProcessingException | URIException e) {
            logger.error("An error occurred while searching for artist: " + searchParam
                    .get(TypeOfSearchEnum.ARTIST.getSearchType()) + " " + e.getMessage());
            return new ArtistInfoObj();
        }
    }

    private void updateArtistInfoNameField(ArtistInfoObj existingInfo, ArtistInfoObj newInfo, String searchParam) {
        // Update additional fields if necessary
        if (existingInfo.getName() != null || !existingInfo.getName().isEmpty()) {
            if (!existingInfo.getName().equals(newInfo.getName())) {
                throw new RuntimeException("Different names has been received doing the search of: " + searchParam +
                        " and when searching with the following mbid: " + existingInfo.getmBID());
            }
        } else {
            existingInfo.setName(newInfo.getName());
        }
        if (existingInfo.getWikiInfo() == null || existingInfo.getWikiInfo().isEmpty()) {
            existingInfo.setWikiInfo(newInfo.getWikiInfo());
        }

        if (existingInfo.getAlbums() == null || existingInfo.getAlbums().isEmpty()) {
            existingInfo.setAlbums(newInfo.getAlbums());
        }
    }
}

