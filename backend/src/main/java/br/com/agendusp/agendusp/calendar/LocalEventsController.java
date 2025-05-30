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

    /*
    Documentação: toJsonTree() transforma o objeto em uma árvore JSON manipulável
    (JsonElement), que pode ser convertida facilmente em RequestBody.
     */

    public String update(String calendarId, String eventId, EventsResource event) {
        JsonObject body = new JsonObject(); //atualização total

        body.add("start", gson.toJsonTree(event.getStart())); // usa tree para objetos aninhados
        body.add("end", gson.toJsonTree(event.getEnd()));
        body.addProperty("summary", event.getSummary());
        body.addProperty("location", event.getLocation());
        if (event.getAttendees() !=null) {
            body.add("attendees", gson.toJsonTree(event.getAttendees()));
        }
        
        return gson.toJson(dataController.updateEvent(eventId, body));
    }

    public String patch(String calendarId, String eventId, EventsResource event) {
        JsonObject body = new JsonObject(); //atualizacão parcial (dos atributos nao nulos)

        if (event.getStart() != null) {
            body.add("start", gson.toJsonTree(event.getStart()));
        }
        if (event.getEnd() != null) {
            body.add("end", gson.toJsonTree(event.getEnd()));
        }
        if (event.getSummary() != null) {
            body.addProperty("summary", event.getSummary());
        }
        if (event.getLocation() != null) {
            body.addProperty("location", event.getLocation());
        }
        if (event.getAttendees() != null) {
            body.add("attendees", gson.toJsonTree(event.getAttendees()));
        }
        
        return gson.toJson(dataController.patchEvent(eventId, body));
    }
}
