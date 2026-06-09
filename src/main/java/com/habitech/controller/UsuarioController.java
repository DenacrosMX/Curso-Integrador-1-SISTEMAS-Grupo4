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
import java.io.IOException;

@WebServlet("/usuarios")
public class UsuarioController extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("eliminar".equals(accion)) {
            int idEliminar = Integer.parseInt(request.getParameter("id"));
            usuarioDao.eliminarLogico(idEliminar);
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

        if (idStr == null || idStr.trim().isEmpty()) {
            // ENCRIPTAMOS LA CLAVE POR DEFECTO "123456" ANTES DE ENVIAR AL DAO
            String passwordPlano = "123456";
            String passwordHaseado = BCrypt.hashpw(passwordPlano, BCrypt.gensalt(12));

            usuario.setPassword(passwordHaseado);
            usuarioDao.insertar(usuario);
        } else {
            usuario.setId(Integer.parseInt(idStr));
            usuarioDao.actualizar(usuario);
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=usuarios");
    }
}