package br.com.agendusp;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
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
import br.com.agendusp.agendusp.controller.EventsDataController;
import br.com.agendusp.agendusp.controller.HomeController;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarListUserItem;
import br.com.agendusp.agendusp.documents.CalendarResource;
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
}
    
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
    public void testCalendarDataController() throws Exception{
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
        CalendarListUserItem findedCalItem = calendarDataController.addCalendarListUserItem(calResource, userId);
        System.out.println(objectMapper.writeValueAsString(findedCalItem));

        //adiciona o calendar 
        CalendarResource calRes = calendarDataController.addCalendar(calResource, userId);
        System.out.println(objectMapper.writeValueAsString(calRes));
        
       
        //pega lista de calendarios

        CalendarListResource calListResource1 =  calendarDataController.getCalendarListResource(calendarListResourceId, userId);
        
        System.out.println(objectMapper.writeValueAsString(calListResource1));
        
        //atualiza o calendar

        CalendarListResource calListResource2 = calendarDataController.updateCalendar(calendarId, calListResource, userId);
        System.out.println(objectMapper.writeValueAsString(calListResource2));
        

        // pega lista de calendarios do usuario
        ArrayList<CalendarListResource> calListResource3 = calendarDataController.getCalendars(userId);
        
        System.out.println(objectMapper.writeValueAsString(calListResource3));
        

        //deleta e vê se apagou
        calendarDataController.removeCalendar(calendarId, userId);

        CalendarListResource calListDelete = calendarDataController.getCalendarListResource(calendarId, userId);
        
        // System.out.println("DELETADO COM SUCESSO");

        
        userRepository.deleteById("12");
        // CalendarListUserItem.deleteById("1");
        // CalendarResource.deleteById("1");
        // CalendarListResource.deleteById("1");

        
    }

    @Test
    @WithMockUser
    public void testEventsDataController() throws Exception{
        User user1 = new User();
        user1.setUsername("user");
        user1.setId("12");
        String userId = user1.getId();

        CalendarListUserItem calItem = new CalendarListUserItem();
        calItem.setCalendarId("teste@gmail.com");
        calItem.setId("1");
        String calendarId = calItem.getCalendarId();

        EventsResource eventsResource = new EventsResource();
        eventsResource.setId("1");
        String endDate = eventsResource.getEnd();
        String eventId = eventsResource.getId();

        userRepository.updateOneByUserId(user1.getUserId(), calItem);

        String atendeeUserId = "3"; 

        // pega eventos em um intervalo
        ArrayList<EventsResource> eventsResource1 = eventsDataController.getEventsOnInterval(calendarId, endDate);
        System.out.println(objectMapper.writeValueAsString(eventsResource1));

        //cria um evento
        EventsResource eventsResource2 = eventsDataController.createEvent(calendarId, eventsResource, userId);
        System.out.println(objectMapper.writeValueAsString(eventsResource2));
        
        //adiciona um calendario ao evento
        EventsResource eventsResource3 = eventsDataController.addCalendarToEvent(calendarId, eventId, userId);
        System.out.println(objectMapper.writeValueAsString(eventsResource3));
        
        //adiciona um calendario ao evento
        EventsResource eventsResource4 = eventsDataController.addAtendeeToEvent(eventId, calendarId, userId, atendeeUserId);
        System.out.println(objectMapper.writeValueAsString(eventsResource4));

        //pega o evento
        EventsResource eventsResource5 = eventsDataController.getEvent(eventId, calendarId, userId);
        System.out.println(objectMapper.writeValueAsString(eventsResource5));

        //atualiza o evento
        EventsResource eventsResource6 = eventsDataController.updateEvent(calendarId, eventId, eventsResource, userId);
        System.out.println(objectMapper.writeValueAsString(eventsResource6));

        //atualiza parcialmente a lista de eventos do calendario
        EventsResource eventsResource7 = eventsDataController.patchEvent(calendarId, eventId, eventsResource, userId);
        System.out.println(objectMapper.writeValueAsString(eventsResource7));

        //pega a lista de eventos do calendario
        ArrayList<EventsResource> eventsResource8 = eventsDataController.getEvents(calendarId, userId);
        System.out.println(objectMapper.writeValueAsString(eventsResource8));

        //deleta o evento e verifica se apagou
        removeEvent(eventId, calendarId, userId);

        ArrayList<String> eventsRemove = eventsResource.getCalendarIds();
        System.out.println(objectMapper.writeValueAsString(eventsRemove));
        System.out.println("Veja se o calendário com id " + calendarId +  " foi removido");


        //cancela o totalmente o evento
        cancelEvent( eventId, calendarId, userId);

        EventsResource eventsRemove = eventsDataController.getEvent(eventId, calendarId, userId);
        System.out.println(objectMapper.writeValueAsString(eventsRemove));

        userRepository.deleteById("12");
}
