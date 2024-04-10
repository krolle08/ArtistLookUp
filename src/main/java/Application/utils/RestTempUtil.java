package Application.utils;

import Application.service.Artist.AlbumInfoObj;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RestTempUtil {
    private static final Logger logger = LoggerFactory.getLogger(RestTempUtil.class.getName());


    public static URI constructUri(String id,RestTemplateConfig config) {
        StringBuilder uriBuilder = new StringBuilder()
                .append(config.getProtocol()).append("://")
                .append(config.getHost())
                .append(config.getPathPrefix())
                .append(config.getVersion())
                .append(config.getQueryTypeArtist() != null ? config.getQueryTypeArtist() : "")
                .append(id)
                .append(config.getPathPostFix());

        if (config.getJson() != null) {
            uriBuilder.append("&fmt=").append(config.getJson());
        }
        if (config.getPostPreFix() != null) {
            uriBuilder.append("&inc=").append(config.getPostPreFix());
        }
        return URI.create(uriBuilder.toString());

    }

    public static URI constructUri(Map<String, String> filterParams, String query, RestTemplateConfig config) {
        String path = config.getPathPrefix() + config.getVersion() + config.getPathPostFix() + query;
        StringBuilder uriBuilder = new StringBuilder()
                .append(config.getProtocol()).append("://")
                .append(config.getHost()).append(":").append(config.getPort())
                .append(path);

        filterParams.forEach((key, value) -> uriBuilder.append(key.toLowerCase()).append(":")
                .append(encodeIfNeeded(value.toLowerCase())));

        if (config.getJson() != null) {
            uriBuilder.append("&").append(config.getJson());
        }
        if (config.getPostPreFix() != null) {
            uriBuilder.append("&").append(config.getPostPreFix());
        }
        return URI.create(uriBuilder.toString());
    }

    public static URI constructUriWikiData(String searchTerm, RestTemplateConfig config) {
        StringBuilder uriBuilder = new StringBuilder()
                .append(config.getProtocol()).append("://")
                .append(config.getHost());
        return UriComponentsBuilder.fromUriString(uriBuilder + config.getPathPrefix() + config.getPathPostFix())
                .queryParam("action", "wbgetentities")
                .queryParam("format", "json")
                .queryParam("ids", searchTerm)
                .queryParam("props", "sitelinks")
                .build()
                .toUri();
    }

    public static URI constructUriWikiPedia(String searchTerm, RestTemplateConfig config) {
        String searchValue = decodeString(searchTerm);
        String path = config.getPathPrefix() + config.getPathPostFix();
        return UriComponentsBuilder.newInstance()
                .scheme(config.getProtocol())
                .host(config.getHost())
                .path(path)
                .queryParam("action", "query")
                .queryParam("format", "json")
                .queryParam("prop", "extracts")
                .queryParam("exintro", true)
                .queryParam("redirects", true)
                .queryParam("titles", searchValue)
                .build()
                .toUri();
    }

    public static boolean isBodyEmpty(ResponseEntity<String> responseEntity) {
            String responseBody = responseEntity.getBody();
            return responseBody == null || responseBody.isBlank();
            }

    public static RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    public static ResponseEntity<String> getResponse(URI uri) {
        RestTemplate restTemplate = getRestTemplate();
        return restTemplate.getForEntity(uri, String.class);
    }

    public static String encodeIfNeeded(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        try {
            return java.net.URLEncoder.encode(input, StandardCharsets.UTF_8.name())
                    .replaceAll("\\+", "%20")
                    .replaceAll("_", "%20") // Replace underscores with %5F
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")");
        } catch (Exception e) {
            logger.warn("Error encoding input: " + input);
            throw new RuntimeException(e);
        }
    }

    public static String decodeString(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        try {
            return java.net.URLDecoder.decode(input, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            logger.warn("Error decoding input: " + input);
            throw new RuntimeException(e);
        }
    }
}


