package br.com.agendusp.agendusp.controller.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.dataobjects.UserInfo;
import br.com.agendusp.agendusp.dataobjects.calendarObjects.CalendarListList;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.EventsResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GoogleCloudAndLocalController {
    @Autowired
    RestClient restClient;
    @Autowired
    UserDataController userDataController;
    @Autowired
    GoogleCalendarListController gCalendarListController;
    @Autowired
    GoogleEventsController gEventsController;
    @Autowired
    GoogleCalendarsController gCalendarsController;
    @Autowired
    OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    EventsDataController eventsDataController;
    @Autowired
    ObjectMapper objMapper;

    @GetMapping("/google/userInfo")
    public User getUserInfo(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {

        System.out.println("Running userinfo");

        ResponseEntity<UserInfo> response = restClient.get()
                .uri("https://www.googleapis.com/oauth2/v2/userinfo")
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                .retrieve().toEntity(UserInfo.class);
        UserInfo inf = response.getBody();
        User user = new User();
        user.setEmail(inf.getEmail());
        user.setGoogleId(inf.getId());
        user.setId(inf.getId());
        user.setUsername(inf.getName());
        userDataController.findUserOrCreate(user);
        User addedUser = userDataController.findUserByName(user.getName());
        return addedUser;
    }

    @GetMapping("/google/cloudToLocal")
    public void cloudToLocal(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) throws Exception {
        this.getUserInfo(authorizedClient);
        CalendarListList calList = gCalendarListController.list(authorizedClient);
        String userId = authorizedClient.getPrincipalName();
        for (CalendarListResource calListResource : calList.getItems()) {
            boolean isWritable = calListResource.getAccessRole() != null &&
                    (calListResource.getAccessRole().equalsIgnoreCase("owner") ||
                            calListResource.getAccessRole().equalsIgnoreCase("writer"));
            boolean userHas = userDataController.userHasCalendar(userId, calListResource.getCalendarId());
            if (!isWritable) {
                System.out.println("Skipping calendar: " + calListResource.getCalendarId() +
                        " (accessRole=" + calListResource.getAccessRole() + ")");
                continue;
            }
            if (!userHas) {
                // Adiciona calendário no usuário se não estiver presente e for possível escrever nele
                try {
                    userDataController.insertCalendarListResource(userId, calListResource);
                    System.out.println("Added calendar to user: " + calListResource.getCalendarId());
                } catch (Exception e) {
                    System.out.println("Error adding calendar to user: " + calListResource.getCalendarId() + " - " + e.getMessage());
                }
            }
            try {
                System.out.println(objMapper.writeValueAsString(calListResource));
                try {
                    gEventsController.list(calListResource.getId(), authorizedClient);
                } catch (Exception e) {
                    System.out.println("Skipping events for calendar: " + calListResource.getCalendarId()
                            + " due to error: " + e.getMessage());
                }
                try {
                    gCalendarsController.get(calListResource.getId(), authorizedClient);
                } catch (IllegalArgumentException ex) {
                    System.out.println("Calendar already exists: " + calListResource.getCalendarId());
                } catch (Exception ex) {
                    System.out.println("Skipping calendar: " + calListResource.getCalendarId() + " due to error: "
                            + ex.getMessage());
                }
            } catch (Exception outer) {
                System.out.println(
                        "Error processing calendar: " + calListResource.getCalendarId() + " - " + outer.getMessage());
            }
        }
    }

    @PostMapping("/google/localToCloud")
    public Map<String, Object> localToCloud(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) throws Exception {
        // 1. Obter usuário autenticado
        User user = this.getUserInfo(authorizedClient);
        ArrayList<CalendarListResource> localCalendars = user.getCalendarList();

        // Relatório de sincronização
        Map<String, Object> syncReport = new HashMap<>();
        ArrayList<String> syncedCalendars = new ArrayList<>();
        //ArrayList<String> syncedEvents = new ArrayList<>();

        // 2. Para cada calendário local, sincronizar com Google
        for (CalendarListResource calendar : localCalendars) {
            try {
                // Tenta obter o calendário na nuvem
                CalendarListResource checkIfGoogleCalendarExist = gCalendarListController.get(calendar.getId(), authorizedClient);
                gCalendarListController.update(calendar);
                // Se não lançar exceção, já existe na nuvem
            } catch (Exception e) {
                // Não existe na nuvem, criar
                gCalendarListController.insert(calendar, authorizedClient);
            }
            syncedCalendars.add(calendar.getCalendarId());

            // 3. Para cada evento local do calendário, sincronizar
            /*ArrayList<EventsResource> localEvents = eventsDataController.getEvents(calendar.getCalendarId(), user.getId());
            for (EventsResource event : localEvents) {
                try {
                    // Tenta obter o evento na nuvem
                    EventsResource checkIfGoogleEventExist = gEventsController.get(calendar.getId(), event.getId(), authorizedClient);
                    gEventsController.update(event, calendar.getId(), authorizedClient);
                    // Se não lançar exceção, já existe
                } catch (Exception e) {
                    // Não existe, criar
                    restClient.post()
                        .uri("https://www.googleapis.com/calendar/v3/calendars/" + calendar.getCalendarId() + "/events")
                        .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                        .body(event)
                        .retrieve().toEntity(EventsResource.class).getBody();
                }
                syncedEvents.add(event.getId());
            }*/
        }
        syncReport.put("calendars", syncedCalendars);
        /*syncReport.put("events", syncedEvents);*/
        return syncReport;
    }
    
}
