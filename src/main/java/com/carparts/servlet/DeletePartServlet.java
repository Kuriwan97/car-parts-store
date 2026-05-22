package com.carparts.servlet;

import com.carparts.dao.PartDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class DeletePartServlet extends HttpServlet {

    private PartDAO partDAO = new PartDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID не указан");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            boolean deleted = partDAO.delete(id);

            if (deleted) {
                System.out.println("Товар с ID " + id + " удален");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Товар не найден");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат ID");
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении: " + e.getMessage());
            throw new ServletException(e);
        }
    }
}