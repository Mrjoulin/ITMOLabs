package com.joulin.lab2.servlets;

import com.joulin.lab2.dto.HitStorage;
import com.joulin.lab2.dto.HttpError;
import com.joulin.lab2.dto.ResponseData;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ControllerServlet", value = "/controller")
public class ControllerServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        if (session.getAttribute("hitStorage") == null) {
            session.setAttribute("hitStorage", new HitStorage());
        }

        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean checkConfirmed = checkSessionActions(request, response);

        if (checkConfirmed) return;

        if (request.getAttribute("error") != null)
            forwardTo("/error", request, response);

        forwardTo("/check", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(
            "error", new HttpError(404, "Page not found!")
        );

        forwardTo("/error", request, response);
    }

    private void forwardTo(String url, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getRequestDispatcher(url).forward(request, response);
    }

    private boolean checkSessionActions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sessionAction = request.getParameter("session");

        if (sessionAction == null) return false;

        HitStorage storage = (HitStorage) request.getSession().getAttribute("hitStorage");
        Integer numRequests = (Integer) request.getSession().getAttribute("numRequests");

        // Clear session action
        if (sessionAction.equals("clear") || sessionAction.equals("get-hits")) {
            if (sessionAction.equals("clear")) storage.clear();

            ResponseData data = new ResponseData(storage.getMap(), numRequests);

            response.getWriter().write(data.toJson());
            return true;
        }

        return false;
    }
}
