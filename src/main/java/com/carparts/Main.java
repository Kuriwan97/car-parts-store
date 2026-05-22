package com.carparts;

import com.carparts.util.DatabaseUtil;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseUtil.getConnection();
            System.out.println("✅ База данных работает!");
            System.out.println("✅ Проект собран правильно!");
            System.out.println("\nТеперь запустите: mvn tomcat7:run");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}