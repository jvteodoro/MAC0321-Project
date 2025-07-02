package br.com.agendusp.agendusp.dataobjects.calendarObjects;

// representa uma pessoa vinculada a um evento ou a um calendario
public class CalendarPerson {
    private String id;
    private String email;
    private String displayName;

    public CalendarPerson(String id, String email, String displayName) {
        setId(id);
        setEmail(email);
        setDisplayName(displayName);
    }

    public CalendarPerson() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID não pode ser nulo ou vazio.");
        }
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio.");
        }
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        if (displayName == null || displayName.isEmpty()) {
            throw new IllegalArgumentException("DisplayName não pode ser nulo ou vazio.");
        }
        this.displayName = displayName;
    }
}
