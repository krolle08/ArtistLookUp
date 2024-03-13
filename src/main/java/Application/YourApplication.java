package Application;

import org.springframework.boot.SpringApplication;
import Application.service.GetDataImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
        GetDataImpl getData = new GetDataImpl();
        try {
            getData.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
