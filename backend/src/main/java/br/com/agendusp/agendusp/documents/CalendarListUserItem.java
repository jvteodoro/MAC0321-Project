package br.com.agendusp.agendusp.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CalendarListUserItem {
    @Id
    String id;
    String calendarId;
    // String summaryOverride;
    String colorId;
    String backgroundColor;
    String foregroundColor;
    boolean hidden;
    boolean selected;
    String accessRole;

    public CalendarListUserItem() {}

    public CalendarListUserItem(String calendarId, String colorId, String backgroundColor, String foregroundColor, boolean hidden, boolean selected, String accessRole) {
        this.calendarId = calendarId;
        this.colorId = colorId;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.hidden = hidden;
        this.selected = selected;
        this.accessRole = accessRole;
    }
    

    public String getCalendarId() {
        return calendarId;
    }
    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
