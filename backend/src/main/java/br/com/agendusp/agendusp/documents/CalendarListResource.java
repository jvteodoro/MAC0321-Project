package br.com.agendusp.agendusp.documents;

import org.springframework.data.mongodb.core.mapping.Document;

import br.com.agendusp.agendusp.dataobjects.calendarObjects.CalendarPerson;

@Document(collection = "calendarLists") 
public class CalendarListResource { // objetos dessa classe serão salvos na coleção calendarLists do MongoDB
    String id;
    String calendarId;
    String kind = "calendar#calendarListEntry";
    // String etag;
    String summary;
    String description;
    String location;
    String timeZone;
    String colorId;
    String backgroundColor;
    String foregroundColor;
    boolean hidden;
    boolean selected;
    String accessRole;
    boolean primary;
    boolean deleted;
    CalendarPerson owner;

    public CalendarListResource() {
    }

    // public CalendarListResource(String calendarId, boolean primary, CalendarListUserItem calendarListUserItem, CalendarResource calendarResource) {
    //     this.calendarId = calendarId;
    //     this.primary = primary;

    //     this.summary = calendarResource.getSummary();
    //     this.description = calendarResource.getDescription();
    //     this.location = calendarResource.getLocation();
    //     this.timeZone = calendarResource.getTimeZone();
    //     this.deleted = calendarResource.isDeleted();
    //     this.owner = calendarResource.getOwner();
        
    //     this.colorId = calendarListUserItem.getColorId();
    //     this.backgroundColor = calendarListUserItem.getBackgroundColor();
    //     this.foregroundColor = calendarListUserItem.getForegroundColor();
    //     this.hidden = calendarListUserItem.isHidden();
    //     this.selected = calendarListUserItem.isSelected();
    //     this.accessRole = calendarListUserItem.getAccessRole();
    // }
    // Índice 0 é o calendário principal do usuário
    public CalendarResource extractCalendarResource() { // extrai um CalendarResource a partir de um CalendarListResource
        if (this.calendarId == null) {
            throw new IllegalArgumentException("Calendar ID cannot be null");
        }
        CalendarResource resource = new CalendarResource();
        resource.setCalendarId(this.calendarId);
        resource.setSummary(this.summary);
        resource.setDescription(this.description);
        resource.setLocation(this.location);
        resource.setTimeZone(this.timeZone);
        resource.setDeleted(this.deleted);
        resource.setOwner(this.owner);
        return resource;
    }

    public String getId() {
        return id;
    }
    public void setId(String id){
        this.calendarId = id;
        this.id = id;
    }

    public String getAcessRole() {
        return this.accessRole;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
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

    public CalendarPerson getOwner() {
        return owner;
    }

    public void setOwner(CalendarPerson owner) {
        this.owner = owner;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString(){
        return "CalendarListResource{" +
                "calendarId='" + calendarId + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\''+
                '}';
       
    }

}
