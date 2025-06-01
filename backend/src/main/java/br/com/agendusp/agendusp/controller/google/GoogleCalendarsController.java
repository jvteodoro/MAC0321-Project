package br.com.agendusp.agendusp.controller.google;

// import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

//@RestController
public class GoogleCalendarsController {//implements CalendarsController {
    private final RestClient restClient; 

    public GoogleCalendarsController(RestClient restClient) {
        this.restClient = restClient;   
    }
    //GoogleCalendarListController calendarListController = new GoogleCalendarListController(restClient);
}
