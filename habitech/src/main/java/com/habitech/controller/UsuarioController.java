package com.habitech.controller;

import com.habitech.dao.UsuarioDAO;
import com.habitech.dao.impl.UsuarioDAOImpl;
import com.habitech.model.UsuarioModel;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/usuarios")
public class UsuarioController extends HttpServlet {
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("registrar".equals(action)) {
            try {
                UsuarioModel u = new UsuarioModel();
                u.setUsername(request.getParameter("username"));
                u.setPassword(request.getParameter("password")); // Viaja en texto plano del form, se encripta en el DAO
                u.setNombres(request.getParameter("nombres"));
                u.setApellidos(request.getParameter("apellidos"));
                u.setEmail(request.getParameter("email"));
                u.setTelefono(request.getParameter("telefono"));
                u.setRol(request.getParameter("rol"));
                u.setEstado("ACTIVO");

                if (usuarioDAO.registrarUsuario(u)) {
                    request.getSession().setAttribute("mensajeExito", "👤 Usuario registrado bajo firma criptográfica BCrypt con éxito.");
                } else {
                    request.getSession().setAttribute("mensajeError", "❌ Error: El DNI o Correo ya se encuentran registrados.");
                }
            } catch (Exception e) {
                request.getSession().setAttribute("mensajeError", "❌ Ocurrió un error inesperado al guardar el operador.");
            }
        } else if ("cambiarEstado".equals(action)) {
            try {
                int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                String nuevoEstado = request.getParameter("nuevoEstado");

                if (usuarioDAO.cambiarEstado(idUsuario, nuevoEstado)) {
                    request.getSession().setAttribute("mensajeExito", "🔄 Estado de acceso actualizado correctamente.");
                } else {
                    request.getSession().setAttribute("mensajeError", "❌ No se pudo modificar el estado del usuario.");
                }
            } catch (Exception e) {
                request.getSession().setAttribute("mensajeError", "❌ Error al cambiar el estado lógico.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?view=usuarios");
    }
}