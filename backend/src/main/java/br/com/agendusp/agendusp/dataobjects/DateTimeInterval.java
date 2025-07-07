package br.com.agendusp.agendusp.dataobjects;

import java.time.LocalDateTime;
import java.time.Duration;

// intervalo de tempo, com tempo inicial e final e metodo para comparar/ordenar pelo inicio do intervalo
public class DateTimeInterval implements Comparable<DateTimeInterval> {
    private LocalDateTime start;
    private LocalDateTime end;

    public DateTimeInterval() {}

    public DateTimeInterval(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("start e end não podem ser nulos");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("end não pode ser antes do start");
        }
        this.start = start;
        this.end = end;
    }

    @Override
    public int compareTo(DateTimeInterval other) {
        return this.start.compareTo(other.getStart());
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        if (end != null && start != null && start.isAfter(end)) {
            throw new IllegalArgumentException("Start não pode ser depois do end");
        }
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        // System.out.println("DateTimeInterval start: "+this.start.toString());
        // System.out.println("DateTimeInterval end: "+end.toString());
        if (end != null && start != null && end.isBefore(this.start)) {
            System.err.println("End: "+end.toString());
            throw new IllegalArgumentException("Start:"+this.start.toString()+" End:"+end.toString()+" .end não pode ser antes do start");
        }
        this.end = end;
    }

    public Duration getDuration() {
        if (start == null || end == null) {
            throw new IllegalStateException("start e end não podem ser nulos para calcular a duração.");
        }
        return Duration.between(start, end);
    }

}
