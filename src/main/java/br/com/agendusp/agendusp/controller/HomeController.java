package br.com.agendusp.agendusp.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HomeController {
    @GetMapping("/")
    public String getHome() {
        return new String("Hello Home");
    }

    @GetMapping("/secured")
    public String getSecured() {
        
        return new String("Hello Secured");
    }
    
    
}
