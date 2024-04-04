package Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * The main class of the application, serving as the entry point.
 * This class initializes the Spring application context, runs the search engine,
 * and handles any exceptions that may occur during execution.
 */
@SpringBootApplication
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class.getName());
    @Autowired
    private Environment env;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        try {
            // Retrieve the managed bean from the Spring context
            SearchEngineController searchEngineController = context.getBean(SearchEngineController.class);

            searchEngineController.runSearchEngine();
        } catch (Exception e) {
            logger.error("An error occurred: " + e.getMessage(), e);
        } finally {
            // Close the Spring application context
            if (context != null) {
                context.close();
            }
        }

        //Check if any threads are running that can prevent system shutdown
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        for (Thread thread : threads) {
            if (thread != null && thread.isAlive()) {
                logger.info("Thread " + thread.getName() + " is running.");
            }
        }
    }

    /**
     * Post-construction initialization method.
     * Prints the active profiles to the console.
     */
    @PostConstruct
    public void init() {
        String activeProfiles = Arrays.toString(env.getActiveProfiles());
        System.out.println("Active profiles: " + activeProfiles);
    }
}