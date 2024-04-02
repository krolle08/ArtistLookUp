package Application;

import Application.api.*;
import Application.service.GetDataImpl;
import Application.utils.ScannerWrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YourApplication {
    static int typoLimit = 10;
    static int consecutiveTypoMistakes = 0;

    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
        ScannerWrapper scanner;

        scanner = new ScannerWrapper();
        GetDataImpl getData = new GetDataImpl(scanner, new MusicBrainzNameSearchRoute(),
                new MusicBrainzIDSearchRoute(), new CoverArtArchiveService(), new WikidataSearchRoute(),
                new WikipediaSearchRoute());
        try {
            getData.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Check if any threads are running that can prevent system shutdown
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        for (Thread thread : threads) {
            if (thread != null && thread.isAlive()) {
                System.out.println("Thread " + thread.getName() + " is running.");
            }
        }
        System.exit(1);
    }
}