package com.kainos.entity;

import java.util.ArrayList;

public class Dataset {
    private String[] labels;
    private ArrayList<Double> btcValues;
    private ArrayList<Double> ethValues;
    private ArrayList<Double> ltcValues;
    private ArrayList<Message> messages;
    private ArrayList<ArrayList<Double>> btcTrend;
    private ArrayList<ArrayList<Double>> ethTrend;
    private ArrayList<ArrayList<Double>> ltcTrend;

    public ArrayList<ArrayList<Double>> getBtcTrend() {
        return btcTrend;
    }

    public void setBtcTrend(ArrayList<ArrayList<Double>> btcTrend) {
        this.btcTrend = btcTrend;
    }

    public ArrayList<Double> getBtcValues() {
        return btcValues;
    }

    public void setBtcValues(ArrayList<Double> btcValues) {
        this.btcValues = btcValues;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public Dataset(String[] labels, ArrayList<Double> btcValues, ArrayList<Double> ethValues, ArrayList<Double> ltcValues, ArrayList<Message> messages, ArrayList<ArrayList<Double>> btcTrend, ArrayList<ArrayList<Double>> ethTrend, ArrayList<ArrayList<Double>> ltcTrend) {
        this.labels = labels;
        this.btcValues = btcValues;
        this.ethValues = ethValues;
        this.ltcValues = ltcValues;
        this.messages = messages;
        this.btcTrend = btcTrend;
        this.ethTrend = ethTrend;
        this.ltcTrend = ltcTrend;
    }

    public ArrayList<ArrayList<Double>> getEthTrend() {
        return ethTrend;
    }

    public ArrayList<ArrayList<Double>> getLtcTrend() {
        return ltcTrend;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public ArrayList<Double> getLtcValues() {
        return ltcValues;
    }

    public void setLtcValues(ArrayList<Double> ltcValues) {
        this.ltcValues = ltcValues;
    }

    public ArrayList<Double> getEthValues() {
        return ethValues;
    }

    public void setEthValues(ArrayList<Double> ethValues) {
        this.ethValues = ethValues;
    }
}
