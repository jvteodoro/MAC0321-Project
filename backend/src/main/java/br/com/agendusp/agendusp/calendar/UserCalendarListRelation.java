package br.com.agendusp.agendusp.calendar;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Document
public class UserCalendarListRelation {
    @Id
    public String userId;
    public String calendarId;
    public String accessRole;

    UserCalendarListRelation(String userId, String calendarId, String accessRole) {
        this.userId = userId;
        this.calendarId = calendarId;
        this.accessRole = accessRole;
    }

    // public UserCalendarListRelation(Gson gson) {
    //     JsonObject json = gson.toJsonTree(this).getAsJsonObject();
    //     this.userId = json.get("userId").getAsString();
    //     this.calendarId = json.get("calendarId").getAsString();
    //     this.accessRole = json.get("accessRole").getAsString();
    // }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getAccessRole() {
        return accessRole;
    }

    public void setAccessRole(String accessRole) {
        this.accessRole = accessRole;
    }
}
