package br.com.agendusp.agendusp.dataobjects;

import java.text.DateFormat;
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

    

}
