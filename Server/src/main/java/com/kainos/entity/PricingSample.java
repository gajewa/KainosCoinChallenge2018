package com.kainos.entity;

import java.util.Date;

public class PricingSample {
    private Date time;
    private Double average;

    public PricingSample(Date time, Double average) {
        this.time = time;
        this.average = average;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }
}
