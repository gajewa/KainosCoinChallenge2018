package com.kainos.entity;

public class LinearEquation {
    private Double a;
    private Double b;
    private boolean found = true;

    public LinearEquation() {
    }

    public boolean isFound() {

        return found;
    }

    public LinearEquation(boolean found) {

        this.found = found;
    }

    public void calculateCoefficients(Point p1, Point p2){
        this.a = ( p2.getY() - p1.getY() ) / (p2.getX() - p1.getX());
        this.b = p1.getY() - p1.getX()*a;
    }

    public void print() {
        System.out.println("y = " + this.a + "x + " + this.b);
    }

    public Double calculateValue(Integer x){
        return this.a * x + this.b;
    }

    public Double getA() {
        return a;
    }

    public Double getB() {
        return b;
    }
}
