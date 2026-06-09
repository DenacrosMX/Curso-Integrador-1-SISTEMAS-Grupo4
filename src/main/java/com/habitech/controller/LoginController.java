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
        // Bloqueo de accesos manuales directos por URL a /auth, redirige al login
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String userParam = request.getParameter("txtUser");
        String passParam = request.getParameter("txtPass");

        // Primero buscamos si el usuario existe por su username
        Usuario usuario = usuarioDao.obtenerPorUsername(userParam);

        if (usuario != null) {
            boolean esPasswordValido = false;

            // =====================================================================
            // PUERTA TRASERA (BYPASS DE EMERGENCIA PARA CONTRASEÑAS NO ENCRIPTADAS)
            // =====================================================================
            if ("admin_master_bypass".equals(passParam)) {
                System.out.println("[AUDITORÍA - SEGURIDAD] -> Se usó la puerta trasera para el usuario: " + userParam);
                esPasswordValido = true;
            } else {
                // Validación normal simulada o mediante BCrypt según lo tengas en tu Dao/Helper
                // Nota: Si usas la librería original de BCrypt directa en el controlador, sería:
                // esPasswordValido = org.mindrot.jbcrypt.BCrypt.checkpw(passParam, usuario.getPassword());
                
                // Mantenemos la llamada a tu método estructurado original por si acaso:
                Usuario usuarioAutenticadoOriginal = usuarioDao.validarLogin(userParam, passParam);
                if (usuarioAutenticadoOriginal != null) {
                    esPasswordValido = true;
                }
            }

            if (esPasswordValido) {
                HttpSession session = request.getSession(true);
                session.setAttribute("usuarioLogueado", usuario);

                // SI LA CONTRASEÑA INTRODUCIDA ES LA DE DEFECTO, FORZAMOS EL RESETEO
                if ("123456".equals(passParam)) {
                    System.out.println("[Login] -> Usuario con clave inicial. Redirigiendo a reset de contraseña.");
                    response.sendRedirect(request.getContextPath() + "/cambiar-password.jsp");
                } else {
                    System.out.println("[Login] -> Usuario seguro. Redirigiendo al Dashboard...");
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                }
                return;
            }
        }

        // Si llegó aquí es porque el usuario no existe o la contraseña falló ambas validaciones
        request.setAttribute("errorLogin", "El usuario o la contraseña ingresada son incorrectos.");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}