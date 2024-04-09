import Application.Application;
import Application.service.Artist.SearchArtistService;
import Application.utils.TestConfig;
import Application.utils.TypeOfSearchEnum;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * These tests are made as End-To-End test for when the application is running
 */
@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = {"local.server.port=8080"}) // Set the port number for testing
public class testLoadTests {

    @Autowired
    TestConfig testConfig;

    @Autowired
    SearchArtistService searchArtistService;

    @Test
    public void testLoadScenario() throws Exception {
        int numUsers = 1;
        HttpClient httpClient = HttpClients.createDefault();
        ExecutorService executor = Executors.newFixedThreadPool(numUsers);

        // Define the base URL of your application
        String baseUrl = "http://localhost:" + testConfig.getPortNumber(); // Use port from configuration

        // Define the endpoint URL
        String endpoint = "/artist/Nik&Jay"; // Change this to the desired artist ID

        // Create an HttpClient instance

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
                         String responseBody = EntityUtils.toString(response.getEntity());

                    // Optionally, you can log or assert the response if needed

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
