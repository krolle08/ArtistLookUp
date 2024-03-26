package Application.service;

import Application.api.WikipediaSearchRoute;
import Application.api.RestTemp;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;

/**
 * I include the Scanner in the constructor when the class relies on user input and it is unlikely that
 * the scanner needs to be swapped out for another Scanner instance during the class's lifetime. It simplifies method
 * signatures and encapsulates the dependency, resulting in cleaner code.
 */
public class GetWikiPediaDataService {
    WikipediaSearchRoute wikipediaSearchRoute = new WikipediaSearchRoute();
    public void getWikipediaDescriptionForArtist(WikiInfo wikiInfo) throws URISyntaxException {
        if(wikiInfo.getWikipedia().isEmpty()){

        }
        wikipediaSearchRoute.wikipediaService(wikiInfo);


    }

    private String constructUrl(String searchTerm){
        return RestTemp.constructUrl(searchTerm,  protocol,  schemeDelimiter,  host,
                pathPrefix,  api,  postPreFix);
    }

    private ResponseEntity getResponse(String uri) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        if (response.getBody().isEmpty()) {
            log.warn("No results on provided uri: " + uri);
        }
        return response;
    }

    private String extractDescription(ResponseEntity response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));
            JsonNode extractNode = rootNode.path("query").path("pages").elements().next().get("extract");
            if (extractNode != null && !extractNode.isNull()) {
                return rootNode.path("query").path("pages").elements().next().get("extract").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

