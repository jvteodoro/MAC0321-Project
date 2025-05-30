package br.com.agendusp.agendusp.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
public class LocalEventsController implements EventsController {

    @Autowired
    private final AbstractDataController dataController;
    @Autowired
    private final Gson gson;

    public LocalEventsController(AbstractDataController dataController, Gson gson) {
        this.dataController = dataController;
        this.gson = gson;
    }

    public HttpStatusCode delete(String calendarId, String eventId) {
        // IMPLEMENTAR
        return HttpStatusCode.valueOf(204); // No Content
    }

    public String get(String calendarId, String eventId) {
        // IMPLEMENTAR
        return gson.toJson(null);
    }

    public String insert(String calendarId, EventsResource event) {
        // IMPLEMENTAR
        return gson.toJson(null);
    }

    public String list(String calendarId) {
        // IMPLEMENTAR
        return gson.toJson(null);
    }

    public String patch(String calendarId, String eventId, EventsResource event) {
        // IMPLEMENTAR
        return gson.toJson(null);
    }

    public String update(String calendarId, String eventId, EventsResource event) {
        // IMPLEMENTAR
        return gson.toJson(null);
    }
}
