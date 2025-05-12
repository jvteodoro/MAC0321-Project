package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;

import org.springframework.http.HttpStatusCode;

public interface CalendarListController {
    public HttpStatusCode delete();
    public ArrayList<Calendar> get(Calendar calendar);
    public CalendarList insert(Calendar calendar);
    public CalendarList list();
    public CalendarList patch(Calendar calendar);
    public CalendarList update(Calendar calendar);
    public WatchResponse watch(WatchRequest watchRequest);

}
