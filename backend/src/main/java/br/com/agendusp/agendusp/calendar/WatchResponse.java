package br.com.agendusp.agendusp.calendar;

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
    //{
    //    "kind": "api#channel",
    //    "id": string,
    //    "resourceId": string,
    //    "resourceUri": string,
    //    "token": string,
    //    "expiration": long
    //  }

    public WatchResponse (String kind, String id, String resourceId, String resourceUri, String token, long expiration) {
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
}
