package br.com.agendusp.agendusp.controller.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.dataobjects.UserInfo;
import br.com.agendusp.agendusp.dataobjects.calendarObjects.CalendarListList;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.User;

@RestController
public class GoogleDataToLocalData {
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
        user.setUserId(inf.getId());
        user.setUsername(inf.getName());
        userDataController.findUserOrCreate(user);
        User addedUser = userDataController.findUserByName(user.getName());
        return addedUser;
    }

    @GetMapping("/google/cloudToLocal")
    public void cloudToLocal(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient)
            throws Exception {
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
                System.out.println("Skipping calendar (not in user list): " + calListResource.getCalendarId());
                continue;
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
}
