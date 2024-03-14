package Application.api;

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
public class MusicBrainzNameSearchRoute {

    /*  @Value("${musicBrainz.protocol}")
      private String PROTOCOL;
      @Value("${musicBrainz.host}")
      private String HOST;
      @Value("${musicBrainz.host}")
      private int PORT;
      @Value("${musicBrainz.servicePath}")

     */
    private Log log  = LogFactory.getLog(MusicBrainzNameSearchRoute.class);
    private String protocol = "http";
    private String schemeDelimiter ="://";
    private String version = "/2";
    private String pathPrefix = "/ws";
    private String annotation = "/annotation";
    private String query = "/?query=";

    private String host = "musicbrainz.org";
    private Integer port = 80;

    @GetMapping("/MBArtist/{Id}")
    public ResponseEntity<String> getMBID(@PathVariable Map<String,String> filterParams) throws URISyntaxException {
        String url = constructUrl(filterParams).toString();
        RestTemplate restTemplate = restTemplate();
        URI uri = new URI(url);
        return restTemplate.getForEntity(uri, String.class);
    }

    private StringBuffer constructUrl(Map<String, String> filterParams) throws URISyntaxException {
        StringBuffer url = new StringBuffer();
        url.append(protocol).append(schemeDelimiter).append(host);
        if(port != null){
            url.append(":").append(port);
        }

        url.append(pathPrefix).append(version).append(annotation).append(query);
        if(filterParams == null){
            log.info("No parameter for the search has been provided");
        } else{
            // Append each parameter from filterParams
            for(Map.Entry<String, String> entry : filterParams.entrySet()){
                String paramName = entry.getKey();
                String paramValue = entry.getValue();
                // Append parameter name and value to URL
                url.append(paramName).append(":").append(paramValue);
            }
        }
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
}