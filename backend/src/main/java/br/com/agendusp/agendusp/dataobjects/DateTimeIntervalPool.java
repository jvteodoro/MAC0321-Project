package br.com.agendusp.agendusp.dataobjects;

import java.util.ArrayList;

public class DateTimeIntervalPool {

    DateTimeInterval dateTimeInterval;
    String id;
    int votes;

    public DateTimeIntervalPool(){}

    public DateTimeIntervalPool(DateTimeInterval dt){
        this.dateTimeInterval = dt;
    }

    public void setDateTimeInterval(DateTimeInterval dt){
        this.dateTimeInterval = dt;
        votes = 0;
    }

    public DateTimeInterval getDateTimeInterval(){
        return this.dateTimeInterval;
    }

    public void vote(){
        this.votes += 1;
    }
    public int getVotes(){
        return this.votes;
    }
    public void setVotes(int votes_num){
        this.votes = votes_num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
