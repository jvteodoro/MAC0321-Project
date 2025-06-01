package br.com.agendusp.agendusp.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
    private final AbstractDataController dataController;
    @Autowired
    private final Gson gson;

    public LocalCalendarListController(AbstractDataController dataController, Gson gson) {
        this.dataController = dataController;
        this.gson = gson;
    }

    @DeleteMapping("/calendarList/delete")
    public ResponseEntity<Void> delete(@RequestParam String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        dataController.removeCalendar(calendarId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/calendarList/get")
    public String get(@RequestParam String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        return gson.toJson(dataController.getCalendar(calendarId, userId));
    }

    @PostMapping("/calendarList/insert")
    public String insert(@RequestBody CalendarListResource calendar,
            @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        dataController.addCalendar(calendar, userId);
        return gson.toJson(calendar);
    }

    @GetMapping("/calendarList/list")
    public String list(@AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
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
        return gson.toJson(dataController.updateCalendar(calendar.getId(), calendar, userId));
    }

    @PatchMapping("/calendarList/patch")
    public String patch(@RequestBody CalendarListResource calendar,
            @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        JsonObject body = new JsonObject(); // atualização parcial (dos atributos nao nulos)

        if (calendar.getDescription() != null) {
            body.addProperty("description", calendar.getDescription());
        }
        if (calendar.getLocation() != null) {
            body.addProperty("location", calendar.getLocation());
        }
        if (calendar.getSummary() != null) {
            body.addProperty("summary", calendar.getSummary());
        }
        if (calendar.getTimeZone() != null) {
            body.addProperty("timeZone", calendar.getTimeZone());
        }

        return gson.toJson(dataController.patchCalendar(calendar.getId(),
                gson.fromJson(body, CalendarListResource.class), userId));
    }
}
