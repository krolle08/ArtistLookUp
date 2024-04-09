package Application.service;

import java.util.Map;

/**
 * Interface responsible for API calls
 */
public interface DataProcessor<T> {

    /**
     * Retrieves data and populate objects.
     * @param searchParam used to assign the type of search and its value from the request
     * @return an object which accordingly to the response returns populated or empty
     */
    MusicEntityObj getData(Map<String, String> searchParam) throws InvalidArtistException;
}

