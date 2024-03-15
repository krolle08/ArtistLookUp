package Application.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WikipediaSearchRoute {

    @GetMapping("/test")
    public String helloYou() {
        return "Hello World Controller";
    }

    @GetMapping("/t")
    public String farvelYou() {
        return "Du gjorde det igen";
    }
}
