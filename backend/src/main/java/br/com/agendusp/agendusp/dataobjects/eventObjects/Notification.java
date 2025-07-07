package br.com.agendusp.agendusp.dataobjects.eventObjects;

// notificacao de eventos
public class Notification {
    private String id;
    private String userId;
    private String message;
    String type;
    private Object source = null;

    public Notification() {}

    public Notification(String userId, String message) {
        this.id = java.util.UUID.randomUUID().toString();
        this.userId = userId;
        this.message = message;
    }

    public Notification( Object source, String userId, String message, String type) {
        this.id = java.util.UUID.randomUUID().toString();
        this.userId = userId;
        this.message = message;
        this.source = source;
        this.type  = type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public Object getSource() {
        return this.source;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    public void setlinkedObjectId(Object source) {
        this.source = source;
    }
}
