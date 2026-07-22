package com.habitech.controller;

import com.habitech.dao.impl.UsuarioDaoImpl;
import com.habitech.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/usuarios")
public class UsuarioController extends HttpServlet {

    private final UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if ("eliminar".equals(accion)) {
            try {
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                usuarioDao.eliminarLogico(idEliminar);
            } catch (NumberFormatException e) {
                System.err.println("[Error] ID inválido para eliminación.");
            }
        } else if ("resetear".equals(accion)) {
            try {
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.trim().isEmpty()) {
                    int idReset = Integer.parseInt(idParam.trim());
                    String hashNuevo = BCrypt.hashpw("123456", BCrypt.gensalt(12));

                    boolean exito = usuarioDao.restablecerContrasena(idReset, hashNuevo);
                    if (!exito) {
                        System.err.println("[Error] No se pudo restablecer la contraseña en la base de datos.");
                    }
                }
            } catch (Exception e) {
                System.err.println("[Error] Excepción al restablecer contraseña.");
                e.printStackTrace();
            }
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=usuarios");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");
        String username = request.getParameter("username");
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String rol = request.getParameter("rol");

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuario.setRol(rol);

        if (idStr == null || idStr.trim().isEmpty() || "0".equals(idStr.trim())) {
            String passwordHaseado = BCrypt.hashpw("123456", BCrypt.gensalt(12));
            usuario.setPassword(passwordHaseado);
            usuarioDao.insertar(usuario);
        } else {
            try {
                int idExistente = Integer.parseInt(idStr.trim());
                usuario.setId(idExistente);
                usuarioDao.actualizar(usuario);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=usuarios");
    }
}