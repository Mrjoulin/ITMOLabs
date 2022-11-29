package com.joulin.lab3.beans;

import com.joulin.lab3.db.HitResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.faces.annotation.ManagedProperty;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.function.Function;

@Getter
@Setter
@ToString
@ManagedBean(name = "client")
@SessionScoped
public class ClientBean implements Serializable {
    private final String sessionId;
    private final LinkedList<HitResult> currentHits;

    @ManagedProperty(value = "#{coordinates}")
    private Coordinates coordinates = new Coordinates();
    @ManagedProperty(value = "#{service}")
    private Service service = new Service();

    public ClientBean() {
        this.sessionId = FacesContext.getCurrentInstance().getExternalContext().getSessionId(true);
        this.currentHits = service.getUserHits(sessionId);
    }

    public void makeUserRequest() {
        makeRequest(this.coordinates);
    }

    public void makeRemoteRequest() {
        Function<String, Double> getParam = (name) -> {
            return Double.parseDouble(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name));
        };

        try {
            Coordinates coordinates = new Coordinates(getParam.apply("x"), getParam.apply("y"), getParam.apply("r"));
            makeRequest(coordinates);
        } catch (NullPointerException | NumberFormatException exception) {
            System.out.println("Can't parse values from request params");
        }
    }

    public void makeRequest(Coordinates coordinates) {
        System.out.println("Make request: " + coordinates.toString());
        HitResult result = service.processRequest(this.sessionId, coordinates);

        if (result != null)
            this.currentHits.addFirst(result);
    }

    public void clearHits() {
        currentHits.clear();
        service.clearUserHits(this.sessionId);

        System.out.println("Current hits: " + currentHits);
    }
}
