package Application.api;

import Application.utils.RestTempUtil;
import Application.utils.RestTemplateConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(MusicBrainzNameSearchRoute.class.getName());

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


    private RestTemplateConfig config;

    @PostConstruct
    public void init() {
        config = new RestTemplateConfig(protocol, host, port, pathPostfix, version,
                null, pathPrefix, json);
        // Initialize any properties or perform setup logic here
        logger.info("Initialized MusicBrainzIDSearchRoute with properties: " +
                        "protocol={}, host={}, port={}, pathPrefix={}, version={}",
                protocol, host, port, pathPrefix, version);
    }



    public URI getUri(Map<String, String> filterParams) throws IllegalArgumentException {
        URI uri;
        try {
            uri = new URI(RestTempUtil.constructUri(filterParams, query, config).toString());
        } catch (URISyntaxException e) {
            logger.error("Error constructing URI with param: " + filterParams.entrySet().iterator().next().getValue() +
                    " " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
        return uri;
    }
    public ResponseEntity<String> doGetResponse(Map<String, String> filterParams){
        URI uri = getUri(filterParams);
        ResponseEntity<String> responseEntity = RestTempUtil.getResponse(uri);
        return responseEntity;
    }
}