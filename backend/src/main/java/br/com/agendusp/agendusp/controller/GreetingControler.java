package br.com.agendusp.agendusp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class GreetingControler {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue ="World") String name, 
    Model model) {
        model.addAttribute("name", name);
        return new String("greeting");
    }
    
    @GetMapping("/teste")
    public String getTeste() {
        return new String("teste");
    }
    

}
