package com.carparts.servlet;

import com.carparts.dao.UserDAO;
import com.carparts.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        System.out.println("=== ПОПЫТКА ВХОДА ===");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        try {
            User user = userDAO.findByUsername(username);

            if (user == null) {
                System.out.println("❌ Пользователь НЕ НАЙДЕН: " + username);
                resp.sendRedirect(req.getContextPath() + "/login.html?error=notfound");
                return;
            }

            System.out.println("Найден пользователь:");
            System.out.println("  ID: " + user.getId());
            System.out.println("  Username: " + user.getUsername());
            System.out.println("  Password в БД: " + user.getPassword());
            System.out.println("  Введенный пароль: " + password);
            System.out.println("  Role: " + user.getRole());

            if (user.getPassword().equals(password)) {
                System.out.println("✅ Пароль верный!");

                HttpSession session = req.getSession();
                session.setAttribute("user", user);
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());
                session.setAttribute("userId", user.getId());

                System.out.println("Сессия создана. Роль: " + user.getRole());

                if ("admin".equals(user.getRole())) {
                    System.out.println("→ Перенаправление на admin.html");
                    resp.sendRedirect(req.getContextPath() + "/admin.html");
                } else {
                    System.out.println("→ Перенаправление на catalog.html");
                    resp.sendRedirect(req.getContextPath() + "/catalog.html");
                }
            } else {
                System.out.println("❌ НЕВЕРНЫЙ ПАРОЛЬ!");
                resp.sendRedirect(req.getContextPath() + "/login.html?error=wrongpass");
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка SQL: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/login.html?error=sql");
        }
    }
}