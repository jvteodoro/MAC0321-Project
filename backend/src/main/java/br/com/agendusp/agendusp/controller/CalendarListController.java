package br.com.agendusp.agendusp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import br.com.agendusp.agendusp.CustomOAuth2User;
import br.com.agendusp.agendusp.documents.CalendarListResource;

public interface CalendarListController {
    public ResponseEntity<Void> delete(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser);

    public String get(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser);

    public String insert(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser);

    public String list(@AuthenticationPrincipal CustomOAuth2User customUser);

    // public String patch(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser);

    public String update(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser);

    // public String watch(WatchRequest watchRequest);

}
