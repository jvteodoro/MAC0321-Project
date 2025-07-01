package br.com.agendusp.agendusp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GoogleRestClientConfig {

	@Bean
	public RestClient restClient() {

		return RestClient.builder()
                .baseUrl("https://www.googleapis.com/calendar/v3/users/me")
				//.defaultHeader("Bearer", authorizedClient.getAccessToken().getTokenValue())//headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
				//.requestInterceptor(requestInterceptor)
				.build();
	}
}
