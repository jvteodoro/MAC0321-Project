package br.com.agendusp.agendusp.documents;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.context.SecurityContext;


import br.com.agendusp.agendusp.dataobjects.CalendarPerson;
import br.com.agendusp.agendusp.dataobjects.EventPool;


@Document(collection = "users")
public class User {//implements UserDetails {

    @Autowired
    private SecurityContext securityContext; // Para acessar o contexto de segurança

    @Id
    private String id; // ID gerada pelo MongoDB
    private String userId;
    private String DisplayName;
    
    private String googleId; // Guarda o 'sub' (estável) do Google
    private String email;
    private String name;
    private ArrayList<EventPool> eventPoolList;
    private ArrayList<EventPool> eventPoolNotifications;
    private ArrayList<CalendarListResource> calendarList; // Índice 0 é o calendário principal do usuário
    private CalendarPerson calendarPerson;

    public User() {
    }

    public User(String userId) {
        this.userId = userId;
    }

    public User(String googleId, String email, String name) {
        this.googleId = googleId;
        this.email = email;
        this.name = name;
        this.calendarPerson = new CalendarPerson(this.googleId, this.email, this.name);
    }

    public CalendarPerson getAsCalendarPerson() {
        return this.calendarPerson;
    }

    private void updateCalendarPerson() {
        this.calendarPerson = new CalendarPerson(this.googleId, this.email, this.name);
    }
    

    public ArrayList<EventPool> getEventPoolList() {
        return eventPoolList;
    }

    public void setEventPoolList(ArrayList<EventPool> eventPoolList) {
        this.eventPoolList = eventPoolList;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalendarList(ArrayList<CalendarListResource> calendarList) {
        this.calendarList = calendarList;
    }

    public ArrayList<EventPool> getEventPoolNotifications() {
        return eventPoolNotifications;
    }
    public void addEventPoolNotifications(EventPool eventPoolNotification){
        this.eventPoolNotifications.add(eventPoolNotification);
    }

    public void setEventPoolNotifications(ArrayList<EventPool> eventPoolNotifications) {
        this.eventPoolNotifications = eventPoolNotifications;
    }

    

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getId() {
        return this.id;
    }
    public String getUserId() {
        return this.userId;
    }

    public void setId(String id) {
        this.id = id;
        updateCalendarPerson();
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
        updateCalendarPerson();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        updateCalendarPerson();
    }

    public String getName() {
        return name;
    }

    public void setUsername(String name) {
        this.name = name;
        updateCalendarPerson();
    }

    public ArrayList<CalendarListResource> getCalendarList() {
        return calendarList;
    }
    public CalendarListResource addCalendarListResource(CalendarListResource calendarListResource) {
        if (this.calendarList == null) {
            this.calendarList = new ArrayList<>();
        }
        this.calendarList.add(calendarListResource);
        return calendarListResource;
    }



    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
    // }

    // @Override
    // public String getPassword() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
    // }

    // @Override
    // public String getUsername() {
    //     // TODO Auto-generated method stub

    // // public void runArray(){
    // //     //implementar
    // // }
        
    //     return this.name;   
    // }
}
