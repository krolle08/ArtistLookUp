package Application.service.Area;

import Application.service.Artist.ArtistInfoObj;
import Application.service.CoverArtArchive.CoverArtArchiveService;
import Application.service.DataProcessor;
import Application.service.MusicBrainz.MusicBrainzIdService;
import Application.service.MusicBrainz.MusicBrainzNameService;
import Application.service.Wikidata.WikidataService;
import Application.service.Wikipedia.WikiPediaService;
import Application.utils.TypeOfSearchEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Under development
 */
@Service
public class SearchAreaService implements DataProcessor<AreaInfoObj> {
    private static final Logger logger = LoggerFactory.getLogger(SearchAreaService.class.getName());

    @Override
    public AreaInfoObj getData(Map<String, String> searchParam) {
        return null;
    }
}

