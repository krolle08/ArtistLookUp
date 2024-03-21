package Application.api;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MusicBrainzIDSearchRoute {

    /*  @Value("${musicBrainz.protocol}")
      private String PROTOCOL;
      @Value("${musicBrainz.host}")
      private String HOST;
      @Value("${musicBrainz.host}")
      private int PORT;
      @Value("${musicBrainz.servicePath}")

     */
    private Log log = LogFactory.getLog(MusicBrainzIDSearchRoute.class);
    private final String protocol = "http";
    private final String schemeDelimiter = "://";
    private final String host = "musicbrainz.org";
    private final Integer port = 80;
    private final String pathPrefix = "/ws";
    private final String version = "/2";
    private final String queryTypeArtist = "/artist/";
    private static final String pathPostFix = "?fmt=json&inc=url-rels+release-groups";
    private Map<String, Object> result = new HashMap<>();

    //@GetMapping("/MBArtist/{Id}")
    public Map<String, Object> getDataFromArtist(String Id) throws URISyntaxException {
        String fullPath = constructUrl(Id, queryTypeArtist).toString();
        RestTemplate restTemplate = restTemplate();
        URI uri = new URI(fullPath);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        result.put("statuscode", String.valueOf(response.getStatusCodeValue()));
        if (response.getBody().isEmpty()) {
            log.info("No body was given with the provided search parameters");
            return result;
        }
        result.putAll(extractData(response.getBody()));
        return result;
    }

    @GetMapping("/MBgenre/{Id}")
    public ResponseEntity<String> getGenre(@PathVariable String Id) throws URISyntaxException {
        String fullPath = constructUrl(Id, queryTypeArtist).toString();
        RestTemplate restTemplate = restTemplate();
        URI uri = new URI(fullPath);
        return restTemplate.getForEntity(uri, String.class);
    }

    @GetMapping("/MBcover/{Id}")
    public ResponseEntity<String> getCover(@PathVariable String Id) throws URISyntaxException {
        String fullPath = constructUrl(Id, queryTypeArtist).toString();
        RestTemplate restTemplate = restTemplate();
        URI uri = new URI(fullPath);
        return restTemplate.getForEntity(uri, String.class);
    }


    public Map<String, String> getRelease(@PathVariable String Id) throws URISyntaxException {
        String fullPath = constructUrl(Id, queryTypeArtist).toString();
        RestTemplate restTemplate = restTemplate();
        URI uri = new URI(fullPath);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        String responseBody = response.getBody();
        return extractData(responseBody);
    }

    @GetMapping("/MBreleaseGroup/{Id}")
    public ResponseEntity<String> getReleaseGroup(@PathVariable String Id) throws URISyntaxException {
        String fullPath = constructUrl(Id, queryTypeArtist).toString();
        RestTemplate restTemplate = restTemplate();
        URI uri = new URI(fullPath);
        return restTemplate.getForEntity(uri, String.class);
    }

    private StringBuffer constructUrl(String iD, String queryType) {
        StringBuffer url = new StringBuffer();
        url.append(protocol).append(schemeDelimiter).append(host);
        if (port != null) {
            url.append(":").append(port);
        }
        if (iD.isEmpty()) {
            log.info("No ID was given for the search:" + iD);

        }

        url.append(pathPrefix).append(version).append(queryType).append(iD);
        url.append(pathPostFix);
        return url;
    }

    public RestTemplate restTemplate() {
        HttpHost proxy = new HttpHost(host, port);
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        return new RestTemplateBuilder().requestFactory(() -> new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().setDefaultRequestConfig(config).build())).build();
    }

    private Map<String, String> extractData(String response) {
        Map<String, String> extractedData = new HashMap<>();
        try {
            // Extract response body
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            extractedData.put("name", rootNode.get("name").asText());
            extractedData.putAll(extractwikiData(rootNode));

            // Add coverData map directly to extractedData
            Map<String, String> coverData = extractCoverIdAndTitle(rootNode);
            extractedData.put("Covers",coverData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extractedData;
    }

    private Map<String, String> extractwikiData(JsonNode rootNode) {
        Map<String, String> result = new HashMap<>();
        JsonNode relations = rootNode.get("relations");
        final String wikipedia = "wikipedia";
        final String wikidata = "wikidata";
        // Iterate through the array of objects
        for (JsonNode node : relations) {
            if (!node.isNull() || !node.isEmpty()) {
                // Check if the object has a direct wikipedia link
                if ((node.get("type").asText()).toString().contains(wikipedia)) {
                    result.put(wikipedia, node.get("url").get("resource").asText());
                    break;
                } else if (node.get("type").asText().toString().contains(wikidata)) {
                    String wikiData = node.get("url").get("resource").asText().toString();
                    String wikiDataTerm = wikiData.replaceAll("^.*?/(Q\\d+)$", "$1");
                    result.put("wikidataSearchTerm", wikiDataTerm);
                }
            }
        }
        return result;
    }

    private Map<String, String> extractCoverIdAndTitle(JsonNode rootNode) {
        Map<String, String> result = new HashMap<>();
        JsonNode relations = rootNode.get("release-groups");
        for (JsonNode node : relations) {
            if (!node.isNull() || !node.isEmpty()) {
                result.put(node.get("title").asText(), node.get("id").asText());
            }
        }
        return result;
    }

}
