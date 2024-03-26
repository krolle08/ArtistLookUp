package Application.features;

import Application.api.MusicBrainzNameSearchRoute;
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
import org.springframework.web.client.RestTemplate;

import java.util.Map;


public class RestTemp {

    private static final Log log = LogFactory.getLog(RestTemp.class);
    public static RestTemplate restTemplate(String host, int port) {
        HttpHost proxy = new HttpHost(host, port);
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        return new RestTemplateBuilder().requestFactory(() -> new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().setDefaultRequestConfig(config).build())).build();
    }

    public static StringBuffer constructUrl(String iD, String queryType, String protocol, String schemeDelimiter, String host, int port, String pathPrefix, String version , String pathPostFix) {
        StringBuffer url = new StringBuffer();
        url.append(protocol).append(schemeDelimiter).append(host);
        if (port != 0) {
            url.append(":").append(port);
        }
        if (iD.isEmpty()) {
            log.info("No ID was given for the search:" + iD);

        }

        url.append(pathPrefix).append(version).append(queryType).append(iD);
        url.append(pathPostFix);
        return url;
    }
    public static StringBuffer constructUrl(Map<String, String> filterParams, String query, String protocol,
                                            String annotation, String schemeDelimiter, String host, int port,
                                            String pathPrefix, String version, String json) {
        StringBuffer url = new StringBuffer();
        url.append(protocol).append(schemeDelimiter).append(host);
        if (port != 0) {
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

    public static boolean isBodyEmpty(ResponseEntity responseEntity) {
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

}


