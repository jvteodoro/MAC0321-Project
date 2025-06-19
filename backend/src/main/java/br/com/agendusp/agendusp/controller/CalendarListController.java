package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.nimbusds.jose.proc.SecurityContext;

import br.com.agendusp.agendusp.CustomOAuth2User;
import br.com.agendusp.agendusp.documents.CalendarListResource;

public interface CalendarListController {
    public ResponseEntity<Void> delete(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser);

    public String get(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser);

    public CalendarListResource insert(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser);

    public ArrayList<CalendarListResource> list(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient);

    // public String patch(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser);

    public CalendarListResource update(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser);

    // public String watch(WatchRequest watchRequest);

}
