package com.carparts.servlet;

import com.carparts.dao.PartDAO;
import com.carparts.model.Part;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class PartsServlet extends HttpServlet {

    private final PartDAO partDAO = new PartDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();

        // Проверяем, авторизован ли пользователь как админ через сессию
        boolean isAdmin = false;
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("role") != null) {
            isAdmin = "admin".equals(session.getAttribute("role"));
        }

        try {
            List<Part> parts = partDAO.findAll();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Каталог запчастей</title>");
            out.println("<style>");
            out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
            out.println("body { font-family: 'Segoe UI', Arial, sans-serif; background: #f0f0f0; padding: 20px; }");
            out.println(".container { max-width: 100%; margin: 0 auto; background: white; border-radius: 10px; padding: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
            out.println("h2 { color: #333; margin-bottom: 20px; border-left: 4px solid #667eea; padding-left: 15px; }");
            out.println(".admin-badge { background: #c6f6d5; padding: 12px; border-radius: 8px; margin-bottom: 20px; text-align: center; color: #22543d; font-weight: bold; border-left: 4px solid #38a169; }");
            out.println("table { width: 100%; border-collapse: collapse; margin-top: 15px; }");
            out.println("th { background: #667eea; color: white; padding: 12px; text-align: left; font-weight: 600; }");
            out.println("td { padding: 12px; text-align: left; border-bottom: 1px solid #e2e8f0; }");
            out.println("tr:hover { background: #f7fafc; }");
            out.println(".price { color: #e53e3e; font-weight: bold; }");
            out.println(".stock { color: #38a169; }");
            out.println(".edit-btn { background: #4299e1; color: white; border: none; padding: 6px 12px; border-radius: 5px; cursor: pointer; text-decoration: none; display: inline-block; font-size: 12px; margin-right: 5px; transition: all 0.2s; }");
            out.println(".edit-btn:hover { background: #3182ce; transform: scale(1.02); }");
            out.println(".delete-btn { background: #e53e3e; color: white; border: none; padding: 6px 12px; border-radius: 5px; cursor: pointer; font-size: 12px; transition: all 0.2s; }");
            out.println(".delete-btn:hover { background: #c53030; transform: scale(1.02); }");
            out.println(".action-buttons { display: flex; gap: 8px; flex-wrap: wrap; }");
            out.println(".empty-message { text-align: center; padding: 40px; color: #718096; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");

            if (isAdmin) {
                out.println("<div class='admin-badge'>");
                out.println("🔧 <strong>Режим администратора</strong> | Доступно: редактирование ✏️ и удаление 🗑️ товаров");
                out.println("</div>");
            }

            out.println("<h2>📋 Каталог автозапчастей</h2>");

            if (parts.isEmpty()) {
                out.println("<div class='empty-message'>📦 Запчасти не найдены<br><small>Добавьте первую запчасть через форму выше</small></div>");
            } else {
                out.println("<table>");
                out.println("<thead>");
                out.println("<tr>");
                out.println("<th>ID</th>");
                out.println("<th>Наименование</th>");
                out.println("<th>Категория</th>");
                out.println("<th>Производитель</th>");
                out.println("<th>Цена, руб.</th>");
                out.println("<th>Наличие, шт.</th>");
                if (isAdmin) {
                    out.println("<th>Действия</th>");
                }
                out.println("</tr>");
                out.println("</thead>");
                out.println("<tbody>");

                for (Part part : parts) {
                    out.println("<tr>");
                    out.println("<td>" + part.getId() + "</td>");
                    out.println("<td>" + escapeHtml(part.getName()) + "</td>");
                    out.println("<td>" + escapeHtml(part.getCategory()) + "</td>");
                    out.println("<td>" + escapeHtml(part.getManufacturer()) + "</td>");
                    out.println("<td class='price'>" + part.getPrice() + " ₽</td>");
                    out.println("<td class='stock'>" + part.getStock() + "</td>");
                    if (isAdmin) {
                        out.println("<td class='action-buttons'>");
                        out.println("<a href='editPart?id=" + part.getId() + "' class='edit-btn' target='_top'>✏️ Редактировать</a>");
                        out.println("<button class='delete-btn' onclick='window.parent.deletePart(" + part.getId() + ")'>🗑️ Удалить</button>");
                        out.println("</td>");
                    }
                    out.println("</tr>");
                }

                out.println("</tbody>");
                out.println("</table>");
            }

            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        } catch (SQLException e) {
            out.println("<div class='container'>");
            out.println("<h3 style='color: #e53e3e;'>❌ Ошибка загрузки запчастей</h3>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("</div>");
            e.printStackTrace();
        }
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}