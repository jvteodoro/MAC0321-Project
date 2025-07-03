package br.com.agendusp.agendusp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.security.test.context.support.WithMockUser;

import com.google.gson.Gson;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.UserRepository;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class UserRepositoryTest extends MongoTestContainer {
    @Autowired
    private UserRepository userRepository;
    private Gson gson = new Gson();

    @BeforeAll
    public static void setup() {
        mongoDBContainer.start();
    }



    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    @Order(2)
    public void testAddUserCalendarList() {
        User user = new User("testuser");
        System.out.println(user.getId());
        User fetchedUser = userRepository.findByUserId(user.getId()).orElse(null);
        assertEquals(user.getId(), fetchedUser.getId());

        CalendarListResource calendarListResource = new CalendarListResource();
        calendarListResource.setCalendarId("test-calendar-id");

        // TODO pois nunca é usado
        Integer num = userRepository.insertCalendarListResourceByUserId("testuser", calendarListResource)
                .orElseThrow(() -> new RuntimeException("Failed to add calendar list resource"));

        // TODO pois nunca é usado
        User updatedUser = userRepository.findByUserId("testuser")
                .orElseThrow(() -> new RuntimeException("User not found after update"));

        CalendarListResource fetchedCalendarListResource = userRepository
                .findCalendarListResourceByIdAndCalendarId("testuser", "test-calendar-id")
                .orElseThrow(() -> new RuntimeException("Calendar not found"));

        assertEquals(calendarListResource.getCalendarId(), fetchedCalendarListResource.getCalendarId());

    }
  
    // @Test
    // @Order(5)
    // public void testDeleteCalendarListResourceById(){
    // userRepository.deleteCalendarListResourceById("testuser",
    // "test-calendar-id");
    // boolean exists = userRepository.existsByCalendarId("testuser",
    // "test-calendar-id");
    // assertEquals(false, exists);
    // }

    // //@Test
    // //@Order(5)
    // public void testRefreshLinks(){
    // // necessário adicionar mais de um usuário com o mesmo calendarId
    // // Para testar o método refreshLinks
    // }

}
