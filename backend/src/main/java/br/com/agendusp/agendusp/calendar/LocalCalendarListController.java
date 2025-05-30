package br.com.agendusp.agendusp.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
public class LocalCalendarListController implements CalendarListController {

    @Autowired
    private final  AbstractDataController dataController;
    @Autowired
    private final Gson gson;

    public LocalCalendarListController(AbstractDataController dataController, Gson gson){
        this.dataController = dataController;
        this.gson = gson;
    }

    public ResponseEntity<Void> delete(String calendarId, OAuth2AuthorizedClient authorizedClient){
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
    }
    

    @GetMapping("/calendarList/get")
    public String get(String calendarId, OAuth2AuthorizedClient authorizedClient){
        // IMPLEMENTAR
        return gson.toJson(null);
    }
    
    @PostMapping("/calendarList/insert")
    public String insert(@RequestBody CalendarListResource calendar, OAuth2AuthorizedClient authorizedClient){
        dataController.addCalendar(calendar);
        return gson.toJson(calendar);
    }

    @GetMapping("/calendarList/list")
    public String list(OAuth2AuthorizedClient authorizedClient){
        return gson.toJson(dataController.getCalendars());
    }  

    @PutMapping("/calendarList/update")
    public String update(@RequestBody CalendarListResource calendar, OAuth2AuthorizedClient authorizedClient) {
        return gson.toJson(dataController.updateCalendar(calendar.getId(), calendar));
    }

    @PatchMapping("/calendarList/patch")
    public String patch(@RequestBody CalendarListResource calendar, OAuth2AuthorizedClient authorizedClient) {
        JsonObject body = new JsonObject(); //atualização parcial (dos atributos nao nulos)
        
        if (calendar.getDescription() !=null) {
            body.addProperty("description", calendar.getDescription());
        }
        if (calendar.getLocation() !=null) {
            body.addProperty("location", calendar.getLocation());
        }
        if (calendar.getSummary() != null) {
            body.addProperty("summary", calendar.getSummary());
        }
        if (calendar.getTimeZone() !=null) {
            body.addProperty("timeZone", calendar.getTimeZone());
        }

        return gson.toJson(dataController.patchCalendar(calendar.getId(), gson.fromJson(body, CalendarListResource.class)));
    }
}
