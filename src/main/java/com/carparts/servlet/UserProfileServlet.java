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

public class UserProfileServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Мой профиль - Магазин автозапчастей</title>");
        out.println("<style>");
        out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
        out.println("body { font-family: Arial, sans-serif; background: #f0f0f0; }");
        out.println(".container { max-width: 1000px; margin: 0 auto; padding: 20px; }");
        out.println(".header { background: white; padding: 20px; border-radius: 10px; margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center; }");
        out.println(".header h1 { color: #333; }");
        out.println(".nav { display: flex; gap: 15px; }");
        out.println(".nav a { color: #667eea; text-decoration: none; padding: 8px 16px; border-radius: 5px; }");
        out.println(".nav a:hover { background: #667eea; color: white; }");
        out.println(".logout-btn { background: #e53e3e; color: white; padding: 8px 16px; border-radius: 5px; text-decoration: none; }");
        out.println(".logout-btn:hover { background: #c53030; }");
        out.println(".profile-card { background: white; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        out.println(".profile-header { text-align: center; margin-bottom: 30px; }");
        out.println(".profile-header .avatar { width: 100px; height: 100px; background: #667eea; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 20px; }");
        out.println(".profile-header .avatar span { font-size: 48px; color: white; }");
        out.println(".profile-header h2 { color: #333; }");
        out.println(".profile-header p { color: #666; margin-top: 5px; }");
        out.println(".info-section { margin-bottom: 30px; }");
        out.println(".info-section h3 { color: #667eea; margin-bottom: 15px; padding-bottom: 5px; border-bottom: 2px solid #667eea; }");
        out.println(".info-row { display: flex; padding: 12px 0; border-bottom: 1px solid #eee; }");
        out.println(".info-label { width: 150px; font-weight: bold; color: #555; }");
        out.println(".info-value { flex: 1; color: #333; }");
        out.println(".edit-btn { background: #667eea; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; font-size: 16px; }");
        out.println(".edit-btn:hover { background: #5a67d8; }");
        out.println(".edit-form { display: none; margin-top: 20px; padding: 20px; background: #f7fafc; border-radius: 10px; }");
        out.println(".edit-form input { width: 100%; padding: 10px; margin: 10px 0; border: 1px solid #ddd; border-radius: 5px; }");
        out.println(".edit-form button { background: #38a169; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; margin-right: 10px; }");
        out.println(".edit-form button.cancel { background: #a0aec0; }");
        out.println(".message { padding: 10px; border-radius: 5px; margin-bottom: 20px; display: none; }");
        out.println(".success { background: #c6f6d5; color: #22543d; }");
        out.println(".error { background: #fed7d7; color: #742a2a; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<div class='header'>");
        out.println("<h1>🚗 Магазин автозапчастей</h1>");
        out.println("<div class='nav'>");
        out.println("<a href='user-catalog.html'>📦 Каталог</a>");
        out.println("<a href='profile'>👤 Мой профиль</a>");
        out.println("<a href='logout' class='logout-btn'>🚪 Выйти</a>");
        out.println("</div>");
        out.println("</div>");

        out.println("<div id='message' class='message'></div>");

        out.println("<div class='profile-card'>");
        out.println("<div class='profile-header'>");
        out.println("<div class='avatar'><span>👤</span></div>");
        out.println("<h2>" + escapeHtml(currentUser.getUsername()) + "</h2>");
        out.println("<p>" + ("admin".equals(currentUser.getRole()) ? "Администратор" : "Пользователь") + "</p>");
        out.println("</div>");

        out.println("<div class='info-section'>");
        out.println("<h3>📋 Личная информация</h3>");
        out.println("<div class='info-row'>");
        out.println("<div class='info-label'>ID пользователя:</div>");
        out.println("<div class='info-value'>" + currentUser.getId() + "</div>");
        out.println("</div>");
        out.println("<div class='info-row'>");
        out.println("<div class='info-label'>Имя пользователя:</div>");
        out.println("<div class='info-value' id='displayUsername'>" + escapeHtml(currentUser.getUsername()) + "</div>");
        out.println("</div>");
        out.println("<div class='info-row'>");
        out.println("<div class='info-label'>Email:</div>");
        out.println("<div class='info-value' id='displayEmail'>" + escapeHtml(currentUser.getEmail()) + "</div>");
        out.println("</div>");
        out.println("<div class='info-row'>");
        out.println("<div class='info-label'>Телефон:</div>");
        out.println("<div class='info-value' id='displayPhone'>" + (currentUser.getPhone() != null && !currentUser.getPhone().isEmpty() ? escapeHtml(currentUser.getPhone()) : "Не указан") + "</div>");
        out.println("</div>");
        out.println("</div>");

        out.println("<button class='edit-btn' onclick='showEditForm()'>✏️ Редактировать профиль</button>");

        out.println("<div id='editForm' class='edit-form'>");
        out.println("<h3>Редактирование профиля</h3>");
        out.println("<form id='profileForm' method='post'>");
        out.println("<input type='text' id='editUsername' name='username' placeholder='Имя пользователя' value='" + escapeHtml(currentUser.getUsername()) + "' required>");
        out.println("<input type='email' id='editEmail' name='email' placeholder='Email' value='" + escapeHtml(currentUser.getEmail()) + "' required>");
        out.println("<input type='tel' id='editPhone' name='phone' placeholder='Телефон' value='" + (currentUser.getPhone() != null ? escapeHtml(currentUser.getPhone()) : "") + "'>");
        out.println("<input type='password' id='editPassword' name='password' placeholder='Новый пароль (оставьте пустым, чтобы не менять)'>");
        out.println("<button type='submit'>💾 Сохранить изменения</button>");
        out.println("<button type='button' class='cancel' onclick='hideEditForm()'>❌ Отмена</button>");
        out.println("</form>");
        out.println("</div>");

        out.println("</div>");
        out.println("</div>");

        out.println("<script>");
        out.println("function showEditForm() {");
        out.println("    document.getElementById('editForm').style.display = 'block';");
        out.println("}");
        out.println("function hideEditForm() {");
        out.println("    document.getElementById('editForm').style.display = 'none';");
        out.println("}");
        out.println("");
        out.println("document.getElementById('profileForm').addEventListener('submit', function(e) {");
        out.println("    e.preventDefault();");
        out.println("    const formData = new URLSearchParams();");
        out.println("    formData.append('username', document.getElementById('editUsername').value);");
        out.println("    formData.append('email', document.getElementById('editEmail').value);");
        out.println("    formData.append('phone', document.getElementById('editPhone').value);");
        out.println("    formData.append('password', document.getElementById('editPassword').value);");
        out.println("    ");
        out.println("    fetch('profile', {");
        out.println("        method: 'POST',");
        out.println("        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },");
        out.println("        body: formData");
        out.println("    })");
        out.println("    .then(response => response.json())");
        out.println("    .then(data => {");
        out.println("        if(data.success) {");
        out.println("            showMessage('Профиль успешно обновлен!', 'success');");
        out.println("            setTimeout(() => { location.reload(); }, 1500);");
        out.println("        } else {");
        out.println("            showMessage(data.message || 'Ошибка при обновлении', 'error');");
        out.println("        }");
        out.println("    })");
        out.println("    .catch(error => {");
        out.println("        showMessage('Ошибка: ' + error, 'error');");
        out.println("    });");
        out.println("});");
        out.println("");
        out.println("function showMessage(text, type) {");
        out.println("    const msgDiv = document.getElementById('message');");
        out.println("    msgDiv.textContent = text;");
        out.println("    msgDiv.className = 'message ' + type;");
        out.println("    msgDiv.style.display = 'block';");
        out.println("    setTimeout(() => { msgDiv.style.display = 'none'; }, 3000);");
        out.println("}");
        out.println("</script>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.getWriter().write("{\"success\": false, \"message\": \"Не авторизован\"}");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");

        try {
            // Проверяем, не занято ли новое имя пользователя
            User existingUser = userDAO.findByUsername(username);
            if (existingUser != null && existingUser.getId() != currentUser.getId()) {
                resp.getWriter().write("{\"success\": false, \"message\": \"Имя пользователя уже занято\"}");
                return;
            }

            currentUser.setUsername(username);
            currentUser.setEmail(email);
            currentUser.setPhone(phone);

            if (password != null && !password.trim().isEmpty()) {
                currentUser.setPassword(password);
            }

            if (userDAO.update(currentUser)) {
                session.setAttribute("user", currentUser);
                session.setAttribute("username", currentUser.getUsername());
                resp.getWriter().write("{\"success\": true, \"message\": \"Профиль обновлен\"}");
            } else {
                resp.getWriter().write("{\"success\": false, \"message\": \"Ошибка при обновлении\"}");
            }
        } catch (SQLException e) {
            resp.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
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