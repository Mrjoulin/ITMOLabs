package com.joulin.lab2.servlets;

import com.joulin.lab2.dto.HttpError;
import com.joulin.lab2.dto.ResponseData;
import com.joulin.lab2.dto.ResponseError;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "ErrorHandleServlet", value = "/error")
public class ErrorHandleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getAttribute("error") != null) {
            HttpError httpError = (HttpError) request.getAttribute("error");
            Integer numRequests = (Integer) request.getSession().getAttribute("numRequests");

            ResponseError responseError = new ResponseError(httpError.getErrorMessage(), numRequests);

            response.setStatus(httpError.getStatusCode());
            response.getWriter().println(responseError.toJson());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
