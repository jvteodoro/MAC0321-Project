package br.com.agendusp.agendusp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import br.com.agendusp.agendusp.repositories.UserRepository;
import br.com.agendusp.agendusp.services.CustomOAuth2UserService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/").permitAll();
            auth.anyRequest().authenticated();
        })
                .oauth2Login(withDefaults())
                .oauth2Client(withDefaults())
                .formLogin(withDefaults())
                .build();

    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> OAuth2UserService() {
        return new CustomOAuth2UserService(userRepository);
    }
}
