package br.com.agendusp.agendusp.controller;

import java.io.FileInputStream;
import java.io.IOException;

//import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.calendar.Calendar;

@RestController
public class CalendarController {
    private final RestClient googleRestClient;

    public CalendarController(RestClient googleRestClient){
        this.googleRestClient = googleRestClient;
    }

    @GetMapping("/calendar")
    public ResponseEntity<String> calendarList(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) throws IOException{

        //HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        //JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        //Calendar.Builder calendarManagerBuilder = 
        //new Calendar.Builder(transport, jsonFactory).setApplicationName("");

        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        //System.out.println(accessToken);

       // OAuth2ClientHttpRequestInterceptor interceptor;

       // RestClient customClient = RestClient.builder()
       // .baseUrl("https://www.googleapis.com/calendar/v3/users/me/calendarList")
       // .defaultHeader("Authorization", "Bearer"+accessToken)
       // .build();
        //ResponseEntity<String> result = customClient.get().retrieve().toEntity(String.class);
//
        //System.out.println("Response Status: "+result.getStatusCode());
        //System.out.println("Response Headers: "+result.getHeaders());
        //System.out.println("Body: "+result.getBody());
        //System.out.println("ID: "+SecurityContextHolder.getContext().getAuthentication());
        //return result;
        //
        
        ResponseEntity<String> calList = googleRestClient.get()
                .uri("/calendarList")
                .retrieve().toEntity(String.class);
        return calList;
    }

}
