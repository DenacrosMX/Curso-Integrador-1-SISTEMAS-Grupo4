package com.habitech.security;

import com.habitech.model.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(urlPatterns = {"/*"}) // Cambiado a /* para interceptar todos los servlets mapeados
public class AuthFilter extends HttpFilter {

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/login.jsp", "/auth", "/logout", "/cambiar-password.jsp", "/reset-password"
    );

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String path = request.getRequestURI().substring(request.getContextPath().length());

        // 1. Permitir archivos estáticos de tus carpetas
        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/img/") || path.startsWith("/uploads/")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Permitir paths públicos de autenticación
        if (PUBLIC_PATHS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        Usuario usuarioLogueado = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        // 3. Control de Autenticación básico
        if (usuarioLogueado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String rol = usuarioLogueado.getRol();

        // 4. Si es ADMIN_SISTEMA no tiene restricciones
        if ("ADMIN_SISTEMA".equals(rol)) {
            chain.doFilter(request, response);
            return;
        }

        // 5. Restricción estricta por Roles a URLs Directas
        if ("/usuarios".equals(path) || "/configuracion".equals(path) || "/infraestructura".equals(path) || "/asignaciones".equals(path) || "/exportarExcel".equals(path)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        if ("/boletas".equals(path) && "CONSERJE".equals(rol)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        if ("/visitas".equals(path) && "RESIDENTE".equals(rol)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // 6. Restricción estricta al parámetro ?modulo= dentro del DashboardController
        if ("/dashboard".equals(path)) {
            String modulo = request.getParameter("modulo");
            if (modulo != null && !modulo.trim().isEmpty()) {

                if ("CONSERJE".equals(rol)) {
                    // El conserje NO puede entrar a estos módulos internos
                    if (Arrays.asList("usuarios", "configuracion", "infraestructura", "asignaciones", "boletas", "reservas").contains(modulo)) {
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }
                }

                if ("RESIDENTE".equals(rol)) {
                    // El residente NO puede entrar a estos módulos internos
                    if (Arrays.asList("usuarios", "configuracion", "infraestructura", "asignaciones", "visitas").contains(modulo)) {
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }
}