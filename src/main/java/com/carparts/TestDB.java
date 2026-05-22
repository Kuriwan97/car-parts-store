package com.carparts;

import com.carparts.dao.UserDAO;
import com.carparts.model.User;
import com.carparts.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class TestDB {
    public static void main(String[] args) {
        try {
            System.out.println("=== ПРОВЕРКА БАЗЫ ДАННЫХ ===\n");

            // 1. Проверка подключения
            Connection conn = DatabaseUtil.getConnection();
            System.out.println("✅ Подключение к БД успешно!");

            // 2. Просмотр всех пользователей
            System.out.println("\n--- Список пользователей в БД ---");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            boolean hasUsers = false;
            while (rs.next()) {
                hasUsers = true;
                System.out.println("ID: " + rs.getInt("id") +
                        ", Username: " + rs.getString("username") +
                        ", Password: " + rs.getString("password") +
                        ", Role: " + rs.getString("role"));
            }
            if (!hasUsers) {
                System.out.println("⚠️ Пользователей нет в БД!");
            }

            // 3. Проверка через DAO
            System.out.println("\n--- Проверка через UserDAO ---");
            UserDAO userDAO = new UserDAO();
            User admin = userDAO.findByUsername("admin");
            if (admin != null) {
                System.out.println("✅ Админ найден: " + admin.getUsername() + " / " + admin.getPassword());
            } else {
                System.out.println("❌ Админ НЕ найден!");
            }

            User testUser = userDAO.findByUsername("testuser");
            if (testUser != null) {
                System.out.println("✅ testuser найден: " + testUser.getUsername());
            } else {
                System.out.println("❌ testuser НЕ найден");
            }

            // 4. Регистрация нового пользователя
            System.out.println("\n--- Регистрация нового пользователя ---");
            User newUser = new User();
            newUser.setUsername("testuser2");
            newUser.setPassword("123456");
            newUser.setEmail("test2@mail.com");
            newUser.setPhone("123456789");
            newUser.setRole("user");

            if (userDAO.create(newUser)) {
                System.out.println("✅ Пользователь testuser2 создан!");
            } else {
                System.out.println("❌ Ошибка создания пользователя");
            }

            // 5. Проверка входа
            System.out.println("\n--- Проверка входа ---");
            User loginUser = userDAO.findByUsername("testuser2");
            if (loginUser != null && loginUser.getPassword().equals("123456")) {
                System.out.println("✅ Вход успешен для testuser2!");
            } else {
                System.out.println("❌ Вход не удался");
            }

            conn.close();
            System.out.println("\n=== ПРОВЕРКА ЗАВЕРШЕНА ===");

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}