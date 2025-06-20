package br.com.agendusp.agendusp.dataobjects;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class EventDate {
    
    LocalDate date;
    LocalDateTime dateTime;
    ZoneId timeZone;

    public EventDate(){
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }

    

}
