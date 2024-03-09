package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class YourApplication {

    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);

        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Make a GET request to /hello endpoint
        ResponseEntity<String> helloResponse = restTemplate.getForEntity("http://localhost:8080/hello", String.class);
        System.out.println("Response from /hello endpoint: " + helloResponse.getBody());

        // Make a GET request to /farvel endpoint
        ResponseEntity<String> farvelResponse = restTemplate.getForEntity("http://localhost:8080/farvel", String.class);
        System.out.println("Response from /farvel endpoint: " + farvelResponse.getBody());
    }
}
