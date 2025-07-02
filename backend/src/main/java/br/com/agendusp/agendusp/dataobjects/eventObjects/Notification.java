package br.com.agendusp.agendusp.dataobjects.eventObjects;

// notificacao de eventos
public class Notification {
    private String id;
    private String userId;
    private String message;

    public Notification(String userId, String message) {
        this.id = java.util.UUID.randomUUID().toString();
        this.userId = userId;
        this.message = message;
    }

    public Notification() {
        this.id = java.util.UUID.randomUUID().toString();
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

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
