package com.carparts.servlet;

import com.carparts.dao.PartDAO;
import com.carparts.model.Part;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class AddPartServlet extends HttpServlet {

    private PartDAO partDAO = new PartDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String category = req.getParameter("category");
        String manufacturer = req.getParameter("manufacturer");
        double price = Double.parseDouble(req.getParameter("price"));
        int stock = Integer.parseInt(req.getParameter("stock"));
        String description = req.getParameter("description");

        System.out.println("Добавление товара: " + name);

        Part part = new Part();
        part.setName(name);
        part.setCategory(category);
        part.setManufacturer(manufacturer);
        part.setPrice(price);
        part.setStock(stock);
        part.setDescription(description);

        try {
            partDAO.create(part);
            System.out.println("Товар добавлен успешно!");
            // Возвращаем успешный ответ
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.sendRedirect(req.getContextPath() + "/admin.html?success=1");
        } catch (SQLException e) {
            System.err.println("Ошибка добавления товара: " + e.getMessage());
            throw new ServletException(e);
        }
    }
}