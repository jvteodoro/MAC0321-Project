package br.com.agendusp.agendusp.calendar;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    
    public String getKind() {
        return kind;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
    public String getEtag() {
        return etag;
    }
    public void setEtag(String etag) {
        this.etag = etag;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getTimeZone() {
        return timeZone;
    }
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
    public String getSummaryOverride() {
        return summaryOverride;
    }
    public void setSummaryOverride(String summaryOverride) {
        this.summaryOverride = summaryOverride;
    }
    public String getColorId() {
        return colorId;
    }
    public void setColorId(String colorId) {
        this.colorId = colorId;
    }
    public String getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public String getForegroundColor() {
        return foregroundColor;
    }
    public void setForegroundColor(String foregroundColor) {
        this.foregroundColor = foregroundColor;
    }
    public boolean isHidden() {
        return hidden;
    }
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public String getAccessRole() {
        return accessRole;
    }
    public void setAccessRole(String accessRole) {
        this.accessRole = accessRole;
    }
    public String getPrimary() {
        return primary;
    }
    public void setPrimary(String primary) {
        this.primary = primary;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public ArrayList<Calendar> getCalendars() {
        return calendars;
    }
    public void setCalendars(ArrayList<Calendar> calendars) {
        this.calendars = calendars;
    }
    

}
