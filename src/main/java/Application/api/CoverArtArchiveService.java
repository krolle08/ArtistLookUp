package Application.api;

import Application.service.AlbumInfo;
import fm.last.musicbrainz.coverart.CoverArt;
import fm.last.musicbrainz.coverart.CoverArtArchiveClient;
import fm.last.musicbrainz.coverart.CoverArtImage;
import fm.last.musicbrainz.coverart.impl.DefaultCoverArtArchiveClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class CoverArtArchiveService {
    private Log log = LogFactory.getLog(MusicBrainzIDSearchRoute.class);
    CoverArtArchiveClient client = new DefaultCoverArtArchiveClient();

    private final int THREAD_POOL_SIZE = 10; // You can adjust this according to your requirements

    //                HVORFOR FÅS DER INGEN RESULTATER HVILKET ID SKAL BRUGES!!!
    public void getss(List<AlbumInfo> albums) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        albums.forEach(album -> {
            try {
                UUID albumId = UUID.fromString(album.getAlbumId());
                CoverArt coverArt = null;
                //        coverArt = client.getByMbid(albumId);
                coverArt = client.getReleaseGroupByMbid(albumId);
                if (coverArt != null) {
                    coverArt.getImages().stream().findFirst().ifPresent(coverArtImage -> {
                                album.setImageURL(coverArtImage.getImageUrl());
                                album.setAlbumId(String.valueOf(coverArtImage.getId()));
                            }
                    );
                } else {
                    log.warn("No images found for album title: " + album.getTitle() + " with the cover ID: " + album.getiD());
                    album.setImageURL("No images found for album");
                }
            } catch (IllegalArgumentException e) {
                log.error("Invalid UUID format for album ID: " + album.getiD(), e);

            } catch (Exception e) {
                log.error("Error processing album: " + album.getTitle(), e);
                e.printStackTrace();
            }
        });
    }


    public void getCovers(List<AlbumInfo> albums) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        for (AlbumInfo album : albums) {
            executor.execute(() -> {
                try {
                    UUID albumId = UUID.fromString(album.getiD());
                    //  CoverArt coverArt = client.getByMbid(albumId);
                    CoverArt coverArt = client.getReleaseGroupByMbid(albumId);
                    if (coverArt != null) {
                        coverArt.getImages().stream().findFirst().ifPresent(coverArtImage -> {
                            album.setImageURL(coverArtImage.getImageUrl());
                            album.setAlbumId(String.valueOf(coverArtImage.getId()));
                        });
                    } else {
                        log.warn("No images found for album title: " + album.getTitle() + " with the cover ID: " + album.getiD());
                    }
                } catch (IllegalArgumentException e) {
                    log.error("Invalid UUID format for album ID: " + album.getiD(), e);
                } catch (Exception e) {
                    log.error("Error processing album: " + album.getTitle(), e);
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        // Wait for all threads to finish
        while (!executor.isTerminated()) {
            try {
                Thread.sleep(100); // wait 100 milliseconds before checking again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


