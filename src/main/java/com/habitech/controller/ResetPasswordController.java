package com.habitech.controller;

import com.habitech.dao.UsuarioDao;
import com.habitech.dao.impl.UsuarioDaoImpl;
import com.habitech.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/resetPassword")
public class ResetPasswordController extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDaoImpl();

    private static final String REGEX_SEGURIDAD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]{8,}$";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        Usuario usuarioLogueado = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String nuevaPass = request.getParameter("txtNuevaPass");

        if (nuevaPass == null || !nuevaPass.matches(REGEX_SEGURIDAD)) {
            request.setAttribute("errorValidacion", "La contraseña no cumple con los requisitos mínimos de seguridad.");
            request.getRequestDispatcher("/cambiar-password.jsp").forward(request, response);
            return;
        }

        String hashNuevo = BCrypt.hashpw(nuevaPass, BCrypt.gensalt(12));
        usuarioLogueado.setPassword(hashNuevo);

        boolean actualizado = actualizarPasswordBD(usuarioLogueado.getId(), hashNuevo);

        if (actualizado) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("errorValidacion", "Hubo un error interno al guardar la contraseña en la base de datos.");
            request.getRequestDispatcher("/cambiar-password.jsp").forward(request, response);
        }
    }

    private boolean actualizarPasswordBD(int idUsuario, String nuevoHash) {
        String sql = "UPDATE usuarios SET password = ? WHERE id = ?";
        try (Connection con = com.habitech.config.ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoHash);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}