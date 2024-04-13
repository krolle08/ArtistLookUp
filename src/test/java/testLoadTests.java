import Application.Application;
import Application.service.Artist.SearchArtistService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * These tests are made as End-To-End test for when the application is running
 */
@Disabled("This test is still under development and should not run automatically")
@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = {"local.server.port=8080"}) // Set the port number for testing
public class testLoadTests {

    @Autowired
    SearchArtistService searchArtistService;

    private String port = "8080";

    @Test
    public void testLoadScenario() throws Exception {
        int numUsers = 10;
        HttpClient httpClient = HttpClients.createDefault();
        ExecutorService executor = Executors.newFixedThreadPool(numUsers);

        List<String> responses = new ArrayList<>();
        // Define the base URL of the application
        String baseUrl = "http://localhost:" + port;

        // Define the endpoint URL
        String endpoint = "/artist/Nirvana";

        // HttpClient instance

        for (int i = 0; i < numUsers; i++) {
            executor.submit(() -> {
                try {
                    // Prepare the request URL
                    String requestUrl = baseUrl + endpoint;

                    // Create an HTTP GET request
                      HttpGet request = new HttpGet(requestUrl);

                    // Execute the request
                       HttpResponse response = httpClient.execute(request);

                    // Consume the response entity
                    responses.add(EntityUtils.toString(response.getEntity()));

                    // Ensure the response is fully consumed to release the connection
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                    fail();
                }
            });
        }

        // Shutdown the executor and wait for all tasks to complete
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);
    }
}
