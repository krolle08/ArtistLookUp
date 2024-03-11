package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Scanner;


@SpringBootApplication
public class SpringBootApp {
    public void runApplication() {
        // Create an instance of a class from the submodule
        SubmoduleClass submoduleObject = new SubmoduleClass();

        // Use the methods or properties of the submodule class
        String submoduleResult = submoduleObject.someMethod();

        // Print the result or perform other application logic
        System.out.println("Application is running! Submodule result: " + submoduleResult);
    }
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);

        Scanner scanner = new Scanner(System.in);






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
