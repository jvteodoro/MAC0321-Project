package br.com.agendusp.agendusp.dataobjects.calendarObjects;

import java.lang.IllegalArgumentException;

// no Google Calendar API, valores possíveis para responseStatus são "needsAction", "declined", "tentative", "accepted"
public class CalendarResponseStatus {
    private String responseStatus;

    public CalendarResponseStatus(String responseStatus) {
        setResponseStatus(responseStatus);
    }

    public CalendarResponseStatus() {
        this.responseStatus = "needsAction";
    }

    public void setResponseStatus(String responseStatus) {
        if (responseStatus == null || responseStatus.isEmpty()) {
            throw new IllegalArgumentException("Response status não pode ser nulo ou vazio.");
        }
        if (!responseStatus.equals("needsAction") &&
            !responseStatus.equals("declined") &&
            !responseStatus.equals("tentative") &&
            !responseStatus.equals("accepted")) {
            throw new IllegalArgumentException("Response status inválido: " + responseStatus);
        }
        this.responseStatus = responseStatus;
    }

    public String getResponseStatus() {
        return responseStatus;
    }
}
