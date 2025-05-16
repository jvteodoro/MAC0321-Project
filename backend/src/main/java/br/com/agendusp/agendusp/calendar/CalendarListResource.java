package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;

import com.google.gson.Gson;

public class CalendarListResource {
    
    String kind = "calendar#calendarListEntry";
    String etag;
    String id;
    String summary;
    String description;
    String location;
    String timeZone;
    String summaryOverride;
    String colorId;
    String backgroundColor;
    String foregroundColor;
    boolean hidden;
    boolean selected;
    String accessRole;
    //ArrayList<DefaultReminder> defaultReminders;
    //ArrayList<> notificationSettings;
    String primary;
    boolean deleted;
    //ConferenceProperties conferenceProperties;
    ArrayList<Calendar> calendars;

    public CalendarListResource(String id, String summary, String description, String location, String timeZone) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.location = location;
        this.timeZone = timeZone;
    }
    public CalendarListResource(Gson gson) {
        this.id = gson.toJsonTree(this).getAsJsonObject().get("id").getAsString();
        this.summary = gson.toJsonTree(this).getAsJsonObject().get("summary").getAsString();
        this.description = gson.toJsonTree(this).getAsJsonObject().get("description").getAsString();
        this.location = gson.toJsonTree(this).getAsJsonObject().get("location").getAsString();
        this.timeZone = gson.toJsonTree(this).getAsJsonObject().get("timeZone").getAsString();
        this.summaryOverride = gson.toJsonTree(this).getAsJsonObject().get("summaryOverride").getAsString();
        this.colorId = gson.toJsonTree(this).getAsJsonObject().get("colorId").getAsString();
        this.backgroundColor = gson.toJsonTree(this).getAsJsonObject().get("backgroundColor").getAsString();
        this.foregroundColor = gson.toJsonTree(this).getAsJsonObject().get("foregroundColor").getAsString();
        this.hidden = gson.toJsonTree(this).getAsJsonObject().get("hidden").getAsBoolean();
        this.selected = gson.toJsonTree(this).getAsJsonObject().get("selected").getAsBoolean();
        this.accessRole = gson.toJsonTree(this).getAsJsonObject().get("accessRole").getAsString();
        this.primary = gson.toJsonTree(this).getAsJsonObject().get("primary").getAsString();
        this.deleted = gson.toJsonTree(this).getAsJsonObject().get("deleted").getAsBoolean();
    }


}
