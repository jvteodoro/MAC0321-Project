package br.com.agendusp.agendusp.calendar;

import org.springframework.http.HttpStatusCode;

public interface Calendar {
    public HttpStatusCode clear();
    public String getId();
}
