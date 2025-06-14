package br.com.agendusp.agendusp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.nimbusds.jose.proc.SecurityContext;

import br.com.agendusp.agendusp.CustomOAuth2User;
import br.com.agendusp.agendusp.documents.CalendarListResource;

public interface CalendarListController {
    public ResponseEntity<Void> delete(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser);

    public String get(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser);

    public String insert(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser);

    public String list(OAuth2User loggedUser);

    // public String patch(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser);

    public String update(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser);

    // public String watch(WatchRequest watchRequest);

}
