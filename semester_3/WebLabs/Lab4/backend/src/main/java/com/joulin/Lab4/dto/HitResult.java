package com.joulin.Lab4.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HitResult {
    private double x;
    private double y;
    private double r;
    private String creationTime;
    private boolean result;

    public HitResult(Coordinates coordinates, String creationTime, boolean result) {
        this(coordinates.getX(), coordinates.getY(), coordinates.getR(), creationTime, result);
    }
}
