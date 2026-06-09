package com.habitech.controller;

import com.habitech.dao.ComunicadoDao;
import com.habitech.dao.impl.ComunicadoDaoImpl;
import com.habitech.model.Comunicado;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            int idEliminar = Integer.parseInt(request.getParameter("id"));
            comunicadoDao.eliminarLogico(idEliminar);
        }

        // Redirección inmediata al esqueleto para que refresque la grilla con JSTL
        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=comunicados");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");
        String titulo = request.getParameter("titulo");
        String contenido = request.getParameter("contenido");
        String alcance = request.getParameter("alcance");
        String torreDestino = request.getParameter("torre_destino");
        String categoria = request.getParameter("categoria");
        String estado = request.getParameter("estado");
        String fechaExpStr = request.getParameter("fecha_expiracion");

        // Validar si la torre destino viene vacía o es Global
        if ("GLOBAL".equals(alcance) || torreDestino == null || torreDestino.trim().isEmpty()) {
            torreDestino = null;
        }

        Comunicado comunicado = new Comunicado();
        comunicado.setTitulo(titulo);
        comunicado.setContenido(contenido);
        comunicado.setAlcance(alcance);
        comunicado.setTorreDestino(torreDestino);
        comunicado.setCategoria(categoria);
        comunicado.setEstado(estado);

        // Parsear fecha de expiración opcional del formulario (formato HTML datetime-local suele venir como YYYY-MM-DDTHH:mm)
        if (fechaExpStr != null && !fechaExpStr.trim().isEmpty()) {
            try {
                comunicado.setFechaExpiracion(OffsetDateTime.parse(fechaExpStr + ":00Z"));
            } catch (Exception e) {
                comunicado.setFechaExpiracion(null);
            }
        }

        if (idStr == null || idStr.trim().isEmpty()) {
            // Hardcodeamos temporalmente el usuario_id emisor en 1 (Simulando el ID del Admin logueado)
            // Cuando agregues el login real, lo sacarás de: ((Usuario) request.getSession().getAttribute("user")).getId()
            comunicado.setUsuarioId(1);
            comunicadoDao.insertar(comunicado);
        } else {
            comunicado.setId(Integer.parseInt(idStr));
            comunicadoDao.actualizar(comunicado);
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?modulo=comunicados");
    }
}