package br.com.agendusp.agendusp.documents;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;

import br.com.agendusp.agendusp.calendar.Calendar;
import br.com.agendusp.agendusp.calendar.CalendarPerson;

public class CalendarResource {
    
    String kind = "calendar#calendar";
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
    CalendarPerson owner;
    ArrayList<UserCalendarRelation> userCalendarRelation;

    // ArrayList<DefaultReminder> defaultReminders;
    // ArrayList<> notificationSettings;
    boolean primary;
    boolean deleted;

    public ArrayList<UserCalendarRelation> getUserCalendarListRelation(){
        return userCalendarRelation;
    }

    public UserCalendarRelation addUserCalendarRelation(UserCalendarRelation userCalendarRelation) {
        if (this.userCalendarRelation == null) {
            this.userCalendarRelation = new ArrayList<UserCalendarRelation>();
        }
        this.userCalendarRelation.add(userCalendarRelation);
        return userCalendarRelation;
    }
    public String getId(){
        return this.id;
    }
}
