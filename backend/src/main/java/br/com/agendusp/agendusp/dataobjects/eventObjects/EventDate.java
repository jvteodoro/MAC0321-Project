package br.com.agendusp.agendusp.dataobjects.eventObjects;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class EventDate {
    
    LocalDate date;
    LocalDateTime dateTime;
    ZoneId timeZone;

    public EventDate(){
    }
    public EventDate(LocalDateTime dt){
        this.dateTime = dt;
        this.date = dt.toLocalDate();
    }
    public EventDate(LocalDateTime dt, ZoneId zId){
        this.dateTime = dt;
        this.date = dt.toLocalDate();
        this.timeZone = zId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date,DateTimeFormatter.ISO_DATE);
    }
    public void setDateFromObject(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTimeFromObject(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    public void setDateTime(String dateTime) {
         this.dateTime = LocalDateTime.parse(dateTime,DateTimeFormatter.ISO_DATE_TIME);
       
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }
    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }

    public LocalDateTime toLocalDateTime() {
        if (dateTime != null) {
            return dateTime;
        } else if (date != null) {
            return date.atStartOfDay();
        } else {
            throw new IllegalStateException("EventDate does not have date or dateTime set.");
        }
    }

    public boolean isBefore(LocalDateTime other) {
        return toLocalDateTime().isBefore(other);
    }

    public boolean isAfter(LocalDateTime other) {
        return toLocalDateTime().isAfter(other);
    }

}
