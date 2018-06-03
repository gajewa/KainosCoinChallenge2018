package com.kainos.entity;

public class Point {
    private Integer x;
    private Double y;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Point(Integer x, Double y) {

        this.x = x;
        this.y = y;
    }
}
