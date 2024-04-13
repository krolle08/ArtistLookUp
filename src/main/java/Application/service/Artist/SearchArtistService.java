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
            if (artistInfoObj.getmBID() == null || artistInfoObj.getmBID().isEmpty()) {
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
        checkAndAddMBIdPresence(entity, newEntity);
        checkAndAddNamePresence(entity, newEntity, searchParam);
        checkAndAddWikiInfo(entity, newEntity);
        addAlbumsIfNotPresent(entity, newEntity);
    }

    private void checkAndAddMBIdPresence(ArtistInfoObj entity, ArtistInfoObj newEntity) {
        if (!entity.getmBID().equals(newEntity.getmBID())) {
            LoggingUtility.warn("MusicBrainz ID are different in the entity and newEntity object: {}, {}", entity.getmBID(), newEntity.getmBID());
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
}

