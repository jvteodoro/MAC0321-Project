package br.com.agendusp.agendusp.calendar;

// import com.google.api.services.calendar.model.ConferenceProperties;
import com.google.gson.Gson;

public class GoogleCalendar implements Calendar{

    String kind = "calendar#calendar";
    String etag;
    String id;
    String summary;
    String description;
    String location;
    String timeZone;
    //ConferenceProperties conferenceProperties;

    public GoogleCalendar(String id, String summary, String description, String location, String timeZone) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.location = location;
        this.timeZone = timeZone;
    }
    public GoogleCalendar(Gson gson) {
        this.id = gson.toJsonTree(this).getAsJsonObject().get("id").getAsString();
        this.summary = gson.toJsonTree(this).getAsJsonObject().get("summary").getAsString();
        this.description = gson.toJsonTree(this).getAsJsonObject().get("description").getAsString();
        this.location = gson.toJsonTree(this).getAsJsonObject().get("location").getAsString();
        this.timeZone = gson.toJsonTree(this).getAsJsonObject().get("timeZone").getAsString();

    }

    @Override
    public org.springframework.http.HttpStatusCode clear() {
        return null;
    }

    public String getId() {
        return id;
    }

}
