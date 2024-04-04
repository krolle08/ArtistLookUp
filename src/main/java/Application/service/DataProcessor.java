package Application.service;

import java.util.Map;

/**
 * Interface responsible for API calls
 */
public interface DataProcessor<T> {

    /**
     * Retrieves data and populate objects.
     */
    T getData(Map<String, String> searchParam);
}

