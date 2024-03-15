package Application;

import Application.api.MusicBrainzNameSearchRoute;
import org.springframework.boot.SpringApplication;
import Application.service.GetDataImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
        Scanner scanner = new Scanner(System.in);
        GetDataImpl getData = new GetDataImpl(scanner, new MusicBrainzNameSearchRoute());
        try {
            getData.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
