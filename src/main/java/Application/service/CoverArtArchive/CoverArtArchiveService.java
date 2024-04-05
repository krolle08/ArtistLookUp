package Application.service.CoverArtArchive;

import Application.api.CoverArtArchiveSearchRoute;
import Application.service.Artist.AlbumInfoObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

/**
 * API service to collect the imageURL for each album in the AlbumInfoObj list
 */
@RestController
public class CoverArtArchiveService {
    private Logger logger = Logger.getLogger(CoverArtArchiveService.class.getName());
    @Autowired
    private CoverArtArchiveSearchRoute coverArtArchiveSearchRoute;

    public void getCoverData(List<AlbumInfoObj> albums) {
        coverArtArchiveSearchRoute.doGetCoverData(albums);
    }
}


