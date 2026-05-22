package com.carparts.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthFilter implements Filter {

    // Список публичных страниц (доступны без авторизации)
    private static final List<String> PUBLIC_PAGES = Arrays.asList(
            "login.html", "register.html", "login", "register",
            ".css", "/css/", "favicon.ico"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthFilter инициализирован");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        System.out.println("Запрос: " + path);

        // Проверка публичных страниц
        for (String publicPage : PUBLIC_PAGES) {
            if (path.equals("/" + publicPage) || path.equals(publicPage) ||
                    path.contains(publicPage) && publicPage.startsWith(".")) {
                chain.doFilter(request, response);
                return;
            }
        }

        // Проверка авторизации
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("Не авторизован -> перенаправление на login.html");
            res.sendRedirect(contextPath + "/login.html");
            return;
        }

        String role = (String) session.getAttribute("role");
        System.out.println("Авторизован: " + session.getAttribute("username") + ", роль: " + role);

        // Доступ к админским страницам только для админа
        if (path.contains("/admin") || path.contains("/addPart") ||
                path.contains("/editPart") || path.contains("/deletePart")) {
            if (!"admin".equals(role)) {
                System.out.println("Доступ запрещен для пользователя с ролью: " + role);
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещен. Требуются права администратора.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("AuthFilter уничтожен");
    }
}