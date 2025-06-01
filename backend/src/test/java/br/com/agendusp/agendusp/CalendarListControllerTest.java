package br.com.agendusp.agendusp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.agendusp.agendusp.calendar.LocalCalendarListController;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.CustomOAuth2User;

import com.google.gson.Gson;

@SpringBootTest
public class CalendarListControllerTest {

    @Autowired
    private LocalCalendarListController controller;

    @Autowired
    private Gson gson;
    private CustomOAuth2User user1;
    private CalendarListResource calendar1;

    @BeforeEach
    public void setup() {
        user1 = new CustomOAuth2User();
        user1.setUserId("agendusp");

        calendar1 = new CalendarListResource();
        calendar1.setId("calendar1");
        calendar1.setAccessRole("owner");
        calendar1.setSummary("Bolo de Chocolate");
        calendar1.setDescription("Caldinha de brigadeiro");
        calendar1.setLocation("China");
        calendar1.setTimeZone("Greenwich");

        controller.insert(calendar1, user1);
    }

    @Test
    public void testDelete() {
        CalendarListResource calendarToDelete = new CalendarListResource();
        calendarToDelete.setId("NovoCalendario");
        controller.insert(calendarToDelete, user1);

        controller.delete("NovoCalendario", user1);

        String json = controller.get("NovoCalendario", user1);
        assertEquals("null", json);
    }

    @Test
    public void testGet() {
        String responseJson = controller.get(calendar1.getId(), user1);
        CalendarListResource responseCalendar = gson.fromJson(responseJson, CalendarListResource.class);

        assertNotNull(responseCalendar);
        assertEquals("Bolo de Chocolate", responseCalendar.getSummary());
        assertEquals("Caldinha de brigadeiro", responseCalendar.getDescription());
        assertEquals("China", responseCalendar.getLocation());
        assertEquals("Greenwich", responseCalendar.getTimeZone());
        assertEquals("owner", responseCalendar.getAccessRole());
    }

    @Test
    public void testInsert() {
        CalendarListResource calendar2 = new CalendarListResource();
        calendar2.setId("calendar2");
        calendar2.setAccessRole("writer");
        calendar2.setSummary("Bolo de maçã");
        calendar2.setDescription("Deixe esfriar antres de desenformar");
        calendar2.setLocation("São Paulo, meu");
        calendar2.setTimeZone("America/Sao_Paulo");

        String responseJson = controller.insert(calendar2, user1);
        CalendarListResource inserted = gson.fromJson(responseJson, CalendarListResource.class);

        assertEquals("writer", inserted.getAccessRole());
        assertEquals("Bolo de maçã", inserted.getSummary());
        assertEquals("Deixe esfriar antres de desenformar", inserted.getDescription());
        assertEquals("São Paulo, meu", inserted.getLocation());
        assertEquals("America/Sao_Paulo", inserted.getTimeZone());
        assertEquals("writer", inserted.getAccessRole());
    }

    @Test 
    public void testList() { //esse aqui eu não tenho certeza
        String jsonList = controller.list(user1);
        assertNotNull(jsonList);
        assert (jsonList.contains(calendar1.getId()));
    }

    @Test
    public void testUpdate() {
        CalendarListResource update = new CalendarListResource();
        update.setId(calendar1.getId());
        update.setSummary("Bolo de Banana");
        update.setDescription("Use uma forma untada");
        update.setLocation("São Paulo, meu");
        update.setTimeZone("Espírito Santo");
        update.setAccessRole("writer");

        String responseJson = controller.update(update, user1);
        CalendarListResource resposta = gson.fromJson(responseJson, CalendarListResource.class);

        assertEquals("Bolo de Banana", resposta.getSummary());
        assertEquals("Use uma forma untada", resposta.getDescription());
        assertEquals("São Paulo, meu", resposta.getLocation());
        assertEquals("Espírito Santo", resposta.getTimeZone());
        assertEquals("writer", resposta.getAccessRole());
    }

    @Test
    public void testPatch() { //se for testar o patch tem que verificar se testou update antes.
        CalendarListResource patch = new CalendarListResource();
        patch.setId(calendar1.getId());
        patch.setSummary("Bolo de Maçã");

        String responseJson = controller.patch(patch, user1);
        CalendarListResource resposta = gson.fromJson(responseJson, CalendarListResource.class);

        assertEquals("Bolo de Maçã", resposta.getSummary());
        assertEquals("Caldinha de brigadeiro", resposta.getDescription());
        assertEquals("China", resposta.getLocation());
        assertEquals("Greenwich", resposta.getTimeZone());
        assertEquals("owner", resposta.getAccessRole());
    }
}

