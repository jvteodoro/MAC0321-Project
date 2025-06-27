package br.com.agendusp.agendusp.documents;

import java.util.ArrayList;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.agendusp.agendusp.dataobjects.calendarObjects.CalendarPerson;
import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPool;


@Document(collection = "users")
public class User { // objetos dessa classe serão salvos na coleção users do MongoDB

    @Id
    private String id; // ID gerada pelo MongoDB
    private String userId;
    private String DisplayName;
    
    private String googleId; // Guarda o 'sub' (estável) do Google
    private String email;
    private String name;
    private ArrayList<String> eventPoolList;
    private ArrayList<EventPool> eventPoolNotifications;
    private ArrayList<CalendarListResource> calendarList; // Índice 0 é o calendário principal do usuário
    private CalendarPerson calendarPerson;

    public User() {
    }

    public User(String userId) { // Construtor para criar um usuário com um ID específico
        this.id = null; // ID será gerado pelo MongoDB
        this.googleId = null; // Google ID pode ser nulo se o usuário não se autenticou com o Google
        this.userId = userId;
    }

    public User(String googleId, String email, String name) { // Construtor para criar um usuário com Google ID, email e nome
        this.id = null; // ID será gerado pelo MongoDB
        this.googleId = googleId;
        this.email = email;
        this.name = name;
        this.calendarPerson = new CalendarPerson(this.id, this.email, this.name);
    }

    public CalendarPerson getAsCalendarPerson() {
        if (this.calendarPerson == null) {
            this.calendarPerson = new CalendarPerson(this.id, this.email, this.name);
        }
        return this.calendarPerson;
    }

    private void updateCalendarPerson() {
        this.calendarPerson = new CalendarPerson(this.id, this.email, this.name);
    }
    

    public ArrayList<String> getEventPoolList() {
        if (this.eventPoolList == null) { 
            this.eventPoolList = new ArrayList<>();
        }
        return eventPoolList;
    }

    public void setEventPoolList(ArrayList<String> eventPoolList) {
        if (eventPoolList == null) {
            eventPoolList = new ArrayList<>();
        }
        this.eventPoolList = eventPoolList;
    }
    public void addEventPool(String eventPoolId){
        if (this.eventPoolList == null) {
            this.eventPoolList = new ArrayList<>();
        }
        this.eventPoolList.add(eventPoolId);
    }

    public void setUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        this.userId = userId;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
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
