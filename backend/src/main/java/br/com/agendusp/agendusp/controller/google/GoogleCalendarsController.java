package br.com.agendusp.agendusp.controller.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import br.com.agendusp.agendusp.controller.calendarControllers.CalendarDataController;
import br.com.agendusp.agendusp.documents.CalendarResource;

@RestController
public class GoogleCalendarsController {// implements CalendarsController {
    @Autowired
    private RestClient restClient;
    @Autowired
    private CalendarDataController calendarDataController;

    @GetMapping("/google/calendar/get")
    public CalendarResource get(@RequestParam String calendarId,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        CalendarResource calResource = restClient.get()
                .uri("https://www.googleapis.com/calendar/v3/calendars/" + calendarId)
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                .retrieve().toEntity(CalendarResource.class).getBody();
        try {
            calendarDataController.addCalendar(calResource, authorizedClient.getPrincipalName());
        } catch (Exception e) {
            System.err.println(e);
        }
        return calResource;
    }
}
