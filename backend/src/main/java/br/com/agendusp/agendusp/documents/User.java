package br.com.agendusp.agendusp.documents;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.agendusp.agendusp.calendar.UserCalendarListRelation;


@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private String id; // ID gerada pelo MongoDB

    private String googleId; // Guarda o 'sub' (estável) do Google
    private String email;
    private String name;
    private ArrayList<UserCalendarListRelation> calendarList;

    public User() {
    }

    public User(String googleId, String email, String name) {
        this.googleId = googleId;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setUsername(String name) {
        this.name = name;
    }
    public void setUserCalendarListRelation(ArrayList<UserCalendarListRelation> calendarList) {
        this.calendarList = calendarList;
    }
    public void addUserCalendarListRelation(UserCalendarListRelation calendarListRelation) {
        if (this.calendarList == null) {
            this.calendarList = new ArrayList<UserCalendarListRelation>();
            this.calendarList.add(calendarListRelation);
        } else {
            this.calendarList.add(calendarListRelation);
        }
    }
    public ArrayList<UserCalendarListRelation> getUserCalendarListRelation() {
        return this.calendarList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        
        return this.name;   
    }
}
