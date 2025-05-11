package br.com.agendusp.agendusp.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.client.RestClient;

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
