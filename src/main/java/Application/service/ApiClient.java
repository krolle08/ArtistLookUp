package Application.service;

import org.springframework.http.ResponseEntity;

// ApiClient.java (Abstract)
public abstract class ApiClient {

    public abstract String buildUrl(String path);
    public abstract <T> ResponseEntity<T> getForObject(String url, Class<T> responseType) throws Exception;


    // Other abstract methods for HTTP requests and error handling (optional)
}