package com.polina.practice;

public class Cube implements Solid{
    private double side;

    public Cube(double side) {
        this.side = side;
    }

    public double getVolume() {
        return Math.pow(side, 3);
    }

    public double getSurfaceArea() {
        return 6 * side * side;
    }
}
