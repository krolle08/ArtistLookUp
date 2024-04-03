package Application;

import Application.service.GetDataImpl;
import Application.utils.ScannerWrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class YourApplication {
    private static final Logger logger = LoggerFactory.getLogger(YourApplication.class.getName());

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(YourApplication.class, args);
        // Retrieve the managed bean from the Spring context
        SearchEngineManager searchEngineManager = context.getBean(SearchEngineManager.class);

        // Run the search engine
        searchEngineManager.runSearchEngine();

        // Close the Spring application context
        context.close();

        //Check if any threads are running that can prevent system shutdown
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        for (Thread thread : threads) {
            if (thread != null && thread.isAlive()) {
                logger.info("Thread " + thread.getName() + " is running.");
            }
        }
    }
}