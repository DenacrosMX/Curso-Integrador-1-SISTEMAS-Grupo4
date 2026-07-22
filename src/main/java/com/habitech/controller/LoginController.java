package com.habitech.controller;

import com.habitech.dao.UsuarioDao;
import com.habitech.dao.impl.UsuarioDaoImpl;
import com.habitech.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/auth")
public class LoginController extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String userParam = request.getParameter("txtUser");
        String passParam = request.getParameter("txtPass");

        Usuario usuarioAutenticado = usuarioDao.validarLogin(userParam, passParam);

        if (usuarioAutenticado != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogueado", usuarioAutenticado);

            if ("123456".equals(passParam)) {
                response.sendRedirect(request.getContextPath() + "/cambiar-password.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else {
            request.setAttribute("errorLogin", "El usuario o la contraseña ingresada son incorrectos.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}