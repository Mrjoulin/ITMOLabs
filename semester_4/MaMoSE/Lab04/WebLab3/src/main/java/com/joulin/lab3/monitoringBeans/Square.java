package com.joulin.lab3.monitoringBeans;

import java.io.Serializable;

public class Square implements SquareMXBean, Serializable {
    private double lastSquare = 0;
    private final double triangleCoefficient = 0.25;
    private final double rectangleCoefficient = 1.0;
    private final double circleCoefficient = Math.PI / 4;

    @Override
    public double calculateSquare(double r) {
        lastSquare = (triangleCoefficient + rectangleCoefficient + circleCoefficient) * Math.pow(r, 2);

        return lastSquare;
    }

    @Override
    public double getLastSquare() {
        return lastSquare;
    }
}
