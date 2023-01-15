package com.joulin.Lab4.utils;

public class OutOfCoordinatesBoundsException extends Exception {
    public OutOfCoordinatesBoundsException(String coordinateName, double coordinateValue, Bounds bounds) {
        super(
                String.format(
                        "Coordinate %s = %.1f is out of bounds: %s%.1f, %.1f%s",
                        coordinateName, coordinateValue, bounds.isInclusive() ? "[" : "(",
                        bounds.getLeft(), bounds.getRight(), bounds.isInclusive() ? "]" : ")"
                )
        );
    }
}
