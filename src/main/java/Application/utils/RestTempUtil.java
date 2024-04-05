package Application.utils;

import Application.service.Artist.SearchArtistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RestTempUtil {
    private static final Logger logger = LoggerFactory.getLogger(SearchArtistService.class.getName());

    public static StringBuffer constructUri(String id, String queryType, String protocol, String schemeDelimiter, String host, int port, String pathPrefix, String version, String pathPostFix) {
        StringBuffer url = new StringBuffer();
        url.append(protocol).append(schemeDelimiter).append(host);
        String queryterm;
        if (port != 0) {
            url.append(":").append(port);
        }
        if (id.isEmpty()) {
            logger.info("No ID was given for the search:" + id);
        }
        if (id.contains(" ")) {
            queryterm = encodeString(id);
        } else {
            queryterm = id;
        }

        url.append(pathPrefix).append(version).append(queryType).append(queryterm);
        url.append(pathPostFix);
        return url;
    }

    public static StringBuffer constructUri(Map<String, String> filterParams, String query, String protocol,
                                            String annotation, String schemeDelimiter, String host, int port,
                                            String pathPrefix, String version, String json) {
        StringBuffer url = new StringBuffer();
        url.append(protocol).append(schemeDelimiter).append(host);
        if (port != 0) {
            url.append(":").append(port);
        }
        url.append(pathPrefix).append(version).append(annotation).append(query);
        if (filterParams.isEmpty()) {
            logger.info("No parameter for the search has been provided");
            return new StringBuffer();
        } else {
            // Append each parameter from filterParams
            for (Map.Entry<String, String> entry : filterParams.entrySet()) {
                String paramName = entry.getKey().toLowerCase();
                String paramValue;
                if (entry.getValue().contains(" ")) {
                    paramValue = RestTempUtil.encodeString(entry.getValue());
                } else {
                    paramValue = entry.getValue();
                }
                // Append parameter name and value to URL
                url.append(paramName).append(":").append(paramValue);
            }
        }
        url.append("&").append(json);
        return url;
    }

    public static String constructUri(String searchTerm, String protocol, String schemeDelimiter, String host,
                                      String pathPrefix, String api, String postPreFix) {
        StringBuffer url = new StringBuffer();
        url.append(protocol).append(schemeDelimiter).append(host);
        if (searchTerm.isEmpty()) {
            logger.info("No search term was given for the search:" + searchTerm);
        }
        url.append(pathPrefix).append(api).append(postPreFix);
        url.append(searchTerm);
        return url.toString();
    }

    public static boolean isBodyEmpty(ResponseEntity responseEntity, String criteria) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(responseEntity.getBody().toString());
            if (criteria == null && !rootNode.isEmpty()) {
                return false;
            }
            if (rootNode.path(criteria).isEmpty()) {
                return true;
            }
        } catch (JsonProcessingException e) {
            logger.warn("Failed reading the rootnode. " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static RestTemplate restTemplate(String host, int port) {
        HttpHost proxy = new HttpHost(host, port);
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        return new RestTemplateBuilder().requestFactory(() -> new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().setDefaultRequestConfig(config).build())).build();
    }

    public static ResponseEntity<String> getResponse(URI uri, String host, int port) {
        RestTemplate restTemplate = RestTempUtil.restTemplate(host, port);
        return restTemplate.getForEntity(uri, String.class);
    }

    public static ResponseEntity<String> getResponse(URI uri) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(uri, String.class);
    }

    public static String encodeString(String input) {
        try {
            return URLEncoder.encode(input, StandardCharsets.UTF_8.name())
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")");
        } catch (UnsupportedEncodingException e) {
            logger.warn("Error encoding input: " + input);
            throw new RuntimeException(e);
        }
    }

    public static String decodeString(String input) {
        try {
            return URLDecoder.decode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.warn("Error decoding input: " + input);
            e.printStackTrace();
            return input;
        }
    }
}


