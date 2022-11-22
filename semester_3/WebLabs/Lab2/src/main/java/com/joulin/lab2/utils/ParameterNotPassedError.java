package com.joulin.lab2.utils;

public class ParameterNotPassedError extends Exception {
    public ParameterNotPassedError(String paramName, String paramType) {
        super(
            String.format(
                "Parameter \"%s\" not given or incorrect type (need %s)",
                paramName, paramType
            )
        );
    }
}
