package br.com.agendusp.agendusp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.agendusp.agendusp.documents.CalendarListResource;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DataControllerOperationsMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

     private String userID = "agendusp";
     private String calendarID = "primeiros";

    private CalendarListResource createCalendar() {
        CalendarListResource calendar = new CalendarListResource();
        calendar.setCalendarId(calendarID);
        calendar.setSummary("Bolo de Chocolate");
        calendar.setDescription("Caldinha de brigadeiro");
        calendar.setLocation("China");
        calendar.setTimeZone("Greenwich");
        calendar.setAccessRole("writer");
        return calendar;
    }

    @BeforeEach
    public void setup() throws Exception {
        CalendarListResource calendar = createCalendar();
        mockMvc.perform(post("/data/calendar")
                .contentType(MediaType.APPLICATION_JSON)
                .header("userId", userID)
                .content(gson.toJson(calendar)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGet() throws Exception {
        String responseJson = mockMvc.perform(get("/data/calendar/{id}", calendarID)
                .header("userId", userID))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CalendarListResource fetched = gson.fromJson(responseJson, CalendarListResource.class);
        assertNotNull(fetched);
        assertEquals("Bolo de Chocolate", fetched.getSummary());
        assertEquals("Caldinha de brigadeiro", fetched.getDescription());
        assertEquals("China", fetched.getLocation());
        assertEquals("Greenwich", fetched.getTimeZone());
        assertEquals("writer", fetched.getAccessRole());
    }

    @Test
    public void testInsert() throws Exception {
        CalendarListResource calendar = new CalendarListResource();
        calendar.setCalendarId("novoId");
        calendar.setSummary("Novo");
        calendar.setAccessRole("reader");

        mockMvc.perform(post("/data/calendar")
                .contentType(MediaType.APPLICATION_JSON)
                .header("userId", userID)
                .content(gson.toJson(calendar)))
                .andExpect(status().isOk());

        String listJson = mockMvc.perform(get("/data/calendars")
                .header("userId", userID))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CalendarListResource[] calendars = gson.fromJson(listJson, CalendarListResource[].class);
        assertTrue(java.util.Arrays.stream(calendars).anyMatch(c -> "novoId".equals(c.getCalendarId())));
    }

    @Test
    public void testList() throws Exception {
        String listJson = mockMvc.perform(get("/data/calendars")
                .header("userId", userID))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CalendarListResource[] calendars = gson.fromJson(listJson, CalendarListResource[].class);
        assertTrue(calendars.length > 0);
        assertTrue(java.util.Arrays.stream(calendars).anyMatch(c -> calendarID.equals(c.getCalendarId())));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/data/calendar/{id}", calendarID)
                .header("userId", userID))
                .andExpect(status().isOk());

        String listJson = mockMvc.perform(get("/data/calendars")
                .header("userId", userID))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CalendarListResource[] calendars = gson.fromJson(listJson, CalendarListResource[].class);
        assertFalse(java.util.Arrays.stream(calendars).anyMatch(c -> calendarID.equals(c.getCalendarId())));
    }

    @Test
    public void testUpdate() throws Exception {
        CalendarListResource calendar = createCalendar();
        calendar.setSummary("Bolo de Cenoura");
        calendar.setDescription("Unte a forma");
        calendar.setLocation("São Paulo, meu");
        calendar.setTimeZone("Espírito Santo");
        calendar.setAccessRole("owner");

        String responseJson = mockMvc.perform(put("/data/calendar/{id}", calendarID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("userId", userID)
                .content(gson.toJson(calendar)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CalendarListResource updated = gson.fromJson(responseJson, CalendarListResource.class);
        assertEquals("Bolo de Cenoura", updated.getSummary());
        assertEquals("Unte a forma", updated.getDescription());
        assertEquals("São Paulo, meu", updated.getLocation());
        assertEquals("Espírito Santo", updated.getTimeZone());
        assertEquals("owner", updated.getAccessRole());
    }

    @Test
    public void testPatch() throws Exception {
        CalendarListResource patch = createCalendar();
        patch.setSummary("Bolo de Banana");
        patch.setDescription(null);
        patch.setLocation(null);
        patch.setTimeZone(null);
        patch.setAccessRole(null);

        String responseJson = mockMvc.perform(patch("/data/calendar/{id}", calendarID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("userId", userID)
                .content(gson.toJson(patch)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CalendarListResource patched = gson.fromJson(responseJson, CalendarListResource.class);
        assertEquals("Bolo de Banana", patched.getSummary());
        assertEquals("Caldinha de brigadeiro", patched.getDescription());
        assertEquals("China", patched.getLocation());
        assertEquals("Greenwich", patched.getTimeZone());
        assertEquals("writer", patched.getAccessRole());
    }
}
