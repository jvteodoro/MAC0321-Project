package br.com.agendusp.agendusp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class CalendarController {
    private final RestClient restClient;

    public CalendarController(RestClient restClient){
        this.restClient = restClient;
    }

    @GetMapping("/calendar")
    public ResponseEntity<String> calendarList(){
        String calList = this.restClient.get()
        .uri("https://www.googleapis.com/calendar/v3/users/me/calendarList")
        .retrieve().body(String.class);

        return ResponseEntity.ok(calList);
    }

}
