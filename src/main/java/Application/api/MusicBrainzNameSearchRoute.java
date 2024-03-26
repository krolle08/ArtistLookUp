package Application.api;

import Application.service.TypeOfSearchEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class MusicBrainzNameSearchRoute {

    /*  @Value("${musicBrainz.protocol}")
      private String PROTOCOL;
      @Value("${musicBrainz.host}")
      private String HOST;
      @Value("${musicBrainz.host}")
      private int PORT;
      @Value("${musicBrainz.servicePath}")

     */
    private Log log = LogFactory.getLog(MusicBrainzNameSearchRoute.class);
    private String protocol = "http";
    private String schemeDelimiter = "://";
    private String host = "musicbrainz.org";
    private Integer port = 80;
    private String pathPrefix = "/ws";
    private String version = "/2";
    private String annotation = "/artist";
    private String query = "/?query=";
    private String json = "fmt=json";

    public Map<String, Object> getMBID(Map<String, String> filterParams) {
        Map<String, Object> result = new HashMap<>();
        URI uri = null;
        try {
            uri = new URI(constructUrl(filterParams).toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        RestTemplate restTemplate = restTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        if (isBodyEmpty(responseEntity)) {
            log.info("No response was given");
            return result;
        }
        try {
            return extractMBData(responseEntity, filterParams, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isBodyEmpty(ResponseEntity responseEntity) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(responseEntity.getBody().toString());
            if (rootNode.path("artists").isEmpty()) {
                return true;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }

    private StringBuffer constructUrl(Map<String, String> filterParams) {
        StringBuffer url = new StringBuffer();
        url.append(protocol).append(schemeDelimiter).append(host);
        if (port != null) {
            url.append(":").append(port);
        }
        url.append(pathPrefix).append(version).append(annotation).append(query);
        if (filterParams.isEmpty()) {
            log.info("No parameter for the search has been provided");
            return new StringBuffer();
        } else {
            // Append each parameter from filterParams
            for (Map.Entry<String, String> entry : filterParams.entrySet()) {
                String paramName = entry.getKey().toLowerCase();
                String paramValue = entry.getValue();
                // Append parameter name and value to URL
                url.append(paramName).append(":").append(paramValue);
            }
        }
        url.append("&").append(json);
        return url;
    }

    public RestTemplate restTemplate() {
        HttpHost proxy = new HttpHost(host, port);
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        return new RestTemplateBuilder().requestFactory(() -> new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().setDefaultRequestConfig(config).build())).build();
    }

    private Map<String, Object> extractMBData(ResponseEntity responseEntity, Map<String, String> filterParams, Map<String, Object> extractedData) throws JsonProcessingException {
        extractedData.put("MBstatuscode", String.valueOf(responseEntity.getStatusCodeValue()));
        extractedData.putAll(filterParams);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseEntity.getBody().toString());
        if (rootNode.path("artists").isEmpty()) {
            return extractedData;
        }
        try {
            String artist = filterParams.get(TypeOfSearchEnum.ARTIST.getSearchType());
            Iterator<JsonNode> annotationIterator = rootNode.path("artists").elements();
            // Flag variable to indicate whether the desired ID has been found
            int highestScore = Integer.MIN_VALUE;
            String highestScoreId = null;

            // Iterate through the annotations
            while (annotationIterator.hasNext()) {
                JsonNode annotation = annotationIterator.next();
                int score = annotation.path("score").asInt(); // Get the score
                String type = annotation.path("name").asText();

                if (artist.equalsIgnoreCase(type) && score > highestScore) {
                    highestScore = score;
                    highestScoreId = annotation.path("id").asText();
                }
            }
            extractedData.put("MBID", highestScoreId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extractedData;
    }

    private String extractMBIdFromText(String text, String searchTerm) {
        // Regular expression to extract the ID between [artist: and | characters
        String regex = "([a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12})" + Pattern.quote(searchTerm);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}