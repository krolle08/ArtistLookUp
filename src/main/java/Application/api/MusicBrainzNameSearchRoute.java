package Application.api;

import Application.utils.LoggingUtility;
import Application.utils.RestTempUtil;
import Application.utils.RestTemplateConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * API service collecting the MusicBrainz ID according to the input providing by the user
 */
@RestController
public class MusicBrainzNameSearchRoute {

    @Value("${musicBrainzName.protocol}")
    private String protocol;

    @Value("${musicBrainzName.host}")
    private String host;

    @Value("${musicBrainzName.port}")
    private String port;

    @Value("${musicBrainzName.pathPrefix}")
    private String pathPrefix;

    @Value("${musicBrainzName.version}")
    private String version;
    @Value("${musicBrainzName.pathPostfix}")
    private String pathPostfix;

    @Value("${musicBrainzName.query}")
    private String query;

    @Value("${musicBrainzName.json}")
    private String json;
    @Value("${musicBrainzName.inc}")
    private String inc;


    private RestTemplateConfig config;

    @PostConstruct
    public void init() {
        config = new RestTemplateConfig(protocol, host, port, pathPostfix, version,
                query, pathPrefix, json, inc);
        // Initialize any properties or perform setup logic here
        LoggingUtility.info("Initialized MusicBrainzIDSearchRoute with properties: " +
                        "protocol={}, host={}, port={}, pathPrefix={}, version={}",
                protocol, host, port, pathPrefix, version);
    }



    public URI getUri(Map<String, String> filterParams) {
        URI uri;
        try {
            uri = new URI(RestTempUtil.getMBNameUriconstructor(filterParams, config).toString());
        } catch (URISyntaxException e) {
            String errorMessage = "Error constructing URI with the name: " + filterParams.entrySet().iterator().next().getValue()
                    + " " + e.getMessage();
            LoggingUtility.error(errorMessage);
            e.printStackTrace();
            throw new IllegalArgumentException(errorMessage);
        }
        return uri;
    }
    public ResponseEntity<String> doGetResponse(Map<String, String> filterParams){
        URI uri = getUri(filterParams);
        ResponseEntity<String> responseEntity = RestTempUtil.getResponse(uri);
        return responseEntity;
    }

    public RestTemplateConfig getRestConfig(){
        return config;
    }
}