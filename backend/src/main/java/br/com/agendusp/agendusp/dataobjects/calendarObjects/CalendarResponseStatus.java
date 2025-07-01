package br.com.agendusp.agendusp.dataobjects.calendarObjects;

import java.lang.IllegalArgumentException;

public class CalendarResponseStatus {
    String responseStatus;

    public CalendarResponseStatus(String responseStatus) throws IllegalArgumentException {
        setResponseStatus(responseStatus);
    }

    public CalendarResponseStatus() {
        this.responseStatus = "needsAction";
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;

    }
}
