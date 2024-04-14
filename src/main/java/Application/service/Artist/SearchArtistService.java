package Application.service.Artist;

import Application.service.DataProcessor;
import Application.service.MusicBrainz.MusicBrainzIdService;
import Application.service.MusicBrainz.MusicBrainzNameService;
import Application.service.MusicEntityObj;
import Application.service.Executor.WikiAndCoverArtExecutorService;
import Application.utils.LoggingUtility;
import Application.utils.RestTempUtil;
import Application.utils.TypeOfSearchEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * The service handles the API calls and populates the MusicEntityObj that is used to process the final Json Response
 */
@Service
public class SearchArtistService implements DataProcessor<ArtistInfoObj> {
    private final MusicBrainzNameService musicBrainzNameService;
    private final MusicBrainzIdService musicBrainzIdService;
    private final WikiAndCoverArtExecutorService wikiAndCoverArtExecutorService;

    public SearchArtistService(MusicBrainzNameService musicBrainzNameService,
                               MusicBrainzIdService musicBrainzIdService,
                               WikiAndCoverArtExecutorService wikiAndCoverArtExecutorService) {
        this.musicBrainzNameService = musicBrainzNameService;
        this.musicBrainzIdService = musicBrainzIdService;
        this.wikiAndCoverArtExecutorService = wikiAndCoverArtExecutorService;
    }

    @Override
    public MusicEntityObj getData(Map<String, String> searchParam) {
        MusicEntityObj entity = new MusicEntityObj();
        ArtistInfoObj artistInfoObj;
            artistInfoObj = getMusicBrainzData(searchParam);
            if (artistInfoObj.isEmpty()) {
                LoggingUtility.info("No results found for: {} as an {}", searchParam.entrySet().iterator().next().getValue(),
                        searchParam.entrySet().iterator().next().getKey());
                return entity;
            }
            wikiAndCoverArtExecutorService.getWikiAndCoverData(artistInfoObj);
        entity.setArtistInfoObj(artistInfoObj);
        return entity;
    }

    private ArtistInfoObj getMusicBrainzData(Map<String, String> searchParam) {
        String searchValue = searchParam.entrySet().iterator().next().getValue();
        ArtistInfoObj entity;
        if (RestTempUtil.isValidUUID(searchValue)) {
            entity = musicBrainzIdService.getMBData(searchValue);
            return entity;
        } else {
            entity = musicBrainzNameService.getMBId(searchParam);
            if (IsMBIdPresent(entity)) {
                ArtistInfoObj newEntity;
                newEntity = musicBrainzIdService.getMBData(entity.getmBID());
                updateEntity(entity, newEntity, searchParam.get(TypeOfSearchEnum.ARTIST.toString()));
            }
        }
        return entity;
    }



    private boolean IsMBIdPresent(ArtistInfoObj entity) {
        return entity == null || entity.getmBID() != null && !entity.getmBID().isEmpty() && RestTempUtil.isValidUUID(entity.getmBID());
    }

    private void updateEntity(ArtistInfoObj entity, ArtistInfoObj newEntity, String searchParam) {
        boolean isMBIdPresent = isMBIdsSimilar(entity, newEntity);
        boolean isNamePresent = isNameSimilar(entity, newEntity, searchParam);
        boolean isWikiDataPresent = isWikiInfoPresent(entity, newEntity);
        addAlbumsIfNotPresent(entity, newEntity);

        if(!isMBIdPresent || !isNamePresent || !isWikiDataPresent){
            String errorMessage = "Error occurred when comparing the responses from MusicBrainz with one response based" +
                    "on the by name provided by the request: " + searchParam + " and when searching by MusicBrainz ID: "
                    + newEntity.getmBID();
            LoggingUtility.error(errorMessage);
        }
    }

    private boolean isMBIdsSimilar(ArtistInfoObj entity, ArtistInfoObj newEntity) {
        if (!entity.getmBID().equals(newEntity.getmBID())) {
            LoggingUtility.warn("MusicBrainz IDs are different in the entity and newEntity objects: {}, {}",
                    entity.getmBID(), newEntity.getmBID());
            return false;
        }
        return true;
    }

    private boolean isNameSimilar(ArtistInfoObj entity, ArtistInfoObj newEntity, String searchParam) {
        if (entity.getName() == null || entity.getName().isEmpty()) {
            entity.setName(newEntity.getName());
        } else if (!entity.getName().equalsIgnoreCase(newEntity.getName())) {
            LoggingUtility.warn("Different names were received when searching for: " + searchParam +
                    " and when searching with the following MBID: " + entity.getmBID());
        }
        return true;
    }

    private boolean isWikiInfoPresent(ArtistInfoObj entity, ArtistInfoObj newEntity) {
        if (entity.getWikiInfo() == null || entity.getWikiInfo().isEmpty()) {
            entity.setWikiInfo(newEntity.getWikiInfo());
            return true;
        } else if(!entity.getWikiInfo().getWikidataSearchTerm().equalsIgnoreCase(newEntity.getWikiInfo().getWikidataSearchTerm())){
            LoggingUtility.warn("Different Wikidata was received when searching for the name: " + entity.getName() +
                    " and when searching with the following MusicBrainz ID: " + entity.getmBID());
        }
        return false;
    }

    private void addAlbumsIfNotPresent(ArtistInfoObj entity, ArtistInfoObj newEntity) {
        // Merge albums from newEntity into entity
        List<AlbumInfoObj> existingAlbums = entity.getAlbums();
        List<AlbumInfoObj> newAlbums = newEntity.getAlbums();
        if (existingAlbums == null) {
            entity.setAlbums(newAlbums);
        } else if (newAlbums != null) {
            for (AlbumInfoObj newAlbum : newAlbums) {
                for (AlbumInfoObj existingAlbum : existingAlbums) {
                    // Check if album with the same albumId already exists
                    if (!existingAlbum.getAlbumId().equals(newAlbum.getAlbumId())) {
                        existingAlbums.add(newAlbum);
                    }
                }
            }
        }
    }
}

