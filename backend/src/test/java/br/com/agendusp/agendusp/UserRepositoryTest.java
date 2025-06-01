package br.com.agendusp.agendusp;

import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.UserRepository;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class UserRepositoryTest extends MongoTestContainer{
    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    @Order(1)
    public void testInsertUser(){
        User user = new User();
        System.out.println(user.getUserId());
        userRepository.insertUser(user);
        User fetchedUser = userRepository.findByUserId(user.getUserId()).orElse(null);
        assertEquals(user,fetchedUser);
    }
    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    @Order(2)
    public void testAddUserCalendarList(){
        CalendarListResource calendarListResource = new CalendarListResource();
        calendarListResource.setId("test-calendar-id");
        userRepository.addUserCalendarList("testuser", calendarListResource);
        CalendarListResource fetchedCalendarListResource = userRepository
        .findCalendarListResourceByUserIdAndCalendarId("testuser", "test-calendar-id").orElse(null);
        assertEquals(calendarListResource, fetchedCalendarListResource);
    }

    @Test
    @Order(3)
    @WithMockUser(username = "testuser", roles = "USER")
    public void testFindCalendarListResourceByUserIdAndCalendarId(){
        CalendarListResource fetchedCalendarListResource = userRepository
        .findCalendarListResourceByUserIdAndCalendarId("testuser", "test-calendar-id").orElse(null);
        assertEquals("test-calendar-id", fetchedCalendarListResource.getId());
    }

    @Test
    @Order(4)
    public void testExistsByCalendarId(){
        boolean exists = userRepository.existsByCalendarId("testuser", "test-calendar-id");
        assertEquals(true, exists);
    }
    
    @Test
    @Order(5)
    public void testDeleteCalendarListResourceById(){
        userRepository.deleteCalendarListResourceById("testuser", "test-calendar-id");
        boolean exists = userRepository.existsByCalendarId("testuser", "test-calendar-id");
        assertEquals(false, exists);
    }

    //@Test
    //@Order(5)
    public void testRefreshLinks(){
        // necessário adicionar mais de um usuário com o mesmo calendarId
        // Para testar o método refreshLinks
    }


}
