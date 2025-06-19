package br.com.agendusp.agendusp.controller.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleDataToLocalData {
    @Autowired
    GoogleCalendarListController gCalendarListController;

    @GetMapping("/google/cloudToLocal")
    public void cloudToLocal(OAuth2AuthorizedClient authorizedClient){
        
    }
}
