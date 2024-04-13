package Application.service.Wiki;

import Application.api.WikipediaSearchRoute;
import Application.service.Artist.WikiInfoObj;
import Application.utils.LoggingUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WikiPediaService {

    @Autowired
    WikipediaSearchRoute wikipediaSearchRoute;

    public void getWikiPediadata(WikiInfoObj wikiInfoObj) {
        ResponseEntity<String> response = wikipediaSearchRoute.doGetResponse(wikiInfoObj.getWikipediaSearchTerm());
        getDescription(response, wikiInfoObj);
        wikiInfoObj.setWikiPediaStatuscode(response.getStatusCodeValue());
    }

    public void getDescription(ResponseEntity response, WikiInfoObj wikiInfoObj) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(response.getBody().toString());
        } catch (JsonProcessingException e) {
            LoggingUtility.error("Error occurred when mapping the response in WikiPediaService.getDescription.37. " +
                    e.getMessage());
            e.printStackTrace();
            wikiInfoObj.setDescription("Error occurred when mapping the response from Wikipedia");
            return;
        }
        JsonNode extractNode = rootNode.path("query").path("pages").elements().next().get("extract");
        if (extractNode != null && !extractNode.isNull()) {
            wikiInfoObj.setDescription(extractNode.asText());
            return;
        }
        wikiInfoObj.setDescription("No description found");
    }
}
