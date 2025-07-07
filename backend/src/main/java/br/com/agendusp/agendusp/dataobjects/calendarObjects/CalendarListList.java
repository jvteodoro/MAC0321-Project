package br.com.agendusp.agendusp.dataobjects.calendarObjects;

import java.util.ArrayList;
import java.util.List;
import br.com.agendusp.agendusp.documents.CalendarListResource;

// representa a lista de calendários do usuário retornada pela Google Calendar API
public class CalendarListList {
    private final String kind = "calendar#calendarList";
    private String etag;
    private String nextPageToken;
    private String nextSyncToken;
    private List<CalendarListResource> items = new ArrayList<>();

    public String getKind() {
        return kind;
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

    public List<CalendarListResource> getItems() {
        return items;
    }

    public void setItems(List<CalendarListResource> items) {
        this.items = items;
    }
}
