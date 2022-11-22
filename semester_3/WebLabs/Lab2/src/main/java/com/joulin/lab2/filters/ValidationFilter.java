package com.joulin.lab2.filters;

import com.joulin.lab2.dto.Coordinates;
import com.joulin.lab2.dto.HttpError;
import com.joulin.lab2.utils.CoordinatesValidation;
import com.joulin.lab2.utils.OutOfCoordinatesBoundsException;
import com.joulin.lab2.utils.ParameterNotPassedError;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebFilter(filterName = "ValidationFilter", urlPatterns = "*")
public class ValidationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (httpRequest.getMethod().equals("GET")) {
            request.setAttribute("startTime", System.nanoTime());

            try {
                Coordinates coordinates = parseCoordinates(request);

                CoordinatesValidation.validate(coordinates);

                request.setAttribute("coordinates", coordinates);
            } catch (ParameterNotPassedError | OutOfCoordinatesBoundsException e) {
                request.setAttribute("error", new HttpError(400, e.getMessage()));
            }
        }

        chain.doFilter(request, response);
    }

    private Coordinates parseCoordinates(ServletRequest request) throws ParameterNotPassedError {
        String[] params = {"x", "y", "r"};
        HashMap<String, Double> parsedParams = new HashMap<>();

        for (String param : params) {
            try {
                double cur = Double.parseDouble(request.getParameter(param));
                parsedParams.put(param, cur);
            } catch (NullPointerException | NumberFormatException e) {
                throw new ParameterNotPassedError(param, "double");
            }
        }

        return new Coordinates(
            parsedParams.get(params[0]),
            parsedParams.get(params[1]),
            parsedParams.get(params[2])
        );
    }
}
