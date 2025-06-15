package br.com.agendusp.agendusp.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HomeController {
    @Autowired
    AbstractDataController dataController;

    @GetMapping("/")
    public String getHome() {
        return new String("Hello Home");
    }

    @GetMapping("/secured")
    public String getSecured(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        //dataController.findUser()
        String userPrincipal = authorizedClient.getPrincipalName();
        authorizedClient.getClientRegistration().getClientName();
        
        return new String("Hello Secured:"+userPrincipal);

    }
}
