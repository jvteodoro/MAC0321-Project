package br.com.agendusp.agendusp.dataobjects;

// representa a contagem de votos de um horario
public class DateTimeIntervalPoll {

    private DateTimeInterval dateTimeInterval;
    private int id;
    private int votes;

    public DateTimeIntervalPoll() {}

    public DateTimeIntervalPoll(DateTimeInterval dt) {
        setDateTimeInterval(dt);
    }

    public DateTimeIntervalPoll(DateTimeInterval dt, int id) {
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
        System.out.println("Votes 0: " + this.votes);
        this.votes += 1;
        System.out.println("Votes 1: " + this.votes);
    }

    public int getVotes() {
        return this.votes;
    }

    public void setVotes(int votes_num) {
        this.votes = votes_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
