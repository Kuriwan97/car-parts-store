package com.carparts.servlet;

import com.carparts.dao.PartDAO;
import com.carparts.dao.UserDAO;
import com.carparts.model.Part;
import com.carparts.model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminServlet extends HttpServlet {
    private PartDAO partDAO = new PartDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Part> parts = partDAO.findAll();
            List<User> users = userDAO.findAll();

            req.setAttribute("parts", parts);
            req.setAttribute("users", users);
            req.getRequestDispatcher("/admin.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}