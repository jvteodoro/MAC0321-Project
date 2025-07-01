package br.com.agendusp;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.agendusp.agendusp.AgendUspApplication;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.controller.FormsController;
import br.com.agendusp.agendusp.controller.HomeController;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.calendarControllers.CalendarDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.dataobjects.DateTimeInterval;
import br.com.agendusp.agendusp.dataobjects.eventObjects.EventDate;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.CalendarRepository;
import br.com.agendusp.agendusp.repositories.EventsRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;
import br.com.agendusp.agendusp.services.PromptBuilder;
import static org.mockito.Mockito.mock;
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
    @Autowired
    FormsController formsController;
    

    
    @Test
    public void testHome() throws Exception{
        // TODO pois nunca é usado
        ResultActions result = mockMvc.perform(get("/")).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void testTeste() throws Exception{
        mockMvc.perform(get("/secured")).andDo(MockMvcResultHandlers.print());
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
        // TODO pois nunca é usado
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
        
        eventStart.setDateFromObject(start.toLocalDate());
        eventStart.setDateTimeFromObject(start);
        eventStart.setTimeZone(zoneId);
        eventsResource.setStart(eventStart);

        eventEnd.setDateFromObject(end.toLocalDate());
        eventEnd.setDateTimeFromObject(end);
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

    @Test
    @WithMockUser
    public void testEventResourceFreeTime() throws Exception{
        EventsResource mockEvent = new EventsResource();
        EventDate start = new EventDate();
        EventDate end = new EventDate();
        ArrayList<DateTimeInterval> freeTimeVec = new ArrayList<>();
        DateTimeInterval initFreeTime = new DateTimeInterval();
        DateTimeInterval freeTime2 = new DateTimeInterval();

        initFreeTime.setStart(LocalDateTime.of(2025, 6, 15, 10, 43, 0));
        initFreeTime.setEnd(LocalDateTime.of(2025, 6, 18, 10, 43, 0));
        freeTime2.setStart(LocalDateTime.of(2025, 6, 20, 8, 34, 0));
        freeTime2.setEnd(LocalDateTime.of(2025, 6, 25, 10, 22, 0));

        freeTimeVec.add(initFreeTime);
        freeTimeVec.add(freeTime2);

        start.setDateTime("2025-06-19T20:23:12.0000Z");
        end.setDateTime("2025-06-23T20:23:12.0000Z");
        
        mockEvent.setStart(start);
        mockEvent.setEnd(end);

        ArrayList<DateTimeInterval> freeTimeVecNew = mockEvent.freeTime(freeTimeVec);

        //System.out.println(freeTimeVec.toString());
        for (DateTimeInterval interval: freeTimeVecNew){
            try{System.out.println(objectMapper.writeValueAsString(interval));}
            catch (Exception e){}
        }
        MvcResult result = mockMvc.perform(post("/pool/create?start="+initFreeTime.getStart().toString()
        +"&end="+initFreeTime.getEnd().toString()).content(objectMapper.writeValueAsString(mockEvent))).andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    // Testes EventPoolDataController
    @Test
    @WithMockUser
    public void getAllEventPoolsTest(){
        
    }

    // Teste AI
    @Test
    @WithMockUser
    public void testPrompBuilder() throws Exception{
        PromptBuilder promptBuilder = new PromptBuilder();
        
        String calendarId;
        String dataInicial = "2025-06-21T00:00:00Z";
        // TODO pois nunca é usado
        EventsResource mockEvent = new EventsResource();
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
        OAuth2AuthorizedClient authorizedClient = mock(OAuth2AuthorizedClient.class);


        calendarId = null;
        String promptCalendarioDia = promptBuilder.getPromptParaInformeDia(authorizedClient, dataInicial, calendarId);
        if (promptCalendarioDia != null){
            System.out.println("Prompt Dia: "+promptCalendarioDia);
        }


        calendarId = "calR1";
        String promptTotalDia = promptBuilder.getPromptParaInformeDia(authorizedClient, dataInicial, calendarId);
        if (promptTotalDia != null){
            System.out.println("Prompt Dia: "+promptTotalDia);
        }

        // calendarId = null;
        // String promptCalendarioSemana = promptBuilder.getPromptParaInformeSemana(authorizedClient, dataInicial, calendarId);
        // if (promptCalendarioSemana != null){
        //     System.out.println("Prompt Semana: "+promptCalendarioSemana);
        // }


        // calendarId = "calR1";
        // String promptTotalSemana = promptBuilder.getPromptParaInformeSemana(authorizedClient, dataInicial, calendarId);
        // if (promptTotalSemana != null){
        //     System.out.println("Prompt Semana: "+promptTotalSemana);
        // }
    
        // TODO pois nunca é usado
        ResultActions result = mockMvc.perform(get("/prompt/semana")).andDo(MockMvcResultHandlers.print());
        // ResultActions result = mockMvc.perform(get("/prompt/dia")).andDo(MockMvcResultHandlers.print());
    }
        

        
}
