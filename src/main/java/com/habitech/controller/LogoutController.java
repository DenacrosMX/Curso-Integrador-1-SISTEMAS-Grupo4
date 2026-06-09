package com.habitech.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Recuperamos la sesión actual sin crear una nueva
        HttpSession session = request.getSession(false);

        if (session != null) {
            System.out.println("[LogoutController] -> Invalidando sesión de usuario.");
            session.invalidate(); // Destruye la sesión por completo
        }

        // Redirige al usuario inmediatamente a la página de login
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}