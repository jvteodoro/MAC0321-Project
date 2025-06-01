package br.com.agendusp.agendusp;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import br.com.agendusp.agendusp.CustomOAuth2User;
import br.com.agendusp.agendusp.documents.CalendarListResource;

@SpringBootTest
public class CalendarListControllerTest{
    public void TestDelete(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser){

    }

    // public String get(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser);

    // public String insert(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser);

    // public String list(@AuthenticationPrincipal CustomOAuth2User customUser);


    @Test
    public void testUpdate(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser) {
        CalendarListResource update = new CalendarListResource();
        update.setSummary("Bolo de Banana");
        update.setDescription("Use uma forma untada");
        update.setLocation("São Paulo, meu");
        update.setTimeZone("Espírito Santo");
        update.setAccessRole("writer");

        String responseJson = controller.update(update, userPrincipal);
        CalendarListResource resposta = gson.fromJson(responseJson, CalendarListResource.class);

        assertEquals("Bolo de Banana", resposta.getSummary());
        assertEquals("Use uma forma untada", resposta.getDescription());
        assertEquals("São Paulo, meu", resposta.getLocation());
        assertEquals("Espírito Santo", resposta.getTimeZone());
        assertEquals("writer", resposta.getAccessRole());
    }

    @Test
    public void testPatch(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser) {
        CalendarListResource patch = new CalendarListResource();
        patch.setSummary("Bolo de Maçã");

        String responseJson = controller.patch(patch, userPrincipal);
        CalendarListResource resposta = gson.fromJson(responseJson, CalendarListResource.class);

        assertEquals("Bolo de Maçã", resposta.getSummary()); //mudado
        assertEquals("Use uma forma untada", resposta.getDescription());
        assertEquals("São Paulo, meu", resposta.getLocation());
        assertEquals("Espírito Santo", resposta.getTimeZone());
        assertEquals("writer", resposta.getAccessRole());
    }
}

    /* 
    public static void main(String[] args) {
        CalendarServiceTest serviceTest = new CalendarServiceTest();

        CalendarListResource calendar = new CalendarListResource();
        calendar.setAccessRole("owner");
        calendar.setSummary("Resumo original");
        calendar.setDescription("Descrição original");
        calendar.setLocation("Local original");
        calendar.setTimeZone("UTC");

        CustomOAuth2User user = new CustomOAuth2User();
        user.setUserId("usuario1");

        serviceTest.TestPatch(calendar, user);
        serviceTest.TestUpdate(calendar, user);
    }*/


