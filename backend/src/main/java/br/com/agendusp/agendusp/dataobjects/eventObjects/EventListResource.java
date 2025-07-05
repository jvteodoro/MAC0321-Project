package br.com.agendusp.agendusp.dataobjects.eventObjects;

import java.util.ArrayList;

import br.com.agendusp.agendusp.dataobjects.DefaultReminderResource;
import br.com.agendusp.agendusp.documents.EventsResource;

public class EventListResource {
  String kind = "calendar#events";
  String etag;
  String summary;
  String description;
  String updated;
  String timeZone;
  String accessRole;
  DefaultReminderResource[] defaultRemindersList;
  String nextPageToken;
  String nextSyncToken;
  ArrayList<EventsResource> items = new ArrayList<>();;
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
  public String getSummary() {
    return summary;
  }
  public void setSummary(String summary) {
    this.summary = summary;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getUpdated() {
    return updated;
  }
  public void setUpdated(String updated) {
    this.updated = updated;
  }
  public String getTimeZone() {
    return timeZone;
  }
  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }
  public String getAccessRole() {
    return accessRole;
  }
  public void setAccessRole(String accessRole) {
    this.accessRole = accessRole;
  }
  public DefaultReminderResource[] getDefaultRemindersList() {
    return defaultRemindersList;
  }
  public void setDefaultRemindersList(DefaultReminderResource[] defaultRemindersList) {
    this.defaultRemindersList = defaultRemindersList;
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
  public ArrayList<EventsResource> getItems() {
    return items;
  }
  public void setItems(ArrayList<EventsResource> items) {
    this.items = items;
  } 

  

}
