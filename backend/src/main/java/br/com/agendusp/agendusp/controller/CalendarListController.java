package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import br.com.agendusp.agendusp.documents.CalendarListResource;

public interface CalendarListController {
    public ResponseEntity<Void> delete(String calendarId, OAuth2AuthorizedClient authorizedClient);

    public CalendarListResource get(String calendarId, OAuth2AuthorizedClient authorizedClient);

    public CalendarListResource insert(CalendarListResource calendar,  OAuth2AuthorizedClient authorizedClient);

    public ArrayList<CalendarListResource> list( OAuth2AuthorizedClient authorizedClient);

    // public String patch(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser);

    public CalendarListResource update(CalendarListResource calendar,  OAuth2AuthorizedClient authorizedClient);

    // public String watch(WatchRequest watchRequest);

}
