package com.kainos.entity;

public class Message {
    String content;
    boolean trend;
    int sample;

    public Message(String content, boolean trend, int sample) {
        this.content = content;
        this.trend = trend;
        this.sample = sample;
    }

    public int getSample() {
        return sample;
    }

    public void setSample(int sample) {
        this.sample = sample;
    }

    public boolean isTrend(){
        return trend;
    }

    public void setTrend(boolean trend) {
        this.trend = trend;
    }

    public String getContent() {
        return content;

    }

    public void setContent(String content) {
        this.content = content;
    }



}
