package com.carparts.servlet;

import com.carparts.dao.PartDAO;
import com.carparts.model.Part;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class EditPartServlet extends HttpServlet {

    private PartDAO partDAO = new PartDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin.html");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Part part = partDAO.findById(id);

            if (part == null) {
                resp.sendRedirect(req.getContextPath() + "/admin.html");
                return;
            }

            PrintWriter out = resp.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Редактирование товара</title>");
            out.println("<style>");
            out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
            out.println("body { font-family: Arial, sans-serif; background: #f0f0f0; padding: 40px; }");
            out.println(".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
            out.println("h1 { color: #333; margin-bottom: 30px; text-align: center; }");
            out.println(".form-group { margin-bottom: 20px; }");
            out.println("label { display: block; margin-bottom: 5px; color: #555; font-weight: bold; }");
            out.println("input, textarea, select { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px; font-size: 14px; }");
            out.println("input:focus, textarea:focus { outline: none; border-color: #667eea; }");
            out.println("button { background: #667eea; color: white; padding: 12px 24px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px; margin-right: 10px; }");
            out.println("button:hover { background: #5a67d8; }");
            out.println(".cancel-btn { background: #a0aec0; }");
            out.println(".cancel-btn:hover { background: #718096; }");
            out.println(".buttons { margin-top: 30px; text-align: center; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1>✏️ Редактирование запчасти</h1>");
            out.println("<form id='editForm' method='post'>");
            out.println("<input type='hidden' name='id' value='" + part.getId() + "'>");
            out.println("<div class='form-group'>");
            out.println("<label>Наименование:</label>");
            out.println("<input type='text' name='name' value='" + escapeHtml(part.getName()) + "' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>Категория:</label>");
            out.println("<input type='text' name='category' value='" + escapeHtml(part.getCategory()) + "' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>Производитель:</label>");
            out.println("<input type='text' name='manufacturer' value='" + escapeHtml(part.getManufacturer()) + "' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>Цена (руб.):</label>");
            out.println("<input type='number' step='0.01' name='price' value='" + part.getPrice() + "' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>Количество на складе:</label>");
            out.println("<input type='number' name='stock' value='" + part.getStock() + "' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>Описание:</label>");
            out.println("<textarea name='description' rows='4'>" + escapeHtml(part.getDescription()) + "</textarea>");
            out.println("</div>");
            out.println("<div class='buttons'>");
            out.println("<button type='submit'>💾 Сохранить изменения</button>");
            out.println("<button type='button' class='cancel-btn' onclick='window.location.href=\"admin.html\"'>❌ Отмена</button>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("<script>");
            out.println("document.getElementById('editForm').addEventListener('submit', function(e) {");
            out.println("    e.preventDefault();");
            out.println("    const formData = new URLSearchParams(new FormData(this));");
            out.println("    fetch('editPart', {");
            out.println("        method: 'POST',");
            out.println("        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },");
            out.println("        body: formData");
            out.println("    })");
            out.println("    .then(response => {");
            out.println("        if(response.ok) {");
            out.println("            window.location.href = 'admin.html?edited=1';");
            out.println("        } else {");
            out.println("            alert('Ошибка при сохранении');");
            out.println("        }");
            out.println("    });");
            out.println("});");
            out.println("</script>");
            out.println("</body>");
            out.println("</html>");

        } catch (NumberFormatException | SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String category = req.getParameter("category");
        String manufacturer = req.getParameter("manufacturer");
        double price = Double.parseDouble(req.getParameter("price"));
        int stock = Integer.parseInt(req.getParameter("stock"));
        String description = req.getParameter("description");

        Part part = new Part();
        part.setId(id);
        part.setName(name);
        part.setCategory(category);
        part.setManufacturer(manufacturer);
        part.setPrice(price);
        part.setStock(stock);
        part.setDescription(description);

        try {
            partDAO.update(part);
            System.out.println("Товар с ID " + id + " обновлен");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении: " + e.getMessage());
            throw new ServletException(e);
        }
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}