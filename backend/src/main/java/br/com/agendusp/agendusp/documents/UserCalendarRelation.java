package br.com.agendusp.agendusp.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserCalendarRelation { 
    @Id
    public String id;
    public String userId;    
    public String accessRole;

    UserCalendarRelation(String userId, String accessRole) { 
        this.userId = userId;
        this.accessRole = accessRole;
    }

    public String getId() {
        if (id == null) {
            throw new IllegalStateException("ID has not been set");
        }
        return id;
    }

    public void setId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        this.id = id;
    }

    public String getUserId() {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalStateException("User ID has not been set");
        }
        return userId;
    }

    public void setUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        this.userId = userId;
    }

    public String getAccessRole() {
        if (accessRole == null || accessRole.isEmpty()) {
            throw new IllegalStateException("Access role has not been set");
        }
        return accessRole;
    }

    public void setAccessRole(String accessRole) {
        if (accessRole == null || accessRole.isEmpty()) {
            throw new IllegalArgumentException("Access role cannot be null or empty");
        }
        this.accessRole = accessRole;
    }
}
