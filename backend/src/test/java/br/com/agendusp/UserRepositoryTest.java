package br.com.agendusp;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.agendusp.agendusp.MongoTestContainer;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.documents.User;

@SpringBootTest
public class UserRepositoryTest extends MongoTestContainer{
    @Autowired
    UserDataController userDataController;
    @Test
    public void test(){
        System.out.println("Teste");
        String userId = "teste@gmail.com";
        User user = new User();
        user.setId(userId);
        userDataController.createUser(user);
        
    }
}
