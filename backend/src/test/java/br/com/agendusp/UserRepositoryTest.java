package br.com.agendusp;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agendusp.agendusp.AgendUspApplication;
import br.com.agendusp.agendusp.MongoTestContainer;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.calendarControllers.CalendarDataController;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.CalendarRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

@SpringBootTest(classes = AgendUspApplication.class)
@AutoConfigureMockMvc
public class UserRepositoryTest extends MongoTestContainer {
    @Autowired
    UserDataController userDataController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    private User setupFind() {
        
        String googleId = "4321234";
        String emailTest = "test@gmail.com";
        User user = new User(googleId, emailTest, googleId);
        Optional<User> userOp = userRepository.findById(googleId);
        if (userOp.isPresent()){
            userRepository.delete(user);
        }
        userRepository.insert(user);
        return user;
    }


    @Test
    @Order(1)
    @WithMockUser
    public void findByGoogleIdTest() throws Exception {
        User user = setupFind();
        Optional<User> recoveredUser = userRepository.findByGoogleId(user.getGoogleId());
        if (recoveredUser.isEmpty()) {
        } else {
            assertEquals(objectMapper.writeValueAsString(user),
                    objectMapper.writeValueAsString(recoveredUser.get()));
        }
        userRepository.delete(user);

    }  
    @Test
    @Order(2)
    @WithMockUser
    public void findByUserIdTest() throws Exception {
        User user = setupFind();
        Optional<User> recoveredUser = userRepository.findByUserId(user.getGoogleId());
        if (recoveredUser.isEmpty()) {
        } else {
            assertEquals(objectMapper.writeValueAsString(user),
                    objectMapper.writeValueAsString(recoveredUser.get()));
        }
        userRepository.delete(user);

    }

    @Test
    @Order(3)
    public void insertCalendarListResourceByUserIdTest() throws Exception{
        String calId = "testId";
        CalendarListResource calResource = new CalendarListResource();
        calResource.setId(calId);
        User user = setupFind();
        userRepository.insertCalendarListResourceByUserId(user.getId(), calResource);
        Optional<CalendarListResource> retrievedCalResource = userRepository.findById(user.getId())
        .orElseThrow(() -> new Exception("Erro, usuário não encontrado"))
        .getCalendarList()
        .stream().filter(p -> p.getId().equals(calId)).findFirst();
        if (retrievedCalResource.isPresent()){
            assertEquals(objectMapper.writeValueAsString(calResource),
                        objectMapper.writeValueAsString(retrievedCalResource));
        }

    }

    @Test
    @Order(4)
    public void findCalendarListResourceByIdAndCalendarIdTest() throws Exception{
        String calId = "testId";
        CalendarListResource calResource = new CalendarListResource();
        calResource.setId(calId);
        User user = setupFind();
        String userId = user.getId();
        userRepository.insertCalendarListResourceByUserId(userId, calResource);
        user = userRepository.findById(user.getId()).orElseThrow(() -> new Exception("Error creating user"));
        Optional<CalendarListResource> retrievedCalRes = userRepository.findCalendarListResourceByIdAndCalendarId(userId, calId);
        if  (retrievedCalRes.isPresent()){
            assertEquals(
                objectMapper.writeValueAsString(calResource), 
                objectMapper.writeValueAsString(retrievedCalRes));
        } else {
            throw new Exception("Error retrieving calendar list resource");
        }

    }

    @Test
    @Order(5)
    public void deleteCalendarListResourceByIdTest(){
        String calId = "testId";
        CalendarListResource calResource = new CalendarListResource();
        calResource.setId(calId);
        User user = setupFind();
        String userId = user.getId();
        userRepository.insertCalendarListResourceByUserId(userId, calResource);
        userRepository.deleteCalendarListResourceById(userId, calId);
        assertTrue(userRepository.findCalendarListResourceByIdAndCalendarId(userId, calId).isEmpty());
    }
    @Test
    @Order(5)
    public void refreshLinksTest(){
        String calId = "testId";
        CalendarListResource calResource = new CalendarListResource();
        calResource.setId(calId);
        User user = setupFind();
        String userId = user.getId();
        userRepository.insertCalendarListResourceByUserId(userId, calResource);
        userRepository.refreshLinks(calId);
        assertTrue(userRepository.findCalendarListResourceByIdAndCalendarId(userId, calId).isEmpty());
    }

    @Test
    @Order(6)
    public void existsByUserIdAndCalendarIdTest() {

        String userId = "testUser";
        String calendarId = "test-calendar-id";
        User user = new User(userId);
        CalendarListResource calendarListResource = new CalendarListResource();
        calendarListResource.setCalendarId(calendarId);
        user.addCalendarListResource(calendarListResource);
        userRepository.insert(user);
        assertTrue(userRepository.existsByUserIdAndCalendarId(userId, calendarId));
    }



    @Test
    @WithMockUser
    public void test() throws Exception {
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

        System.out.println("USer: " + objectMapper.writeValueAsString(userDataController.findUser(userId)));

        Optional<CalendarListResource> resp = userRepository.findCalendarListResourceByIdAndCalendarId(user.getId(),
                calR1.getCalendarId());
        if (resp.isEmpty()) {
            System.out.println("Resposta vazia");
        } else {
            System.out.println(objectMapper.writeValueAsString(resp.get()));
        }
    }
}
