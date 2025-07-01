package br.com.agendusp.agendusp.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.google.GoogleCalendarListController;
import br.com.agendusp.agendusp.controller.google.GoogleCalendarsController;
import br.com.agendusp.agendusp.controller.google.GoogleDataToLocalData;
import br.com.agendusp.agendusp.controller.google.GoogleEventsController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    GoogleDataToLocalData gDataToLocalData;
    @Autowired
    GoogleCalendarListController gCalendarListController;
    @Autowired
    GoogleEventsController gEventsController;
    @Autowired
    GoogleCalendarsController gCalendarsController;
    @Autowired
    UserDataController userDataController;
    @Autowired
    ObjectMapper objMapper;
    @Autowired
    OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName()
            );
            if (authorizedClient != null) {
                System.out.println("Downloading google info");
                gDataToLocalData.getUserInfo(authorizedClient);
                try {
                    gDataToLocalData.cloudToLocal(authorizedClient);
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        }
        System.out.println("Google info donwload ended");
        // Redirecione ou responda conforme necessário
        response.sendRedirect("http://localhost:3000/login-success");
    }
}
