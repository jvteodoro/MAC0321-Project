package br.com.agendusp.agendusp.dataobjects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class WatchResponse {
    String kind = "api#channel";
    String id;
    String resourceId;
    String resourceUri;
    String token;
    long expiration;
    // Resposta
    // {
    // "kind": "api#channel",
    // "id": string,
    // "resourceId": string,
    // "resourceUri": string,
    // "token": string,
    // "expiration": long
    // }

    public WatchResponse(String kind, String id, String resourceId, String resourceUri, String token, long expiration) {
        this.kind = kind;
        this.id = id;
        this.resourceId = resourceId;
        this.resourceUri = resourceUri;
        this.token = token;
        this.expiration = expiration;
    }

    public WatchResponse(Gson gson) {
        JsonObject obj = gson.toJsonTree(this).getAsJsonObject();
        this.kind = obj.get("kind").getAsString();
        this.id = obj.get("id").getAsString();
        this.resourceId = obj.get("resourceId").getAsString();
        this.resourceUri = obj.get("resourceUri").getAsString();
        this.token = obj.get("token").getAsString();
        this.expiration = obj.get("expiration").getAsLong();
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
