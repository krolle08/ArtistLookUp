package Application.service.impl;

import Application.api.WikipediaClientImpl;
import Application.model.response.WikipediaResponse;
import Application.service.WikipediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class WikipediaServiceImpl implements WikipediaService {
    @Autowired
    WikipediaClientImpl wikipediaClient;
    @Override
    public WikipediaResponse getDescription(String wikipediaId) throws Exception {
        String url = wikipediaClient.buildUrl(wikipediaId);
        WikipediaResponse wikipediaResponse = wikipediaClient.getForObject(url).getBody();
        return wikipediaResponse;
    }
}
