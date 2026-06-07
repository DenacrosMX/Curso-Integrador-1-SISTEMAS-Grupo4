package com.habitech.controller;

import com.habitech.dao.IncidenciaDAO;
import com.habitech.dao.impl.IncidenciaDAOImpl;
import com.habitech.model.IncidenciaModel;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/incidencias")
public class IncidenciaController extends HttpServlet {
    private final IncidenciaDAO incidenciaDAO = new IncidenciaDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("reportar".equals(action)) {
            try {
                int inmuebleId = Integer.parseInt(request.getParameter("inmuebleId"));
                String titulo = request.getParameter("titulo");
                String descripcion = request.getParameter("descripcion");
                String prioridad = request.getParameter("prioridad");

                IncidenciaModel ticket = new IncidenciaModel(0, inmuebleId, titulo, descripcion, prioridad, "ABIERTO", null, null, null);

                if (incidenciaDAO.registrarIncidencia(ticket)) {
                    request.getSession().setAttribute("mensajeExito", "🔧 Ticket de falla creado y asignado a Mesa de Ayuda.");
                } else {
                    request.getSession().setAttribute("mensajeError", "❌ No se pudo guardar el reporte técnico.");
                }
            } catch (Exception e) {
                request.getSession().setAttribute("mensajeError", "❌ Error al procesar los parámetros del reporte.");
            }
        } else if ("cambiarEstado".equals(action)) {
            try {
                int idIncidencia = Integer.parseInt(request.getParameter("idIncidencia"));
                String nuevoEstado = request.getParameter("nuevoEstado");

                if (incidenciaDAO.actualizarEstado(idIncidencia, nuevoEstado)) {
                    request.getSession().setAttribute("mensajeExito", "💼 Ticket de falla actualizado a estado: " + nuevoEstado);
                } else {
                    request.getSession().setAttribute("mensajeError", "❌ No se pudo actualizar el estado de la incidencia.");
                }
            } catch (Exception e) {
                request.getSession().setAttribute("mensajeError", "❌ Error en los parámetros de actualización.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/dashboard?view=mesa_ayuda");
    }
}