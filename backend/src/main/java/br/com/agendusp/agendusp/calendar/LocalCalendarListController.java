package br.com.agendusp.agendusp.calendar;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.json.Json;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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
    

    @GetMapping("/calendarList")
    public CalendarListResource get(String calendarId){
        return new CalendarListResource(gson);
    }
    
    @PostMapping("/calendarList/insert")
    public Json insert(@RequestBody CalendarListResource calendar, OAuth2AuthorizedClient authorizedClient){
        dataController.addCalendar(calendar);
        return calendar;
    }
    public Json list(OAuth2AuthorizedClient authorizedClient){
        return dataController.getCalendars();
    } 
    public Json patch(CalendarListResource calendar){
        return dataController.patchCalendar(calendar.getId(), calendar);
    }
    public Json update(CalendarListResource calendar){
        return dataController.updateCalendar(calendar.getId(), calendar);
    }
    public Json watch(WatchRequest watchRequest){
        WatchResponse watchResponse = new WatchResponse(gson);
        watchResponse.setId("watch-id");
        watchResponse.setResourceId("resource-id");
        watchResponse.setResourceUri("https://example.com/watch");
        watchResponse.setExpiration(1728000000L); // 20 dias em milissegundos
        watchResponse.setKind("api#watchResponse");
        return watchResponse;
    }

}
