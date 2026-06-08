package com.habitech.controller;

import com.habitech.dao.UsuarioDAO;
import com.habitech.dao.impl.UsuarioDAOImpl;
import com.habitech.model.UsuarioModel;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/auth")
public class LoginController extends HttpServlet {
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    // GET maneja el Logout (Cerrar Sesión)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Destruye la sesión del usuario
        }
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    // POST maneja el Login (Iniciar Sesión)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Autentica usando BCrypt internamente en el DAO
        UsuarioModel usuarioLogueado = usuarioDAO.autenticar(username, password);

        if (usuarioLogueado != null) {
            // Guardamos el objeto usuario en la sesión web
            HttpSession session = request.getSession();
            session.setAttribute("usuarioSesion", usuarioLogueado);

            // Redirecciona al Dashboard principal
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            // Si las credenciales fallan, devuelve un mensaje explicativo
            request.setAttribute("errorLogin", "❌ Credenciales incorrectas o usuario inactivo.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}