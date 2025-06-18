package br.com.agendusp;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Calendar;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.BeanOverride;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.calendar.Calendar.CalendarList;

import br.com.agendusp.agendusp.AgendUspApplication;
import br.com.agendusp.agendusp.calendar.EventsResource;
import br.com.agendusp.agendusp.controller.CalendarDataController;
import br.com.agendusp.agendusp.controller.HomeController;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.documents.CalendarListUserItem;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.CalendarRepository;
import br.com.agendusp.agendusp.repositories.EventsRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

@SpringBootConfiguration
class TestConfig {
     @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.build();
    }
    @Bean
    public HomeController homeController(){
        return new HomeController();
    }

}


@SpringBootTest(classes=AgendUspApplication.class)
@AutoConfigureMockMvc
public class NewTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    EventsRepository eventsRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CalendarDataController calendarDataController;
    @Autowired
    EventsDataController eventsDataController;
    @Autowired
    UserDataController userDataController;
    
    
    @Test
    public void testHome() throws Exception{
        ResultActions result = mockMvc.perform(get("/")).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void testTeste() throws Exception{
        mockMvc.perform(get("/testeSecured")).andDo(MockMvcResultHandlers.print());
    }
    // private void insertTestEvents(){
    //     //eventsRepository.insert(ev1);

    // }

    private User setupFind(){
        User user = new User();
        String googleId = "4321234";
        String emailTest = "test@gmail.com";
        user.setId(googleId);
        user.setGoogleId(googleId);
        user.setEmail(emailTest);
        userRepository.delete(user);
        userRepository.insert(user);
        return user;
    }
    // private CalendarListUserItem setupCalendarListUserItem(){
    //     CalendarListUserItem calendarListUserItem = new CalendarListUserItem();
    //     calendarListUserItem.setCalendarId();
    // }

    @Test
    @WithMockUser
    public void findByGoogleIdTest() throws Exception {
        User user = setupFind();     
        Optional<User> recoveredUser = userRepository.findByGoogleId(user.getGoogleId());
        if (recoveredUser.isEmpty()){
        } else {
            assertEquals(objectMapper.writeValueAsString(user), 
                        objectMapper.writeValueAsString(recoveredUser.get()));
        }
        userRepository.delete(user);
        
    }
    @Test
    @WithMockUser
    public void updateOneByUserIdTest(){
        User user = setupFind();

    }

    @Test
    @WithMockUser
    public void genericTest() throws Exception{
        User user1 = new User();

        CalendarListUserItem calItem = new CalendarListUserItem();
        CalendarListUserItem calItem2 = new CalendarListUserItem();
        calItem.setCalendarId("teste@gmail.com");
        calItem2.setCalendarId("teste2@gmail.com");
        calItem.setId("1");
        calItem2.setId("2");


        user1.setUsername("user");
        user1.setId("12");
        //Delete pode dar quantas vezes quiser
        userRepository.deleteById("12");
        userRepository.deleteById("12");
        userRepository.deleteById("12");
        userRepository.deleteById("12");
        //Não pode dar insert duas vezes no mesmo item
        userRepository.insert(user1);

        Optional<User> user = userRepository.findById("12");
        if (user.isEmpty()) {
            System.out.println("Usuário não encontrado");
        } else {
            System.out.println(user.get().getName());
        }

        userRepository.updateOneByUserId(user1.getUserId(), calItem);
        userRepository.updateOneByUserId(user1.getUserId(), calItem2);
        Optional<User> newUser = userRepository.findById(user1.getId());
        if (!newUser.isEmpty()){
            String str = objectMapper.writeValueAsString(newUser);   
            System.out.println(str);
        }

        Optional<CalendarListUserItem> findedCalItem =userRepository.findCalendarListUserItemByUserIdAndCalendarId(user1.getUserId(), calItem.getCalendarId());
        if (!findedCalItem.isEmpty()){
            System.out.println(objectMapper.writeValueAsString(findedCalItem));
        }

    }

    @Test
    @WithMockUser
    public void testCalendarListInsert(){
        
    }

    @Test
    @WithMockUser
    public void testCalendarDataController(){
        User user1 = new User();
        user1.setUsername("user");
        user1.setId("12");

        CalendarListUserItem calItem = new CalendarListUserItem();
        calItem.setCalendarId("teste@gmail.com");
        calItem.setId("1");

        CalendarResource calResource = new CalendarResource();
        calResource.setId("1");
        
        CalendarListResource calListResource = new CalendarListResource();
        calListResource.setId("1");

        String userId = user1.getId();
        String calendarId = calItem.getCalendarId();
        String calendarResourceId = calResource.getId();
        String calendarListResourceId = calListResource.getId();

        userRepository.updateOneByUserId(user1.getUserId(), calItem);
        
        //adiciona o calendarlistusaritem
        Optional<CalendarListUserItem> findedCalItem = calendarDataController.addCalendarListUserItem(calResource, userId);
        if (!findedCalItem.isEmpty()){
            System.out.println(objectMapper.writeValueAsString(findedCalItem));
        }

        //adiciona o calendar 
        Optional<CalendarResource> calRes = calendarDataController.addCalendar(calResource, userId);
        if (!calRes.isEmpty()) {
            System.out.println(objectMapper.writeValueAsString(calRes));
        }
       
        //pega lista de calendarios

        Optional<CalendarListResource> calListResource1 =  calendarDataController.getCalendarListResource(calendarListResourceId, userId);
        if (!calListResource1.isEmpty()) {
            System.out.println(objectMapper.writeValueAsString(calRes));
        }
        //atualiza o calendar

        Optional<CalendarListResource> calListResource2 = calendarDataController.updateCalendar(calendarId, calListResource, userId);
        if (!calListResource2.isEmpty()) {
            System.out.println(objectMapper.writeValueAsString(calRes));
        }

        // pega lista de calendarios do usuario
        Optional<ArrayList<CalendarListResource>> calListResource3 = calendarDataController.getCalendars(userId);
        if (!calListResource3.isEmpty()) {
                System.out.println(objectMapper.writeValueAsString(calRes));
        }

        //deleta e vê se apagou
        calendarDataController.deleteCalendar(calendarId, userId);

        Optional<CalendarListResource> calListDelete = calendarDataController.getCalendarListResource(calendarId, userId);
        if (calListDelete.isEmpty()) {
            System.out.println("DELETADO COM SUCESSO");
        } else {
            System.out.println(objectMapper.writeValueAsString(calListUserItem));

        
        userRepository.deleteById("12");
        CalendarListUserItem.deleteById("1");
        CalendarResource.deleteById("1");
        CalendarListResource.deleteById("1");

        
    }

   
}
