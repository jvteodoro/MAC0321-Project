package br.com.agendusp.agendusp.dataobjects;

// representa a contagem de votos de um horario
public class DateTimeIntervalPoll {

    private DateTimeInterval dateTimeInterval;
    private String id;
    private int votes;

    public DateTimeIntervalPoll() {}

    public DateTimeIntervalPoll(DateTimeInterval dt) {
        setDateTimeInterval(dt);
    }

    public DateTimeIntervalPoll(DateTimeInterval dt, String id) {
        this(dt);
        this.id = id;
    }

    public void setDateTimeInterval(DateTimeInterval dt) {
        this.dateTimeInterval = dt;
        votes = 0;
    }

    public DateTimeInterval getDateTimeInterval() {
        return this.dateTimeInterval;
    }

    public void vote() {
        this.votes += 1;
    }

    public int getVotes() {
        return this.votes;
    }

    public void setVotes(int votes_num) {
        this.votes = votes_num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
