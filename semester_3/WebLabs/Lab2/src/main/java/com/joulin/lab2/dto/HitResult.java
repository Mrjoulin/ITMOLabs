package com.joulin.lab2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class HitResult implements Serializable {
    private double x;
    private double y;
    private double r;
    private String currentTime;
    private double executionTime;
    private boolean result;

    public HitResult(Coordinates coordinates, String currentTime, double executionTime, boolean result) {
        this.x = coordinates.getX();
        this.y = coordinates.getY();
        this.r = coordinates.getR();
        this.currentTime = currentTime;
        this.executionTime = executionTime;
        this.result = result;
    }

    public Map<String, String> getMap() {
        Map<String, String> fields = new HashMap<>();
        fields.put("x", String.valueOf(x));
        fields.put("y", String.valueOf(y));
        fields.put("r", String.valueOf(r));
        fields.put("currentTime", String.valueOf(currentTime));
        fields.put("executionTime", String.valueOf(executionTime));
        fields.put("result", String.valueOf(result));
        return fields;
    }
}
