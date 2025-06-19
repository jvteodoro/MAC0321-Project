package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;


import br.com.agendusp.agendusp.CustomOAuth2User;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
public class LocalCalendarListController implements CalendarListController {

    @Autowired
    CalendarDataController calendarDataController;
    @Autowired
    UserDataController userDataController;
    @Autowired
    EventsDataController eventsDataController;
    @Autowired
    private Gson gson;

    public LocalCalendarListController() {}

    @DeleteMapping("/calendarList/delete")
    public ResponseEntity<Void> delete(@RequestParam String calendarId,
            @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        calendarDataController.removeCalendar(calendarId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/calendarList/getUser")
    public User getUser(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient){
        return userDataController.findUser(authorizedClient.getPrincipalName());
    }

    @GetMapping("/calendarList/get")
    public String get(@RequestParam String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        return gson.toJson(calendarDataController.getCalendarListResource(calendarId, userId));
    }


    @PostMapping("/calendarList/insert")
    public CalendarListResource insert(@RequestBody CalendarListResource calendar,
            @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        userDataController.insertCalendarListResource(userId, calendar);
        return calendar;
    }
    
    @GetMapping("/calendarList/list")
    public ArrayList<CalendarListResource> list(
        @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        String userName = authorizedClient.getPrincipalName();
        String userId = userDataController.findUser(userName).getGoogleId();
        System.out.println("USER ID:"+userId);
        return calendarDataController.getCalendarList(userId);
        // try {
        //     return gson.toJson(dataController.getCalendars(userId));
        // } catch (Exception e) {
        //     return gson.toJson("Error fetching calendars: " + e.getMessage());
        // }
    }

    @PutMapping("/calendarList/update")
    public CalendarListResource update(@RequestBody CalendarListResource calendar,
            @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        return calendarDataController.updateCalendarListResource(calendar.getId(), calendar, userId);
    }

    // @PatchMapping("/calendarList/patch")
    // public String patch(@RequestBody CalendarListResource calListResource,
    //         @AuthenticationPrincipal CustomOAuth2User customUser) {
    //     String userId = customUser.getUser().getId();
    //     JsonObject body = new JsonObject(); // atualização parcial (dos atributos nao nulos)
    //     if (calListResource.getDescription() != null) {
    //         body.addProperty("description", calListResource.getDescription());
    //     }
    //     if (calListResource.getLocation() != null) {
    //         body.addProperty("location", calListResource.getLocation());
    //     }
    //     if (calListResource.getSummary() != null) {
    //         body.addProperty("summary", calListResource.getSummary());
    //     }
    //     if (calListResource.getTimeZone() != null) {
    //         body.addProperty("timeZone", calListResource.getTimeZone());
    //     }
    //     if (calListResource.getAccessRole() != null) {
    //         body.addProperty("acessRole", calListResource.getAccessRole());
    //     }

    //     return gson.toJson(null);
    //     // return gson.toJson(dataController.patchCalendar(calListResource.getCalendarId(),
    //     //         gson.fromJson(body, CalendarListResource.class), userId));
    // }
}
