package com.example.api;

import com.sun.glass.ui.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ApplicationConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testHelloWorldController {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testHelloWorldEndpoint() {
        String response = testRestTemplate.getForObject("http://localhost:" + port + "/hello", String.class);
        assertThat(response).isEqualTo("Du gjorde det");
    }

    @Test
    public void testFarvelWorldEndpoint() {
        String response = testRestTemplate.getForObject("http://localhost:" + port + "/farvel", String.class);
        assertThat(response).isEqualTo("Du gjorde det");
    }
}
