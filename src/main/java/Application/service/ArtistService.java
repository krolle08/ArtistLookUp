package Application.service;

import Application.model.ArtistDetails;

public interface ArtistService {

    ArtistDetails getData(String searchParam) throws Exception;

}
