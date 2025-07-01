package br.com.agendusp.agendusp;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class CalendarListControllerTest {

        @Autowired
        private MockMvc mockMvc;

        private Gson gson = new Gson();

        private CalendarListResource novoCalendario(String id) {
                CalendarListResource cal = new CalendarListResource();
                cal.setCalendarId(id);
                cal.setSummary("Resumo");
                cal.setDescription("Descricao");
                cal.setLocation("Localizacao");
                cal.setTimeZone("America/Sao_Paulo");
                cal.setAccessRole("owner");
                return cal;
        }

        @BeforeEach
        void setup() throws Exception {
                mockMvc.perform(delete("/calendarList/delete?calendarId=cal1"));
                mockMvc.perform(delete("/calendarList/delete?calendarId=cal2"));

                mockMvc.perform(post("/calendarList/insert")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(novoCalendario("cal1"))))
                                .andExpect(status().isOk());

                mockMvc.perform(post("/calendarList/insert")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(novoCalendario("cal2"))))
                                .andExpect(status().isOk());
        }

        @Test
        void testInsert() throws Exception {
                CalendarListResource calendar = novoCalendario("cal3");

                MvcResult mockResult = mockMvc.perform(post("/calendarList/insert")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(calendar)))
                                .andExpect(status().isOk())
                                .andReturn();

                String json = mockResult.getResponse().getContentAsString();
                CalendarListResource result = gson.fromJson(json, CalendarListResource.class);
                assertEquals(calendar.getCalendarId(), result.getCalendarId());
                assertEquals(calendar.getSummary(), result.getSummary());
        }

        @Test
        void testList() throws Exception {
                MvcResult result = mockMvc.perform(get("/calendarList/list"))
                                .andExpect(status().isOk())
                                .andReturn();

                String json = result.getResponse().getContentAsString();
                Type listType = new TypeToken<List<CalendarListResource>>() {
                }.getType();
                List<CalendarListResource> lista = gson.fromJson(json, listType);

                assertEquals(2, lista.size());

                assertTrue(lista.stream()
                                .anyMatch(c -> "cal1".equals(c.getCalendarId()) && "Resumo".equals(c.getSummary())));
                assertTrue(lista.stream()
                                .anyMatch(c -> "cal2".equals(c.getCalendarId()) && "Resumo".equals(c.getSummary())));
        }

        @Test
        void testGet() throws Exception {
                MvcResult result = mockMvc.perform(get("/calendarList/get?calendarId=cal1"))
                                .andExpect(status().isOk())
                                .andReturn();

                String json = result.getResponse().getContentAsString();
                CalendarListResource calendar = gson.fromJson(json, CalendarListResource.class);

                assertEquals("cal1", calendar.getCalendarId());
                assertEquals("Resumo", calendar.getSummary());
        }

        @Test
        void testDelete() throws Exception {
                mockMvc.perform(delete("/calendarList/delete?calendarId=cal1"))
                                .andExpect(status().isOk());

                MvcResult result = mockMvc.perform(get("/calendarList/list"))
                                .andExpect(status().isOk())
                                .andReturn();

                String json = result.getResponse().getContentAsString();
                Type listType = new TypeToken<List<CalendarListResource>>() {
                }.getType();
                List<CalendarListResource> lista = gson.fromJson(json, listType);

                assertEquals(1, lista.size());
                assertEquals("cal2", lista.get(0).getCalendarId());
        }

        @Test
        void testUpdate() throws Exception {
                CalendarListResource calendar = new CalendarListResource();
                calendar.setCalendarId("cal1");
                calendar.setSummary("Resumo atualizado");
                calendar.setDescription("Descrição atualizada");
                calendar.setLocation("Localização atualizada");
                calendar.setTimeZone("America/New_York");
                calendar.setAccessRole("writer");

                MvcResult mockResult = mockMvc.perform(put("/calendarList/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(calendar)))
                                .andExpect(status().isOk())
                                .andReturn();

                String json = mockResult.getResponse().getContentAsString();
                CalendarListResource result = gson.fromJson(json, CalendarListResource.class);

                assertEquals("cal1", result.getCalendarId());
                assertEquals("Resumo atualizado", result.getSummary());
                assertEquals("Descrição atualizada", result.getDescription());
                assertEquals("Localização atualizada", result.getLocation());
                assertEquals("America/New_York", result.getTimeZone());
                assertEquals("writer", result.getAccessRole());
        }

        @Test 
        void testPatch() throws Exception { 
                CalendarListResource patch = new CalendarListResource();
                patch.setCalendarId("cal1");
                patch.setSummary("Resumão modificado");

                MvcResult mockResult = mockMvc.perform(patch("/calendarList/patch")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gson.toJson(patch)))
                                .andExpect(status().isOk())
                                .andReturn();

                String json = mockResult.getResponse().getContentAsString();
                CalendarListResource result = gson.fromJson(json, CalendarListResource.class);

                assertEquals("cal1", result.getCalendarId());
                assertEquals("Resumão modificado", result.getSummary());
        }
}
