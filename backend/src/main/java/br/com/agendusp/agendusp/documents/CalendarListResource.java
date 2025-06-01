package br.com.agendusp.agendusp.documents;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.agendusp.agendusp.calendar.Calendar;

@Document
public class CalendarListResource {

    String kind = "calendar#calendarListEntry";
    String etag;
    @Id
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
    // ArrayList<DefaultReminder> defaultReminders;
    // ArrayList<> notificationSettings;
    boolean primary;
    boolean deleted;
    // ConferenceProperties conferenceProperties;
    ArrayList<Calendar> calendars;

    public CalendarListResource() {
    }

    public CalendarListResource(String id, String summary, String description, String location, String timeZone) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.location = location;
        this.timeZone = timeZone;
    }

    public CalendarListResource(Gson gson) {
        JsonObject json = gson.toJsonTree(this).getAsJsonObject();
        this.id = json.get("id").getAsString();
        this.summary = json.get("summary").getAsString();
        this.description = json.get("description").getAsString();
        this.location = json.get("location").getAsString();
        this.timeZone = json.get("timeZone").getAsString();
        this.summaryOverride = json.get("summaryOverride").getAsString();
        this.colorId = json.get("colorId").getAsString();
        this.backgroundColor = json.get("backgroundColor").getAsString();
        this.foregroundColor = json.get("foregroundColor").getAsString();
        this.hidden = json.get("hidden").getAsBoolean();
        this.selected = json.get("selected").getAsBoolean();
        this.accessRole = json.get("accessRole").getAsString();
        this.primary = json.get("primary").getAsBoolean();
        this.deleted = json.get("deleted").getAsBoolean();
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

    public boolean getPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
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
