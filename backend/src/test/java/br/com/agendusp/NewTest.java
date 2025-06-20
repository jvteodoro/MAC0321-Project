package br.com.agendusp;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.agendusp.agendusp.AgendUspApplication;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.controller.CalendarDataController;
import br.com.agendusp.agendusp.controller.EventsDataController;
import br.com.agendusp.agendusp.controller.HomeController;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.dataobjects.EventDate;
import br.com.agendusp.agendusp.documents.CalendarListResource;
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
    // private CalendarListResource setupCalendarListResource(){
    //     CalendarListResource calendarListUserItem = new CalendarListResource();
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

        CalendarListResource calItem = new CalendarListResource();
        CalendarListResource calItem2 = new CalendarListResource();
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

        userRepository.insertCalendarListResourceByUserId(user1.getUserId(), calItem);
        userRepository.insertCalendarListResourceByUserId(user1.getUserId(), calItem2);
        Optional<User> newUser = userRepository.findById(user1.getId());
        if (!newUser.isEmpty()){
            String str = objectMapper.writeValueAsString(newUser);   
            System.out.println(str);
        }

        Optional<CalendarListResource> findedCalItem =userRepository.findCalendarListResourceByIdAndCalendarId(user1.getUserId(), calItem.getCalendarId());
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
    public void addCalendarListResourceFromCalendarTest() throws Exception{
        User user1 = new User();
        user1.setId("afadsfsf@ajodjf.com");
        user1.setGoogleId(user1.getId());
        user1.setUserId(user1.getGoogleId());
        userRepository.delete(user1);
        userRepository.insert(user1);
        System.out.println(objectMapper.writeValueAsString(user1));
        CalendarResource calR = new CalendarResource();
        calR.setCalendarId("1231233@gmail.com");
        calR.setId(calR.getCalendarId());
        System.out.println("Usuario: "+user1.getUserId());
        System.out.println("\n\nCalendário passado para addCalendar: "+objectMapper.writeValueAsString(calR));
        calendarDataController.removeCalendar(calR.getCalendarId(), user1.getUserId());
        calendarDataController.addCalendar(calR, user1.getUserId());

        CalendarListResource response = calendarDataController.addCalendarListResourceFromCalendar(user1.getUserId(), calR.getCalendarId());
        CalendarListResource fetched = calendarDataController.getCalendarListResource(calR.getCalendarId(), user1.getUserId());
        assertEquals(objectMapper.writeValueAsString(calR.toCalendarListResource()), 
        objectMapper.writeValueAsString(fetched));
    }

    @Test
    @WithMockUser
    public void testCalendarDataController() throws Exception{
        User user1 = new User();
        user1.setUsername("user");
        user1.setId("12");

        CalendarListResource calItem = new CalendarListResource();
        calItem.setCalendarId("teste@gmail.com");
        calItem.setId("1");

        CalendarResource calResource = new CalendarResource();
        calResource.setId("1");
        
        CalendarListResource calListResource = new CalendarListResource();
        calListResource.setId("1");

        String userId = user1.getId();
        System.out.println("USer ID: "+userId);
        String calendarId = calItem.getCalendarId();
        String calendarResourceId = calResource.getId();
        String calendarListResourceId = calListResource.getId();

        userRepository.insertCalendarListResourceByUserId(user1.getUserId(), calItem);
        
        //adiciona o calendarlistusaritem
        CalendarListResource findedCalItem = calendarDataController.addCalendarListResource(calListResource, userId);
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
        ArrayList<CalendarListResource> calListResource3 = calendarDataController.getCalendarList(userId);
        
        System.out.println(objectMapper.writeValueAsString(calListResource3));
        

        //deleta e vê se apagou
        calendarDataController.removeCalendar(calendarId, userId);

        CalendarListResource calListDelete = calendarDataController.getCalendarListResource(calendarId, userId);
        
        // System.out.println("DELETADO COM SUCESSO");

        
        userRepository.deleteById("12");
        // CalendarListResource.deleteById("1");
        // CalendarResource.deleteById("1");
        // CalendarListResource.deleteById("1");

        
    }
    @Test
    @WithMockUser
    public void testAddCalendarListResource(){
        User user = new User();
        String userId = "testUser";
        user.setId(userId);
        user.setGoogleId(userId);
        user.setUserId(userId);
        user.setDisplayName("Usuário de Test");
        userDataController.createUser(user);
        
        CalendarListResource calListR = new CalendarListResource();
        String calendarId = "calendario@teste.com";
        calListR.setId(calendarId);
        calListR.setCalendarId(calendarId);
        calListR.setOwner(user.getAsCalendarPerson());

        userRepository.addCalendarListResource(userId, calListR);
    }

    @Test
    @WithMockUser
    public void testAddCalendar() throws Exception {
        User user1 = new User();
        user1.setUsername("user");
        user1.setUserId("12");
        user1.setId("12");
        userDataController.deleteUser(user1.getId());
        userDataController.createUser(user1);
        String userId = user1.getId();
        String calendarId = "teste4@gmail.com";

        CalendarResource calR = new CalendarResource();
        calR.setCalendarId(calendarId);
        calR.setId(calendarId);
        calR.setOwner(user1.getAsCalendarPerson());
        calendarDataController.removeCalendar(calendarId, userId);
        calendarDataController.addCalendar(calR, userId);

        User user2 = userDataController.findUser(userId);
        System.out.println(objectMapper.writeValueAsString(user2));

    }

    @Test
    @WithMockUser
    public void testEventsDataController() throws Exception{
        User user1 = new User();
        user1.setUsername("user");
        user1.setId("12");
        String userId = user1.getId();
        String calendarId = "teste@gmail.com";

        CalendarResource calR = new CalendarResource();
        calR.setCalendarId(calendarId);
        calR.setId(calendarId);
        calendarDataController.removeCalendar(calendarId, userId);
        calendarDataController.addCalendar(calR, userId);


        EventsResource eventsResource = new EventsResource();
        eventsResource.setId("1");
        eventsResource.addCalendarId(calendarId);

        LocalDateTime start = LocalDateTime.now();
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        LocalDateTime end = start.plusDays(3);

        EventDate eventStart = new EventDate();
        EventDate eventEnd = new EventDate();
        
        eventStart.setDate(start.toLocalDate());
        eventStart.setDateTime(start);
        eventStart.setTimeZone(zoneId);
        eventsResource.setStart(eventStart);

        eventEnd.setDate(end.toLocalDate());
        eventEnd.setDateTime(end);
        eventEnd.setTimeZone(zoneId);
        eventsResource.setEnd(eventEnd);

        eventsDataController.createEvent(calendarId, eventsResource, userId);

        LocalDateTime endDate = eventsResource.getEnd().getDateTime();
        String eventId = eventsResource.getId();
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
        eventsDataController.removeEvent(eventId, calendarId, userId);

        ArrayList<String> eventsRemove = eventsResource.getCalendarIds();
        System.out.println(objectMapper.writeValueAsString(eventsRemove));
        System.out.println("Veja se o calendário com id " + calendarId +  " foi removido");


        //cancela o totalmente o evento
        eventsDataController.cancelEvent( eventId, calendarId, userId);

        EventsResource eventsRemoveVar = eventsDataController.getEvent(eventId, calendarId, userId);
        System.out.println(objectMapper.writeValueAsString(eventsRemoveVar));

        userRepository.deleteById("12");
    }

    // User Tests
     @Test
    @WithMockUser
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
        userDataController.deleteUser(userId);
        userDataController.createUser(user);
        User fetchedUser = userDataController.findUser(userId);
        System.out.println("User: "+objectMapper.writeValueAsString(fetchedUser));

        Optional<CalendarListResource> resp = userRepository.findCalendarListResourceByIdAndCalendarId(user.getUserId(), calR1.getCalendarId());
        if (resp.isEmpty()) {
            System.out.println("Resposta vazia");
        } else {
            System.out.println(objectMapper.writeValueAsString(resp.get()));
        }
    }

}
