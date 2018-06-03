package com.kainos.entity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class TrendCalculator {

    public ArrayList<ArrayList<Double>> getTrendSamples(Double[] arr){
        ArrayList<LinearEquation> trendLineList = new ArrayList<>();
        ArrayList<ArrayList<Double>> samples = new ArrayList<>();



        for(int i=0; i<5; i++){
            trendLineList.add(getTrendLine(Arrays.copyOfRange(arr, i*7, (i+1)*7 )));
        }

        for (LinearEquation trendLine : trendLineList) {



            ArrayList<Double> temp = new ArrayList<>();

            if(!trendLine.isFound()){
                samples.add(temp);
                continue;
            }

            for (int j = 0; j < 7; j++) {
                temp.add(trendLine.calculateValue(j));
            }

            samples.add(temp);
        }

        return samples;
    }

    public Point[] getMaxs(Double[] arr){
        ArrayList<Point> al = new ArrayList<Point>();

        if(arr[0] > arr[1])
            al.add(new Point(0, arr[0]));

        for (int i = 1; i < arr.length - 1; i++ ){
            if(arr[i]>arr[i+1] && arr[i] > arr[i-1])
                al.add(new Point(i, arr[i]));
        }

        if(arr[arr.length-1] > arr[arr.length-2])
            al.add(new Point(arr.length-1, arr[arr.length-1]));

        return al.toArray(new Point[0]);
    }

    public Point[] getMins(Double[] arr){
        ArrayList<Point> al = new ArrayList<Point>();

        if(arr[0] < arr[1])
            al.add(new Point(0, arr[0]));

        for (int i = 1; i < arr.length - 1; i++ ){
            if(arr[i]<arr[i+1] && arr[i] < arr[i-1])
                al.add(new Point(i, arr[i]));
        }

        if(arr[arr.length-1] < arr[arr.length-2])
            al.add(new Point(arr.length-1, arr[arr.length-1]));

        return al.toArray(new Point[0]);
    }

    public LinearEquation getTrendLine(Double[] arr){
        if(arr[arr.length-1] - arr[0] < 0)
            return getDescTrend(arr);
        else
            return getAscTrend(arr);
    }

    public LinearEquation getDescTrend(Double[] arr){ // get descending trendline
        Point[] n = getMaxs(arr);

        if (n.length<2){
            return new LinearEquation(false);
        }

        LinearEquation equation = new LinearEquation();

        equation.calculateCoefficients(n[0], n[1]);
        equation.print();
        Point[] finals = new Point[] {n[0], n[1]};

        for (Point aN : n) {
            if (aN.getY() < equation.calculateValue(aN.getX())){
                LinearEquation newEquation = new LinearEquation();
                newEquation.calculateCoefficients(finals[1], aN);

                if(finals[0].getY() < newEquation.calculateValue(finals[0].getX())){
                    equation.calculateCoefficients(finals[1], aN);
                    finals = new Point[] {finals[1], aN};
                    equation.print();
                }
            }
        }

        return equation;
    }

    public LinearEquation getAscTrend(Double[] arr){
        Point[] n = getMins(arr);

        if (n.length<2){
            return new LinearEquation(false);
        }

        LinearEquation equation = new LinearEquation();

        equation.calculateCoefficients(n[0], n[1]);
        equation.print();
        Point[] finals = new Point[] {n[0], n[1]};

        for (Point aN : n) {
            if (aN.getY() > equation.calculateValue(aN.getX())){
                LinearEquation newEquation = new LinearEquation();
                newEquation.calculateCoefficients(finals[1], aN);

                if(finals[0].getY() > newEquation.calculateValue(finals[0].getX())){
                    equation.calculateCoefficients(finals[1], aN);
                    finals = new Point[] {finals[1], aN};
                    equation.print();
                }
            }
        }

        return equation;
    }
}
