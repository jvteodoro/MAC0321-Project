package br.com.agendusp.agendusp;

import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import br.com.agendusp.agendusp.calendar.UserRepositoryController;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.UserRepository;

@SpringBootTest
public class TestUser {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRepositoryController userRepositoryController;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testUserPrincipal(){
        userRepositoryController.addUser();
        Optional<User> userOptional = userRepository.findByName("testuser");
        User user;
        if (userOptional.isPresent()){
            user =  userOptional.get();
            System.out.println(user);

        }
    }
}
