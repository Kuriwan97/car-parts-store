package com.carparts.util;

import java.sql.*;

public class DatabaseUtil {
    // Убираем параметры из URL - они не поддерживаются SQLite
    private static final String DB_URL = "jdbc:sqlite:car_parts_store.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            createTables();
            insertTestData();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        // Устанавливаем кодировку после подключения
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA encoding = 'UTF-8'");
        }
        return conn;
    }

    private static void createTables() {
        String createUsersTable =
                "CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT UNIQUE NOT NULL, " +
                        "password TEXT NOT NULL, " +
                        "email TEXT NOT NULL, " +
                        "phone TEXT, " +
                        "role TEXT DEFAULT 'user')";

        String createPartsTable =
                "CREATE TABLE IF NOT EXISTS parts (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "category TEXT NOT NULL, " +
                        "manufacturer TEXT NOT NULL, " +
                        "price REAL NOT NULL, " +
                        "stock INTEGER NOT NULL, " +
                        "description TEXT)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createPartsTable);

            // Добавление тестового админа
            String checkAdmin = "SELECT COUNT(*) FROM users WHERE username = 'admin'";
            ResultSet rs = stmt.executeQuery(checkAdmin);
            if (rs.next() && rs.getInt(1) == 0) {
                String insertAdmin =
                        "INSERT INTO users (username, password, email, phone, role) " +
                                "VALUES ('admin', 'admin123', 'admin@store.com', '+123456789', 'admin')";
                stmt.execute(insertAdmin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertTestData() {
        String checkParts = "SELECT COUNT(*) FROM parts";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(checkParts);
            if (rs.next() && rs.getInt(1) == 0) {
                String[][] parts = {
                        {"Масляный фильтр", "Фильтры", "Bosch", "850", "50", "Оригинальный масляный фильтр для двигателя"},
                        {"Тормозные колодки", "Тормозная система", "TRW", "3200", "30", "Комплект передних тормозных колодок"},
                        {"Воздушный фильтр", "Фильтры", "MANN", "1200", "45", "Салонный фильтр с активированным углем"},
                        {"Аккумулятор", "Электрооборудование", "Varta", "8900", "12", "60Ah стартерный аккумулятор"},
                        {"Свечи зажигания", "Двигатель", "NGK", "450", "100", "Иридиевые свечи зажигания"},
                        {"Ремень ГРМ", "Двигатель", "Gates", "2100", "25", "Комплект ремня ГРМ с роликами"},
                        {"Тормозные диски", "Тормозная система", "ATE", "4500", "18", "Вентилируемые тормозные диски"},
                        {"Амортизатор", "Подвеска", "Sachs", "5200", "15", "Передний амортизатор"},
                        {"Лямбда-зонд", "Электрооборудование", "Denso", "6800", "8", "Кислородный датчик"},
                        {"Шрус наружный", "Привод", "GKN", "3800", "20", "Шарнир равных угловых скоростей"}
                };

                String sql = "INSERT INTO parts (name, category, manufacturer, price, stock, description) VALUES (?, ?, ?, ?, ?, ?)";
                for (String[] data : parts) {
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, data[0]);
                    pstmt.setString(2, data[1]);
                    pstmt.setString(3, data[2]);
                    pstmt.setDouble(4, Double.parseDouble(data[3]));
                    pstmt.setInt(5, Integer.parseInt(data[4]));
                    pstmt.setString(6, data[5]);
                    pstmt.executeUpdate();
                }
                System.out.println("Тестовые данные добавлены!");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка добавления данных: " + e.getMessage());
        }
    }
}