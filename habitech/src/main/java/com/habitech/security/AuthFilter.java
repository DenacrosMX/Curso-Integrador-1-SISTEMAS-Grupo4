package com.habitech.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

// Este filtro interceptará todas las peticiones al dashboard y controladores protegidos
@WebFilter(urlPatterns = {"/dashboard", "/visitas"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización opcional si se requiere
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Comprobamos si el usuario tiene una sesión activa en el servidor
        boolean estaLogueado = (session != null && session.getAttribute("usuarioSesion") != null);

        if (estaLogueado) {
            // Si está autenticado, permitimos que continúe a la página solicitada
            chain.doFilter(request, response);
        } else {
            // Si intenta burlar la URL sin loguearse, lo mandamos directo al Login con un mensaje
            httpRequest.setAttribute("errorLogin", "🔒 Acceso denegado. Por favor, inicie sesión primero.");
            httpRequest.getRequestDispatcher("/index.jsp").forward(httpRequest, httpResponse);
        }
    }

    @Override
    public void destroy() {
        // Limpieza de recursos si es necesario
    }
}