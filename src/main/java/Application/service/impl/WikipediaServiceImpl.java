package Application.service.impl;

import Application.api.WikipediaClientImpl;
import Application.model.response.WikipediaResponse;
import Application.service.WikipediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;

@Service

public class WikipediaServiceImpl implements WikipediaService {
    @Autowired
    WikipediaClientImpl wikipediaClient;
    @Override
    public WikipediaResponse getDescription(String wikipediaId) throws Exception {
        URI url = new URI(wikipediaClient.buildUrl(URLEncoder.encode(wikipediaId)));
        WikipediaResponse wikipediaResponse = wikipediaClient.getForObject(url).getBody();
        return wikipediaResponse;
    }
}
