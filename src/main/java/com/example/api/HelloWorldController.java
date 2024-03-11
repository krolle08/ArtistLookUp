package com.example.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloWorldController {

    @GetMapping("/hello")
    public String helloYou() {

        return "Du gjorde det";
    }

    @GetMapping("/farvel")
    public String farvelYou() {
        return "Du gjorde det igen";
    }
}
