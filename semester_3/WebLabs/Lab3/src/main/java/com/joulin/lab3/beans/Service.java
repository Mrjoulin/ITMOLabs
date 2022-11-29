package com.joulin.lab3.beans;

import com.joulin.lab3.db.HitResult;
import com.joulin.lab3.utils.AreaCheck;
import com.joulin.lab3.db.DataBaseController;
import com.joulin.lab3.utils.CoordinatesValidation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@ToString
@SessionScoped
@ManagedBean(name = "service")
public class Service implements Serializable {
    private final AreaCheck areaCheck;
    private final DataBaseController dbController;

    public Service() {
        areaCheck = new AreaCheck();
        dbController = new DataBaseController();
    }

    public LinkedList<HitResult> getUserHits(String sessionId) {
        if (sessionId == null) return new LinkedList<>();

        List<HitResult> hits = dbController.getUserHits(sessionId);

        System.out.println("Return user hits: " + hits);

        return hits != null ? new LinkedList<>(hits) : new LinkedList<>();
    }

    public HitResult processRequest(String sessionId, Coordinates coordinates) {
        if (!CoordinatesValidation.validate(coordinates)) {
            System.out.println("Coordinates not valid");
            return null;
        }

        boolean isHit = areaCheck.isHit(coordinates);
        HitResult hitResult = new HitResult(sessionId, coordinates, getCurrentDate(), isHit);

        System.out.println("Get result:" + hitResult);

        dbController.addHitResult(hitResult);

        System.out.println("WTFFF");

        return hitResult;
    }

    public void clearUserHits(String sessionId) {
        dbController.markUserHitsRemoved(sessionId);
    }

    private String getCurrentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
