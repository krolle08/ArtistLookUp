package Application.service;

import Application.model.response.WikiDataResponse;

public interface WikidataService {

    public WikiDataResponse getWikipediaId(String wikiDataId) throws Exception;
}
