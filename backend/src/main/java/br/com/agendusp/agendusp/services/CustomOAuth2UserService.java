package br.com.agendusp.agendusp.services;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import br.com.agendusp.agendusp.CustomOAuth2User;
import br.com.agendusp.agendusp.User;
import br.com.agendusp.agendusp.repositories.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Pega dados da resposta do google
        String googleId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Cria o usuário no MongoDB
        User user = userRepository.findByGoogleId(googleId)
                .orElseGet(() -> {
                    User newUser = new User(googleId, email, name);
                    return userRepository.save(newUser);
                });

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }
}
