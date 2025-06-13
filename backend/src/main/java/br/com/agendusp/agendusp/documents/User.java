package br.com.agendusp.agendusp.documents;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;


import br.com.agendusp.agendusp.dataobjects.CalendarPerson;


@Document(collection = "users")
public class User {//implements UserDetails {

    @Autowired
    private SecurityContext securityContext; // Para acessar o contexto de segurança

    @Id
    private String id; // ID gerada pelo MongoDB
    private String userId;

    private String googleId; // Guarda o 'sub' (estável) do Google
    private String email;
    private String name;
    private ArrayList<CalendarListUserItem> calendarList; // Índice 0 é o calendário principal do usuário
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

    public ArrayList<CalendarListUserItem> getCalendarList() {
        return calendarList;
    }
    public CalendarListUserItem addCalendarListUserItem(CalendarListUserItem calendarListUserItem) {
        if (this.calendarList == null) {
            this.calendarList = new ArrayList<>();
        }
        this.calendarList.add(calendarListUserItem);
        return calendarListUserItem;
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
