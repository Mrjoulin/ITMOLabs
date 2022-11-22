package com.joulin.lab2.filters;

import com.joulin.lab2.dto.Coordinates;
import com.joulin.lab2.dto.HitStorage;
import com.joulin.lab2.dto.HttpError;
import com.joulin.lab2.utils.CoordinatesValidation;
import com.joulin.lab2.utils.OutOfCoordinatesBoundsException;
import com.joulin.lab2.utils.ParameterNotPassedError;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@WebFilter(filterName = "CountRequestsFilter", urlPatterns = "*")
public class CountRequestsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        Integer numRequests = (Integer) session.getAttribute("numRequests");
        session.setAttribute("numRequests", numRequests == null ? 1 : numRequests + 1);

        chain.doFilter(request, response);
    }
}
