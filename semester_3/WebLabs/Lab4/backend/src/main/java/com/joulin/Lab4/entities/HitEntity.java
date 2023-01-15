package com.joulin.Lab4.entities;

import com.joulin.Lab4.dto.HitResult;
import lombok.*;

import javax.persistence.*;


@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hitsResults")
public class HitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    private Integer id;
    private Long ownerId;

    private double x;
    private double y;
    private double r;
    private String creationTime;
    private boolean result;

    private boolean removed = false;

    public HitEntity(Long ownerId, HitResult hitResult) {
        this.ownerId = ownerId;
        this.x = hitResult.getX();
        this.y = hitResult.getY();
        this.r = hitResult.getR();
        this.creationTime = hitResult.getCreationTime();
        this.result = hitResult.isResult();
    }

    public HitResult toHitResult() {
        return new HitResult(x, y, r, creationTime, result);
    }
}
