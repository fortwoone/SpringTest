package com.fortwoone.springtest;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/bonjour")
    public Greeting greeting() {
        return new Greeting(counter.incrementAndGet(), "Bonjour tout le monde !");
    }
}