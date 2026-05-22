package com.carparts.servlet;

import com.carparts.dao.UserDAO;
import com.carparts.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        System.out.println("=== РЕГИСТРАЦИЯ ===");
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
        System.out.println("Password: " + password);

        try {
            // Проверка существования пользователя
            User existing = userDAO.findByUsername(username);
            if (existing != null) {
                System.out.println("❌ Пользователь уже существует: " + username);
                resp.sendRedirect(req.getContextPath() + "/register.html?error=exists");
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setPhone(phone != null ? phone : "");
            user.setRole("user");

            System.out.println("Создание пользователя...");
            boolean created = userDAO.create(user);

            if (created) {
                System.out.println("✅ Пользователь успешно создан: " + username);
                resp.sendRedirect(req.getContextPath() + "/login.html?success=1");
            } else {
                System.out.println("❌ Ошибка при создании пользователя");
                resp.sendRedirect(req.getContextPath() + "/register.html?error=create");
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка SQL: " + e.getMessage());
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/register.html?error=sql");
        }
    }
}