package Application.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
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
import org.apache.http.client.config.RequestConfig;

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
    private final String queryTypeArtist = "artist/";
    private static final String pathPostFix = "?fmt=json&inc=url-rels+release-groups";

    @GetMapping("/MBArtist/{Id}")
    public Map<String, String> getDataFromArtist(@PathVariable String Id) throws URISyntaxException {
        String fullPath = constructUrl(Id, queryTypeArtist).toString();
        RestTemplate restTemplate = restTemplate();
        URI uri = new URI(fullPath);
        return getAndextractData(restTemplate, uri);
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

    @GetMapping("/MBrelease/{Id}")
    public Map<String, String> getRelease(@PathVariable String Id) throws URISyntaxException {
        String fullPath = constructUrl(Id, queryTypeArtist).toString();
        RestTemplate restTemplate = restTemplate();
        URI uri = new URI(fullPath);
        return getAndextractData(restTemplate, uri);
    }

    @GetMapping("/MBreleaseGroup/{Id}")
    public ResponseEntity<String> getReleaseGroup(@PathVariable String Id) throws URISyntaxException {
        String fullPath = constructUrl(Id, queryTypeArtist).toString();
        RestTemplate restTemplate = restTemplate();
        URI uri = new URI(fullPath);
        return restTemplate.getForEntity(uri, String.class);
    }

    private StringBuffer constructUrl(String iD, String queryType) throws URISyntaxException {
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
        return new RestTemplateBuilder().requestFactory(() ->
                        new HttpComponentsClientHttpRequestFactory(HttpClientBuilder
                                .create()
                                .setDefaultRequestConfig(config)
                                .build()))
                .build();
    }

    private Map<String, String> getAndextractData(RestTemplate restTemplate, URI uri) {
        Map<String, String> extractedData = new HashMap<>();
        String wikipedia = "wikipedia";
        String wikidata = "wikidata";
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(restTemplate.getForEntity(uri, String.class).getBody());
            // Iterate through the array of objects
            for (JsonNode node : rootNode) {
                // Check if the object has a direct wikipedia link
                if(wikipedia.equals(node.get("type").asText())){
                    extractedData.put(wikipedia, node.get("url").get("resource").asText());
                }else if (wikidata.equals(node.get("type").asText())) {
                    // Extract the entity field
                    extractedData.put("MBID", node.get("entity").asText());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return extractedData;
    }
}
