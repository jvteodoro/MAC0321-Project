package br.com.agendusp.agendusp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.UserRepository;

public class UserRepositoryController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final OAuth2User oAuth2User;

    public UserRepositoryController(OAuth2User oAuth2User, UserRepository userRepository) {
        this.oAuth2User = oAuth2User;
        this.userRepository = userRepository;
    }
    public void addUser() {
        User user = new User(oAuth2User.getName());
        user.setUsername(oAuth2User.getName());
        user.setEmail(oAuth2User.getAttributes().get("email").toString());
        user.setGoogleId(oAuth2User.getAttribute("sub")); // 'sub' é o ID estável do usuário no Google
        userRepository.insert(user);
    }
}
