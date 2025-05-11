package br.com.agendusp.agendusp.config;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

@Configuration
public class GoogleRestClientConfig {

	@Bean
	public RestClient restClient(OAuth2AuthorizedClientManager authorizedClientManager) {

		return RestClient.builder()
                .baseUrl("https://www.googleapis.com/calendar/v3/users/me")
				//.requestInterceptor(requestInterceptor)
				.build();
	}
}
