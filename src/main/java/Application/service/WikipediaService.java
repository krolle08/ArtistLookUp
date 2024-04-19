package Application.service;

import Application.model.response.WikipediaResponse;

public interface WikipediaService {

    public WikipediaResponse getDescription(String wikipediaId) throws Exception;

}
