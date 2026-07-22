package com.habitech.controller;

import com.habitech.dao.ComunicadoDao;
import com.habitech.dao.impl.ComunicadoDaoImpl;
import com.habitech.model.Comunicado;
import com.habitech.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.OffsetDateTime;

@WebServlet("/comunicados")
public class ComunicadoController extends HttpServlet {

    private final ComunicadoDao comunicadoDao = new ComunicadoDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if ("eliminar".equals(accion) && request.getParameter("id") != null) {
            try {
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                comunicadoDao.eliminarLogico(idEliminar);
            } catch (NumberFormatException e) {
                System.err.println("[Error] ID inválido para la eliminación de comunicado.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=comunicados");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Usuario usuarioSesion = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String idStr = request.getParameter("id");
        String titulo = request.getParameter("titulo");
        String contenido = request.getParameter("contenido");
        String alcance = request.getParameter("alcance");
        String torreDestino = request.getParameter("torre_destino");
        String categoria = request.getParameter("categoria");
        String estado = request.getParameter("estado");
        String fechaExpStr = request.getParameter("fecha_expiracion");

        if ("GLOBAL".equals(alcance) || torreDestino == null || torreDestino.trim().isEmpty()) {
            torreDestino = null;
        }

        try {
            Comunicado comunicado = new Comunicado();
            comunicado.setTitulo(titulo);
            comunicado.setContenido(contenido);
            comunicado.setAlcance(alcance);
            comunicado.setTorreDestino(torreDestino);
            comunicado.setCategoria(categoria);
            comunicado.setEstado(estado);

            if (fechaExpStr != null && !fechaExpStr.trim().isEmpty()) {
                try {
                    comunicado.setFechaExpiracion(OffsetDateTime.parse(fechaExpStr + ":00Z"));
                } catch (Exception e) {
                    comunicado.setFechaExpiracion(null);
                }
            }

            if (idStr == null || idStr.trim().isEmpty()) {
                comunicado.setUsuarioId(usuarioSesion.getId());
                comunicadoDao.insertar(comunicado);
            } else {
                comunicado.setId(Integer.parseInt(idStr.trim()));
                comunicadoDao.actualizar(comunicado);
            }
        } catch (NumberFormatException e) {
            System.err.println("[Error] ID inválido para el procesamiento del comunicado.");
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=comunicados");
    }
}