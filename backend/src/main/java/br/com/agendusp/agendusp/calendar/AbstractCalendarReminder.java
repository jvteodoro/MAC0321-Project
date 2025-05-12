package br.com.agendusp.agendusp.calendar;

public class AbstractCalendarReminder {
    String method;
    int minutes;

    public AbstractCalendarReminder(String method, int minutes) {
        if (method == null || method.isEmpty()) {
            throw new IllegalArgumentException("Method cannot be null or empty");
        }
        //if (minutes == null) {
        //    throw new IllegalArgumentException("Minutes cannot be null");
        //}
        if (!method.equals("email") && !method.equals("popup")) {
            throw new IllegalArgumentException("Invalid method: " + method);
        }
        if (minutes < 0) {
            throw new IllegalArgumentException("Minutes cannot be negative");
        }
        else if (minutes > 40320) {
            throw new IllegalArgumentException("Minutes cannot be greater than 40320");
        }
        this.method = method;
        this.minutes = minutes;
    }
}
