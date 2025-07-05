package br.com.agendusp.agendusp.documents;

import java.util.ArrayList;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.agendusp.agendusp.dataobjects.PollNotification;
import br.com.agendusp.agendusp.dataobjects.calendarObjects.CalendarPerson;

@Document(collection = "users")
public class User { // objetos dessa classe serão salvos na coleção users do MongoDB

    @Id
    private String id; // ID gerada pelo MongoDB
    private String DisplayName;

    private String googleId; // Guarda o 'sub' (estável) do Google
    private String email;
    private String name;
    private ArrayList<String> eventPollList = new ArrayList<>();
    private ArrayList<PollNotification> eventPollNotifications = new ArrayList<>(); 
    private ArrayList<CalendarListResource> calendarList = new ArrayList<>(); // Índice 0 é o calendário principal do usuário
    private CalendarPerson calendarPerson;

    public User() {
    }

    public User(String userId) { // Construtor para criar um usuário com um ID específico
        this.id = userId; // 
        this.googleId = null; // Google ID pode ser nulo se o usuário não se autenticou com o Google
    }

    public User(String googleId, String email, String name) { // Construtor para criar um usuário com Google ID, email e
                                                              // nome
        this.id = googleId;
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
        if (this.calendarPerson == null){
            this.calendarPerson = new CalendarPerson();
        }
        this.calendarPerson.setId(this.googleId);
        this.calendarPerson.setEmail(this.email);
        this.calendarPerson.setDisplayName(name);
    }

    public ArrayList<String> getEventPollList() {
        if (this.eventPollList == null) {
            this.eventPollList = new ArrayList<>();
        }
        return eventPollList;
    }

    public void setEventPollList(ArrayList<String> eventPollList) {
        if (eventPollList == null) {
            eventPollList = new ArrayList<>();
        }
        this.eventPollList = eventPollList;
    }

    public void addEventPoll(String eventPollId) {
        if (this.eventPollList == null) {
            this.eventPollList = new ArrayList<>();
        }
        this.eventPollList.add(eventPollId);
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

    public ArrayList<PollNotification> getEventPollNotifications() {
        return eventPollNotifications;
    }

    public void addEventPollNotifications(PollNotification eventPollNotification) {
        this.eventPollNotifications.add(eventPollNotification);
    }

    public void setEventPollNotifications(ArrayList<PollNotification> eventPollNotifications) {
        this.eventPollNotifications = eventPollNotifications;
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
        //updateCalendarPerson();
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
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'getAuthorities'");
    // }

    // @Override
    // public String getPassword() {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'getPassword'");
    // }

    // @Override
    // public String getUsername() {
    // // TODO Auto-generated method stub

    // // public void runArray(){
    // // //implementar
    // // }

    // return this.name;
    // }
}
