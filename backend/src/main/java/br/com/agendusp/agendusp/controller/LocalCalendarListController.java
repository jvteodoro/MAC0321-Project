package br.com.agendusp.agendusp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nimbusds.jose.proc.SecurityContext;

import br.com.agendusp.agendusp.CustomOAuth2User;
import br.com.agendusp.agendusp.documents.CalendarListResource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
public class LocalCalendarListController implements CalendarListController {

    @Autowired
    private AbstractDataController dataController;
    @Autowired
    private Gson gson;

    public LocalCalendarListController() {}

    @DeleteMapping("/calendarList/delete")
    public ResponseEntity<Void> delete(@RequestParam String calendarId,
            @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        dataController.removeCalendar(calendarId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/calendarList/get")
    public String get(@RequestParam String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        return gson.toJson(dataController.getCalendarListResource(calendarId, userId));
    }

    @PostMapping("/calendarList/insert")
    public String insert(@RequestBody CalendarListResource calendar,
            @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        dataController.addCalendarListUserItem(calendar.extractCalendarResource(), userId);
        return gson.toJson(calendar);
    }
    
    @GetMapping("/calendarList/list")
    public String list(@Autowired SecurityContextHolder securityContextHolder) {
        String userId = securityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        System.out.println("USER ID:"+userId);
        try {
            return gson.toJson(dataController.getCalendars(userId));
        } catch (Exception e) {
            return gson.toJson("Error fetching calendars: " + e.getMessage());
        }
    }

    @PutMapping("/calendarList/update")
    public String update(@RequestBody CalendarListResource calendar,
            @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        return gson.toJson(dataController.updateCalendar(calendar.getCalendarId(), calendar, userId));
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
