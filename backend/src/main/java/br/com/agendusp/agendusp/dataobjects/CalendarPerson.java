package br.com.agendusp.agendusp.dataobjects;

public class CalendarPerson {
    String id;
    String email;
    String displayName;

    public CalendarPerson(String id, String email, String name){
        this.id = id;
        this.email = email;
        this.displayName = name;
     }

    public CalendarPerson(){

    }
     
    public String getId(){
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
