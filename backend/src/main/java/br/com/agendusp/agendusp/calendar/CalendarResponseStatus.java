package br.com.agendusp.agendusp.calendar;

import java.lang.IllegalArgumentException;



public class CalendarResponseStatus {
    String responseStatus;

    public CalendarResponseStatus(String responseStatus) throws IllegalArgumentException {
        setResponseStatus(responseStatus);
    }
    
    public CalendarResponseStatus() {
        this.responseStatus = "needsAction";
    }

    public void setResponseStatus(String responseStatus) throws IllegalArgumentException {
        if (responseStatus == null || responseStatus.isEmpty()) {
            throw new IllegalArgumentException("Response status cannot be null or empty");
        }
        if (responseStatus != "needsAction" && responseStatus != "declined" 
            && responseStatus != "tentative" && responseStatus != "accepted") {
            throw new IllegalArgumentException("Invalid response status: " + responseStatus);

        }
        this.responseStatus = responseStatus;

    }
}
