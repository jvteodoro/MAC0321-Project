package br.com.agendusp.agendusp.dataobjects;

import br.com.agendusp.agendusp.documents.CalendarListResource;

public class CalendarListList {
    String kind = "calendar#calendarList";
    String etag;
    String nextPageToken;
    String nextSyncToken;
    CalendarListResource[] items;
    
    public String getKind() {
        return kind;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
    public String getEtag() {
        return etag;
    }
    public void setEtag(String etag) {
        this.etag = etag;
    }
    public String getNextPageToken() {
        return nextPageToken;
    }
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
    public String getNextSyncToken() {
        return nextSyncToken;
    }
    public void setNextSyncToken(String nextSyncToken) {
        this.nextSyncToken = nextSyncToken;
    }
    public CalendarListResource[] getItems() {
        return items;
    }
    public void setItems(CalendarListResource[] items) {
        this.items = items;
    }

    


}
