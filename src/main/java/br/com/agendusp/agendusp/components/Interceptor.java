package br.com.agendusp.agendusp.components;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import com.google.api.client.util.Value;

@Component
// This class is an interceptor for HTTP requests that adds an OAuth2 access token to the request headers.
public class Interceptor implements ClientHttpRequestInterceptor {

    //@Value("${spring.security.oauth2.client.registration.Google.client-id}")
    private static String REGISTRATION_ID = "Google";



    private final OAuth2AuthorizedClientManager    authorizedClientManager;

    @Autowired
    private Interceptor(OAuth2AuthorizedClientManager authorizedClientManager) {
        // Constructor logic if needed
        this.authorizedClientManager = authorizedClientManager;
    }

    @Override
    public ClientHttpResponse intercept( HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("INSIDE INTERCEPTOR");
       System.out.println("REQUISIOTN BODY: "+new String(body, StandardCharsets.UTF_8));


        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        OAuth2AuthorizeRequest req = OAuth2AuthorizeRequest.withClientRegistrationId(REGISTRATION_ID)
                .principal(authentication.getName())
                .build();
        
        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(req);
        if  (authorizedClient == null) {
            throw new IllegalStateException("No authorized client found");
        }

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        request.getHeaders().setBearerAuth(accessToken.getTokenValue());


        return execution.execute(request, body);
    }

   
}
