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
import java.util.Optional;
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
    private String annotation = "/annotation";
    private String query = "/?query=";
    private String queryType = "Artist:";
    private String json = "fmt=json";
    public Map<String, String> getMBID(Map<String, String> filterParams) {
        Map<String, String> extractedData = new HashMap<>();
        URI uri = null;
        try {
            uri = new URI(constructUrl(filterParams).toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        RestTemplate restTemplate = restTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        if (responseEntity.getBody().isEmpty()) {
            log.info("No response was given ");
            return extractedData;
        }
        try {
            return extractMBID(responseEntity, filterParams, extractedData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private StringBuffer constructUrl(Map<String, String> filterParams) {
        StringBuffer url = new StringBuffer();
        url.append(protocol).append(schemeDelimiter).append(host);
        if (port != null) {
            url.append(":").append(port);
        }

        url.append(pathPrefix).append(version).append(annotation).append(query);
        if (filterParams == null) {
            log.info("No parameter for the search has been provided");
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

    private Map<String, String> extractMBID(ResponseEntity responseEntity, Map<String, String> filterParams, Map<String, String> extractedData) throws JsonProcessingException {
        extractedData.put("MBstatuscode", String.valueOf(responseEntity.getStatusCodeValue()));
        extractedData.putAll(filterParams);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String artist = "artist";
            JsonNode rootNode = mapper.readTree(responseEntity.getBody().toString());
            Iterator<JsonNode> annotationIterator = rootNode.path("annotations").elements();
            // Flag variable to indicate whether the desired ID has been found
            boolean idFound = false;

            // Iterate through the annotations
            while (annotationIterator.hasNext() && !idFound) {
                JsonNode annotation = annotationIterator.next();
                String type = annotation.path("type").asText();
                if (artist.equals(type) && annotation.path("text").asText().contains(filterParams.get(TypeOfSearchEnum.ARTIST.getSearchType()))) {
                    String text = annotation.path("text").asText();
                    String id = extractMBIdFromText(text, filterParams.get(TypeOfSearchEnum.ARTIST.getSearchType()));
                    if (id != null) {
                        extractedData.put("MBID", id);
                        idFound = true; // Set the flag to true once the desired ID is found
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extractedData;
    }
    private String extractMBIdFromText(String text, String name) {
        // Regular expression to extract the ID between [artist: and | characters
        String regex = "\\[artist:([a-f0-9\\-]+)\\|" + name + "\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}