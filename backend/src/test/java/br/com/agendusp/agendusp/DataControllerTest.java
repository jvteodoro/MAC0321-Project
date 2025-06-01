package br.com.agendusp.agendusp;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import br.com.agendusp.agendusp.CustomOAuth2User;
import br.com.agendusp.agendusp.documents.CalendarListResource;



@SpringBootTest
public class DataControllerTest extends MongoTestContainer {

    private DataController dataController;


    public void testGet(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser) {
        // Implement the logic to get a calendar by ID
    }
    public void testInsert(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser) {
        // Implement the logic to insert a new calendar
    } 
    public void testList(@AuthenticationPrincipal CustomOAuth2User customUser) {
        // Implement the logic to list all calendars
    }
    public void testDelete(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser) {
        // Implement the logic to delete a calendar by ID
    }      
   

    @Test
    public void testUpdate() {
        CalendarListResource update = new CalendarListResource();
        update.setSummary("Bolo de Banana");
        update.setDescription("Use uma forma untada");
        update.setLocation("São Paulo, meu");
        update.setTimeZone("Espírito Santo");
        update.setAccessRole("writer");

        
        assertEquals("Bolo de Banana", update.getSummary());
        assertEquals("Use uma forma untada", update.getDescription());
        assertEquals("São Paulo, meu", update.getLocation());
        assertEquals("Espírito Santo", update.getTimeZone());
        assertEquals("writer", update.getAccessRole());
    }

    @Test
    public void testPatch() {
        CalendarListResource patch = new CalendarListResource();
        patch.setSummary("Bolo de Banana");

        assertEquals("Bolo de Banana", patched.getSummary());
    }
}


