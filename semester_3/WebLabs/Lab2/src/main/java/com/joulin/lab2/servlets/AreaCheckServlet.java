package com.joulin.lab2.servlets;

import com.joulin.lab2.dto.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet(name = "AreaCheckServlet", value = "/check")
public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get data from attributes
        Coordinates coordinates = (Coordinates) request.getAttribute("coordinates");
        Long startTime = (Long) request.getAttribute("startTime");
        HitStorage hitStorage = (HitStorage) request.getSession().getAttribute("hitStorage");
        Integer numRequests = (Integer) request.getSession().getAttribute("numRequests");

        // Process hit
        HitResult hitResult = new HitResult(
            coordinates, getCurrentDate(), getExecutionTime(startTime), isHit(coordinates)
        );
        if (hitStorage != null) { hitStorage.add(hitResult); }

        ResponseData data = new ResponseData(Collections.singletonList(hitResult.getMap()), numRequests);

        // Write response
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().println(data.toJson());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("error", new HttpError(404, "<h1>Page not found:(</h1>"));
    }

    private boolean isHit(Coordinates coordinates) {
        return coordinates != null && isHit(coordinates.getX(), coordinates.getY(), coordinates.getR());
    }

    private boolean isHit(double x, double y, double r) {
        return isCircleHit(x, y, r) || isRectangleHit(x, y, r) || isTriangleHit(x, y, r);
    }

    private boolean isTriangleHit(double x, double y, double r) {
        return (x <= 0 && y >= 0) && (2 * y - x <= r);
    }

    private boolean isRectangleHit(double x, double y, double r) {
        return (x >= 0 && y >= 0) && (x <= r && y <= r);
    }

    private boolean isCircleHit(double x, double y, double r) {
        return (x >= 0 && y <= 0) && (x*x + y*y <= r*r);
    }

    private String getCurrentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    private double getExecutionTime(Long startTime) {
        return startTime != null ? (System.nanoTime() - startTime) / Math.pow(10, 6) : 0.0;
    }
}
