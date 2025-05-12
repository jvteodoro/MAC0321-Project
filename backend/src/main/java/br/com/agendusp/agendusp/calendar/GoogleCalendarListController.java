package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.google.api.client.json.Json;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class GoogleCalendarListController { // implements CalendarListController {

    private final RestClient restClient;
    private final OAuth2AuthorizedClient auhtorizedClient;

    public GoogleCalendarListController(OAuth2AuthorizedClient authorizedClient, RestClient restClient ) {
        this.restClient = restClient;
        this.auhtorizedClient = authorizedClient;


    }

    public HttpStatusCode delete(){}
    public ArrayList<Calendar> get(Calendar calendar){}
    public CalendarList insert(Calendar calendar){}

    @GetMapping("/google/calendarList/list")
    public CalendarList list(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
          ResponseEntity<Json> calList = restClient.get()
                .uri("/calendarList")
                .headers(headers -> headers.setBearerAuth(auhtorizedClient.getAccessToken().getTokenValue()))
                .retrieve().toEntity(Json.class);
        CalendarListResource calendarListResource = new CalendarListResource();
    }
    public CalendarList patch(Calendar calendar){}
    public CalendarList update(Calendar calendar){}
    public WatchResponse watch(WatchRequest watchRequest){}
}
