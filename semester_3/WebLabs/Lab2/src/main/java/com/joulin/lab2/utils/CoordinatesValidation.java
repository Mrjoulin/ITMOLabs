package com.joulin.lab2.utils;

import com.joulin.lab2.dto.Coordinates;

public class CoordinatesValidation {
    static public void validate(Coordinates coordinates) throws OutOfCoordinatesBoundsException {
        validateVariable("X", coordinates.getX(), Bounds.X_BOUNDS);
        validateVariable("Y", coordinates.getY(), Bounds.Y_BOUNDS);
        validateVariable("R", coordinates.getR(), Bounds.R_BOUNDS);
    }

    static private void validateVariable(String varName, double var, Bounds bounds) throws OutOfCoordinatesBoundsException {
        if (var < bounds.getLeft() || var > bounds.getRight()) {
            if (!bounds.isInclusive() && (var == bounds.getLeft() || var == bounds.getRight()))
                return;

            throw new OutOfCoordinatesBoundsException(varName, var, bounds);
        }
    }
}
