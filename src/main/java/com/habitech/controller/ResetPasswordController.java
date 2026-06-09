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

@WebServlet("/resetPassword")
public class ResetPasswordController extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDaoImpl();

    // EXPRESIÓN REGULAR: Mínimo 8 caracteres, 1 Mayúscula, 1 Minúscula, 1 Número, 1 Símbolo
    private static final String REGEX_SEGURIDAD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]{8,}$";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        // Verificamos que haya un usuario en sesión intentando cambiar la clave
        Usuario usuarioLogueado = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String nuevaPass = request.getParameter("txtNuevaPass");

        // 1. VALIDACIÓN LOGICA DE POLÍTICA DE SEGURIDAD
        if (nuevaPass == null || !nuevaPass.matches(REGEX_SEGURIDAD)) {
            request.setAttribute("errorValidacion", "La contraseña no cumple con los requisitos mínimos de seguridad.");
            request.getRequestDispatcher("/cambiar-password.jsp").forward(request, response);
            return;
        }

        // 2. ENCRIPTACIÓN CRIPTOGRÁFICA DE LA NUEVA CLAVE
        String hashNuevo = BCrypt.hashpw(nuevaPass, BCrypt.gensalt(12));

        // Reutilizamos el objeto de usuario en sesión y le seteamos el nuevo hash
        usuarioLogueado.setPassword(hashNuevo);

        // Usamos una pequeña variante de inserción/actualización directa para persistir la contraseña
        boolean actualizado = actualizarPasswordBD(usuarioLogueado.getId(), hashNuevo);

        if (actualizado) {
            System.out.println("[ResetPassword] -> Contraseña actualizada con éxito para: " + usuarioLogueado.getUsername());
            // Acceso concedido directo al Dashboard
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("errorValidacion", "Hubo un error interno al guardar la contraseña en la base de datos.");
            request.getRequestDispatcher("/cambiar-password.jsp").forward(request, response);
        }
    }

    // Método rápido de persistencia exclusivo para credenciales
    private boolean actualizarPasswordBD(int idUsuario, String nuevoHash) {
        String sql = "UPDATE usuarios SET password = ? WHERE id = ?";
        try (java.sql.Connection con = com.habitech.config.ConexionDB.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoHash);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}