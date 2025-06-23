package br.com.agendusp.agendusp.dataobjects;

import java.time.LocalDateTime;

public class DateTimeInterval implements Comparable<DateTimeInterval> {
    LocalDateTime start;
    LocalDateTime end;

    public DateTimeInterval(){}

    @Override
    public int compareTo(DateTimeInterval other){
        return this.start.compareTo(other.getStart());
    }

    public LocalDateTime getStart() {
        return start;
    }
    public void setStart(LocalDateTime start) {
        this.start = start;
    }
    public LocalDateTime getEnd() {
        return end;
    }
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    
}
