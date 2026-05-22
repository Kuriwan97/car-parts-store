package com.carparts.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Получаем текущую сессию
        HttpSession session = req.getSession(false);

        // Если сессия существует, удаляем её
        if (session != null) {
            session.invalidate();
        }

        // Перенаправляем на страницу входа
        resp.sendRedirect(req.getContextPath() + "/login.html");
    }
}