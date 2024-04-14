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

import java.util.Iterator;
import java.util.Map;

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
            String errorMessage = "Error occurred when mapping the response from Wikipedia";
            LoggingUtility.error(errorMessage + " in WikiPediaService.getDescription.37. " +
                    e.getMessage());
            e.printStackTrace();
            wikiInfoObj.setDescription(errorMessage);
            return;
        }
        JsonNode pagesNode = rootNode.path("query").path("pages");
        if (pagesNode != null && pagesNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> pagesIterator = pagesNode.fields();
            while (pagesIterator.hasNext()) {
                Map.Entry<String, JsonNode> pageEntry = pagesIterator.next();
                String keyValue = pageEntry.getKey();
                JsonNode pageValue = pageEntry.getValue();

                JsonNode extractNode = pageValue.get("extract");
                if (extractNode != null && !extractNode.isNull()) {
                    String extract = extractNode.asText();
                    wikiInfoObj.setDescription(extract);
                    return;
                } else {
                    LoggingUtility.warn("No 'extract' node found for key: " + keyValue);
                }
            }
        } else {
            LoggingUtility.warn("No 'pages' node found in the JSON response");
        }
        wikiInfoObj.setDescription("No description found for: " + wikiInfoObj.getWikipediaSearchTerm());
    }
}
