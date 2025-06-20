package br.com.agendusp;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agendusp.agendusp.AgendUspApplication;
import br.com.agendusp.agendusp.MongoTestContainer;
import br.com.agendusp.agendusp.controller.CalendarDataController;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.CalendarRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

@SpringBootTest(classes = AgendUspApplication.class)
public class UserRepositoryTest extends MongoTestContainer{
    @Autowired
    UserDataController userDataController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void test() throws Exception{
        System.out.println("Teste");
        String userId = "teste@gmail.com";
        User user = new User();
        user.setId(userId);
        ArrayList<CalendarListResource> calendarList = new ArrayList<>();

        CalendarListResource calR1 = new CalendarListResource();
        CalendarListResource calR2 = new CalendarListResource();
        calR1.setId("calR1");
        calR1.setCalendarId("calR1");
        calR2.setId("calR2");
        calR2.setCalendarId("calR2");
        
        calendarList.add(calR1);
        calendarList.add(calR2);

        user.setCalendarList(calendarList);
        userDataController.createUser(user);
        
        Optional<CalendarListResource> resp = userRepository.findCalendarListResourceByIdAndCalendarId(user.getUserId(), calR1.getCalendarId());
        if (resp.isEmpty()) {
            System.out.println("Resposta vazia");
        } else {
            System.out.println(objectMapper.writeValueAsString(resp.get()));
        }
    }
}
