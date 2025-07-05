package br.com.agendusp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import br.com.agendusp.agendusp.AgendUspApplication;
import br.com.agendusp.agendusp.MongoTestContainer;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventsController;
import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.documents.User;




@SpringBootTest(classes = AgendUspApplication.class)
public class LocalEventControllerTest extends MongoTestContainer {

    @Autowired
    MockMvc mockMvc; // MockMvc é usado para simular requisições HTTP
    @Autowired
    UserDataController userDataController; // Controlador de dados do usuário
    @Autowired
    EventsDataController eventsDataController; // Controlador de dados de eventos
    @Autowired
    EventsController eventsController;
    @Autowired
    Gson gson; // Gson é usado para manipulação de JSON
    @Autowired
    ObjectMapper objMapper;
    static String userId = "testUser"; // ID do usuário de teste
    static String userEmail = "test@gmail.com"; // Email do usuário de teste
    static String userName = "Usuário de Teste"; // Nome do usuário de teste
    static String eventId = "testEvent"; // ID do evento de teste
    static String eventName = "Evento de Teste"; // Nome do evento de teste
    public void setupDataBase() {
        // Configura o banco de dados com um usuário e um evento de teste
        User user = new User(userId, userEmail, userName);
        userDataController.createUser(user);
    }
    
    @Test
    @WithMockUser
    public void testListWindows() throws Exception {
        setupDataBase(); // Configura o banco de dados antes do teste
        String calendarId = "testCalendar"; // ID do calendário de teste
        String endDateTime = "2023-12-31T23:59:59"; // Data e hora de término para o teste

        mockMvc.perform(MockMvcRequestBuilders.get("/events/listWindows2")
                .param("calendarId", calendarId)
                .param("endDateTime", endDateTime))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Verifica se a resposta é 200 OK
                .andDo(MockMvcResultHandlers.print()); // Imprime os detalhes da requisição e resposta
    }

    @Test
    @WithMockUser
    public void testCreateEvent() throws Exception {
        setupDataBase(); // Configura o banco de dados antes do teste
        String calendarId = "testCalendar"; // ID do calendário de teste
        String eventDate = "2023-12-31T10:00:00"; // Data e hora do evento de teste

        String eventJson = "{ \"name\": \"" + eventName + "\", \"date\": \"" + eventDate + "\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/events/create")
                .param("calendarId", calendarId)
                .contentType("application/json")
                .content(eventJson))
                .andExpect(MockMvcResultMatchers.status().isCreated()) // Verifica se a resposta é 201 Created
                .andDo(MockMvcResultHandlers.print()); // Imprime os detalhes da requisição e resposta
    }

    @Test
    @WithMockUser   
    public void testDeleteEvent() throws Exception {
        setupDataBase(); // Configura o banco de dados antes do teste
        String calendarId = "testCalendar"; // ID do calendário de teste

        mockMvc.perform(MockMvcRequestBuilders.delete("/events/delete/{eventId}", eventId)
                .param("calendarId", calendarId))
                .andExpect(MockMvcResultMatchers.status().isNoContent()) // Verifica se a resposta é 204 No Content
                .andDo(MockMvcResultHandlers.print()); // Imprime os detalhes da requisição e resposta
    }

    













}
