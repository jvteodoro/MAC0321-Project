package br.com.agendusp.agendusp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class GoogleRestClientConfig {

    @Bean
    public RestClient restClient(OAuth2AuthorizedClientManager authorizedClientManager){
        System.out.println("INSIDE GOOGLE REST CLIENT");
        OAuth2ClientHttpRequestInterceptor interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);

        return RestClient.builder().requestInterceptor(interceptor)
        .baseUrl("https://www.googleapis.com/calendar/v3/users/me/calendarList")
        .build();
    }
}
