package Application;

import Application.utils.LoggingUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * The main class of the application, serving as the entry point.
 * This class initializes the Spring application context.
 */

@EnableAsync
@SpringBootApplication
public class Application {
    @Autowired
    private Environment env;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Post-construction initialization method.
     * Prints the active profiles to the console.
     */
    @PostConstruct
    public void init() {
        String activeProfiles = Arrays.toString(env.getActiveProfiles());
        LoggingUtility.info("Active profiles: " + activeProfiles);
    }
}