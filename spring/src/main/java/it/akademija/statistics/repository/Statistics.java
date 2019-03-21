package it.akademija.statistics.repository;

public class Statistics {
    private long count;
    private String type;

    public Statistics(){}

    public Statistics(long count, String type) {
        this.count = count;
        this.type = type;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

